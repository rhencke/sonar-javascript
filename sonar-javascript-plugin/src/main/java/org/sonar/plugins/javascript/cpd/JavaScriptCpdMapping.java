/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011 SonarSource and Eriks Nukis
 * sonarqube@googlegroups.com
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.javascript.cpd;

import java.nio.charset.Charset;
import net.sourceforge.pmd.cpd.Tokenizer;
import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Language;
import org.sonar.plugins.javascript.JavaScriptLanguage;
import org.sonar.plugins.javascript.JavaScriptPlugin;

public class JavaScriptCpdMapping extends AbstractCpdMapping {

  private final JavaScriptLanguage language;
  private final Charset charset;
  private final boolean excludeMinified;

  public JavaScriptCpdMapping(JavaScriptLanguage language, FileSystem fs, Settings settings) {
    this.language = language;
    this.charset = fs.encoding();
    this.excludeMinified = settings.getBoolean(JavaScriptPlugin.EXCLUDE_MINIFIED_FILES);
  }

  @Override
  public Tokenizer getTokenizer() {
    return new JavaScriptTokenizer(charset, excludeMinified);
  }

  @Override
  public Language getLanguage() {
    return language;
  }

}
