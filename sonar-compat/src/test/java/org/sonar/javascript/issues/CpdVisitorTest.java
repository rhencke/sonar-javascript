/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.javascript.issues;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.typed.ActionParser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.internal.google.common.io.Files;
import org.sonar.javascript.parser.JavaScriptParserBuilder;
import org.sonar.plugins.javascript.api.tree.ScriptTree;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.visitors.TreeVisitorContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CpdVisitorTest {

  private static final Charset CHARSET = Charsets.UTF_8;

  private final ActionParser<Tree> p = JavaScriptParserBuilder.createParser(CHARSET);

  private DefaultInputFile inputFile;
  private NewCpdTokens cpdTokens;

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Test
  public void test() throws Exception {
    scan("var x = 'a' + 1 + 'line1\nline2';");
    verify(cpdTokens).addToken(inputFile.newRange(1, 0, 1, 3), "var");
    verify(cpdTokens).addToken(inputFile.newRange(1, 4, 1, 5), "x");
    verify(cpdTokens).addToken(inputFile.newRange(1, 6, 1, 7), "=");
    verify(cpdTokens).addToken(inputFile.newRange(1, 8, 1, 11), "LITERAL");
    verify(cpdTokens).addToken(inputFile.newRange(1, 12, 1, 13), "+");
    verify(cpdTokens).addToken(inputFile.newRange(1, 14, 1, 15), "1");
    verify(cpdTokens).addToken(inputFile.newRange(1, 16, 1, 17), "+");
    verify(cpdTokens).addToken(inputFile.newRange(1, 18, 2, 6), "LITERAL");
    verify(cpdTokens).addToken(inputFile.newRange(2, 6, 2, 7), ";");
    verify(cpdTokens).save();
  }

  private void scan(String source) throws IOException {
    File file = tempFolder.newFile();
    Files.write(source, file, CHARSET);
    
    DefaultFileSystem fileSystem = new DefaultFileSystem(file.getParentFile());
    fileSystem.setEncoding(CHARSET);
    inputFile = new DefaultInputFile(file.getName(), file.getName())
      .setLanguage("js")
      .setType(Type.MAIN)
      .initMetadata(new FileMetadata().readMetadata(file, CHARSET));
    fileSystem.add(inputFile);

    SensorContext sensorContext = mock(SensorContext.class);
    cpdTokens = mock(NewCpdTokens.class);
    when(sensorContext.newCpdTokens()).thenReturn(cpdTokens);
    when(cpdTokens.onFile(inputFile)).thenReturn(cpdTokens);

    TreeVisitorContext visitorContext = mock(TreeVisitorContext.class);
    when(visitorContext.getFile()).thenReturn(file);

    CpdVisitor cpdVisitor = new CpdVisitor(fileSystem, sensorContext);
    Tree tree = p.parse(file);
    when(visitorContext.getTopTree()).thenReturn((ScriptTree) tree);
    cpdVisitor.scanTree(visitorContext);
  }

}
