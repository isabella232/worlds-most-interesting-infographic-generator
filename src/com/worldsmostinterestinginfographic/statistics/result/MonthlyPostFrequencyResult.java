/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worldsmostinterestinginfographic.statistics.result;

import com.whoischarles.util.json.Minify;

public class MonthlyPostFrequencyResult implements StatisticsResult, InfographicResult {

  private int[] postsByMonthOfYear;
  private String error;

  public MonthlyPostFrequencyResult(int[] postsByMonthOfYear) {
    if (postsByMonthOfYear == null) {
      throw new IllegalArgumentException();
    }

    this.postsByMonthOfYear = postsByMonthOfYear;
  }

  public MonthlyPostFrequencyResult(String error) {
    this.error = error;
  }

  @Override
  public int[] getResult() {
    return postsByMonthOfYear;
  }

  @Override
  public String getError() {
    return error;
  }

  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"private\": [" +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[0] + "," +
                  "      \"percent\": 2" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[1] + "," +
                  "      \"percent\": 11" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[2] + "," +
                  "      \"percent\": 20" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[3] + "," +
                  "      \"percent\": 30" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[4] + "," +
                  "      \"percent\": 51" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[5] + "," +
                  "      \"percent\": 57" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[6] + "," +
                  "      \"percent\": 70" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[7] + "," +
                  "      \"percent\": 75" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[8] + "," +
                  "      \"percent\": 88" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[9] + "," +
                  "      \"percent\": 100" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[10] + "," +
                  "      \"percent\": 110" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[11] + "," +
                  "      \"percent\": 120" +
                  "    }" +
                  "  ]," +
                  "  \"color\": \"#3a5897\"" +
                  "}";

    return new Minify().minify(json);
  }
}
