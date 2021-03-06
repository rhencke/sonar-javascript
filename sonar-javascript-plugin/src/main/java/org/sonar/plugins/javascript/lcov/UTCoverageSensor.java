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
package org.sonar.plugins.javascript.lcov;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.javascript.JavaScriptPlugin;

public class UTCoverageSensor extends LCOVCoverageSensor {

  public UTCoverageSensor(FileSystem fileSystem, Settings settings) {
    super(fileSystem, settings);
    linesToCoverMetric = CoreMetrics.LINES_TO_COVER;
    uncoveredLinesMetric = CoreMetrics.UNCOVERED_LINES;
    coverageLineHitsDataMetric = CoreMetrics.COVERAGE_LINE_HITS_DATA;
    coveredConditionsByLineMetric = CoreMetrics.COVERED_CONDITIONS_BY_LINE;
    conditionsByLineMetric = CoreMetrics.CONDITIONS_BY_LINE;
    uncoveredConditionsMetric = CoreMetrics.UNCOVERED_CONDITIONS;
    conditionsToCoverMetric = CoreMetrics.CONDITIONS_TO_COVER;
    reportPaths = new String[]{ JavaScriptPlugin.LCOV_UT_REPORT_PATH };
  }

}
