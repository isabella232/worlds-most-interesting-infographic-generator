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
