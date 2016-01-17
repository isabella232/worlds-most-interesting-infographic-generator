package com.worldsmostinterestinginfographic.statistics.result;

import com.whoischarles.util.json.Minify;

public class DailyPostFrequencyResult implements StatisticsResult, InfographicResult {

  private int[] postsByDayOfWeek;
  private String error;

  public DailyPostFrequencyResult(int[] postsByDayOfWeek) {
    if (postsByDayOfWeek == null) {
      throw new IllegalArgumentException();
    }

    this.postsByDayOfWeek = postsByDayOfWeek;
  }

  @Override
  public int[] getResult() {
    return postsByDayOfWeek;
  }

  @Override
  public String getError() {
    return error;
  }

  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"frequency\": [" +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[1] + "," +
                  "      \"highest\": " + postsByDayOfWeek[1] + "," +
                  "      \"month\": \"Mon\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[2] + "," +
                  "      \"highest\": " + postsByDayOfWeek[2] + "," +
                  "      \"month\": \"Tue\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[3] + "," +
                  "      \"highest\": " + postsByDayOfWeek[3] + "," +
                  "      \"month\": \"Wed\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[4] + "," +
                  "      \"highest\": " + postsByDayOfWeek[4] + "," +
                  "      \"month\": \"Thu\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[5] + "," +
                  "      \"highest\": " + postsByDayOfWeek[5] + "," +
                  "      \"month\": \"Fri\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[6] + "," +
                  "      \"highest\": " + postsByDayOfWeek[6] + "," +
                  "      \"month\": \"Sat\"" +
                  "    }," +
                  "    {" +
                  "      \"lowest\": " + postsByDayOfWeek[0] + "," +
                  "      \"highest\": " + postsByDayOfWeek[0] + "," +
                  "      \"month\": \"Sun\"" +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
