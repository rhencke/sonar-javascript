/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2015 SonarSource
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
package org.sonar.samples.javascript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;

public class TypeStatistics implements PostJob {

  private static final Logger LOG = LoggerFactory.getLogger(TypeStatistics.class);
  private static int totalSymbol = 0;
  private static int totalTypes = 0;
  private static int totalUnknownType = 0;
  private static int totalSymbolsWithType = 0;

  public static void increaseSymbol(int numberOfTypes) {
    totalSymbol++;
    totalTypes += numberOfTypes;

    if (numberOfTypes > 0) {
      totalSymbolsWithType++;
    }
  }

  @Override
  public void executeOn(Project project, SensorContext sensorContext) {
    LOG.info("");
    LOG.info("============  SYMBOL TYPE STATISTICS  =============");
    LOG.info("\t\t Total Symbols:.........................{}", totalSymbol);
    LOG.info("");
    LOG.info("\t\t Total Symbol with Types:...............{}", totalSymbolsWithType);
    LOG.info("\t\t Symbol with Types:.....................{} %", Math.round((((double) totalSymbolsWithType) / totalSymbol) * 100));
    LOG.info("\t\t\t Total Symbol without UNKNOWN:........{}", totalSymbolsWithType - totalUnknownType);
    LOG.info("\t\t\t Total UNKNOWN:.......................{}", totalUnknownType);
    LOG.info("");
    LOG.info("\t\t Average number of types:...............{}", (double) totalTypes / totalSymbolsWithType);
    LOG.info("");
  }

  public static void increaseUnknown() {
    totalUnknownType++;
  }
}
