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

package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.StatisticsResult;
import com.worldsmostinterestinginfographic.statistics.result.TopWordsResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopWordsCollector implements StatisticsCollector {

  private static final int MIN_WORD_LENGTH = 4;
  private static final String WORD_FINDER_REGEX = "\\b[A-Za-z]+\\b";

  @Override
  public StatisticsResult collect(User user, List<Post> posts) {

    // Populate word-count map
    Map<String, Integer> wordsCountMap = new HashMap<String, Integer>();
    for (Post post : posts) {

      Pattern pattern = Pattern.compile(WORD_FINDER_REGEX, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(post.getMessage());

      while (matcher.find()) {

        String word = matcher.group();

        // Only accept words greater than a certain minimum length
        if (word.length() < MIN_WORD_LENGTH) {
          continue;
        }

        if (!wordsCountMap.containsKey(word)) {
          wordsCountMap.put(word, 1);
          continue;
        }

        wordsCountMap.put(word, wordsCountMap.get(word) + 1);
      }
    }

    // Convert word-count map from map to sorted list ordered by occurrence count (i.e. index 0 has word with most occurrences)
    List<Map.Entry<String, Integer>>  topWordsList = new LinkedList<Map.Entry<String, Integer>>(wordsCountMap.entrySet());
    Collections.sort(topWordsList, new Comparator<Map.Entry<String,Integer>>() {
      public int compare(Map.Entry<String, Integer> o1,
                         Map.Entry<String, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    // TODO: Figure out better way to handle 15 item limit for word cloud
    return new TopWordsResult(topWordsList.subList(0, 15));
  }
}
