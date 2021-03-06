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
package org.sonar.javascript.tree.impl.declaration;

import java.util.List;
import org.junit.Test;
import org.sonar.javascript.utils.JavaScriptTreeModelTest;
import org.sonar.plugins.javascript.api.tree.Tree.Kind;
import org.sonar.plugins.javascript.api.tree.declaration.ParameterListTree;
import org.sonar.plugins.javascript.api.tree.expression.IdentifierTree;

import static org.fest.assertions.Assertions.assertThat;

public class FormalParameterListTreeModelTest extends JavaScriptTreeModelTest {


  @Test
  public void parameters() throws Exception {
    ParameterListTree tree = parse("function f(p1, p2, ...p3) {};", Kind.FORMAL_PARAMETER_LIST);

    assertThat(tree.is(Kind.FORMAL_PARAMETER_LIST)).isTrue();
    assertThat(tree.openParenthesis().text()).isEqualTo("(");

    assertThat(tree.parameters().size()).isEqualTo(3);
    assertThat(expressionToString(tree.parameters().get(0))).isEqualTo("p1");
    assertThat(expressionToString(tree.parameters().get(1))).isEqualTo("p2");
    assertThat(expressionToString(tree.parameters().get(2))).isEqualTo("...p3");

    assertThat(tree.parameters().getSeparators().size()).isEqualTo(2);
    assertThat(tree.closeParenthesis().text()).isEqualTo(")");
  }


  @Test
  public void no_parameter() throws Exception {
    ParameterListTree tree = parse("function f() {};", Kind.FORMAL_PARAMETER_LIST);

    assertThat(tree.is(Kind.FORMAL_PARAMETER_LIST)).isTrue();
    assertThat(tree.openParenthesis().text()).isEqualTo("(");

    assertThat(tree.parameters().size()).isEqualTo(0);
    assertThat(tree.parameters().getSeparators().size()).isEqualTo(0);

    assertThat(tree.closeParenthesis().text()).isEqualTo(")");
  }

  @Test
  public void parametersIdentifiers() throws Exception {
    ParameterListTree tree = parse("function f(p1, p2 = 0, { name:p3 }, [,,p4], ...p5) {};", Kind.FORMAL_PARAMETER_LIST);

    List<IdentifierTree> parameters = ((ParameterListTreeImpl) tree).parameterIdentifiers();
    assertThat(parameters.size()).isEqualTo(5);
    assertThat(parameters.get(0).name()).isEqualTo("p1");
    assertThat(parameters.get(1).name()).isEqualTo("p2");
    assertThat(parameters.get(2).name()).isEqualTo("p3");
    assertThat(parameters.get(3).name()).isEqualTo("p4");
    assertThat(parameters.get(4).name()).isEqualTo("p5");
  }

  @Test(expected = IllegalStateException.class)
  public void actual_parameters_identifiers() throws Exception {
    ParameterListTree tree = parse("foo(p1, 1 + 1)", Kind.ARGUMENTS);
    ((ParameterListTreeImpl) tree).parameterIdentifiers();
  }
}
