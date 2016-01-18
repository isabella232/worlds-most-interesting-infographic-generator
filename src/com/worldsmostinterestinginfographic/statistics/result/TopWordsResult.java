package com.worldsmostinterestinginfographic.statistics.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TopWordsResult implements StatisticsResult, InfographicResult {

  private List<Map.Entry<String, Integer>> topWords;
  private String error;

  public TopWordsResult(List<Map.Entry<String, Integer>> topWords) {
    if (topWords == null) {
      throw new IllegalArgumentException();
    }

    this.topWords = topWords;
  }

  public TopWordsResult(String error) {
    this.error = error;
  }

  @Override
  public List<Map.Entry<String, Integer>> getResult() {
    return topWords;
  }

  @Override
  public String getError() {
    return error;
  }

  @Override
  public String getInfographicJson() {

    List<String> wordsHtml = new ArrayList<>(topWords.size());
    int emphasis = 5;
    int previousCount = topWords.get(0).getValue();
    for (int i = 0; i < topWords.size(); i++) {

      if (emphasis > 0 && topWords.get(i).getValue() < previousCount) {
        emphasis--;
      }

      wordsHtml.add("<li class=\\\"" + giveMeVees(emphasis) + (emphasis > 0 ? "-" : "") + "popular\\\"><a href=\\\"#\\\"/>" + topWords.get(i).getKey() + "</a></li>");
    }

    Collections.shuffle(wordsHtml);

    String html = "";
    for (String wordHtml : wordsHtml) {
      html += wordHtml;
    }

    String json = "{" +
                  "	\"html\": \"" + html + "\"," +
                  "	\"topword\": \"" + topWords.get(0).getKey() + "\"" +
                  "}";

    return json;
  }

  private String giveMeVees(int numVees) {
    String vees = "";
    for (int i = 0; i < numVees; i++) {
      vees += "v";
    }

    return vees;
  }
}
