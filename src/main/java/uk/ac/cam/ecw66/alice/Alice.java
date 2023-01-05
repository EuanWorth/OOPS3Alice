/*
 * Copyright 2023 Andrew Rice <acr31@cam.ac.uk>, E.C. Worth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.cam.ecw66.alice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Alice {

  /** Return the number of tokens whose contents is a word. */
  static long countWords(List<Token> tokens) {
    long counter = 0;
    for (Token t : tokens) {
      if (!t.isWord()) {
        continue;
      }
      counter++;
    }
    return counter;
  }

  /**
   * Find the most frequent proper nouns in the text.
   *
   * @param tokens a list of the tokens in the text
   * @param size the number of proper nouns to return
   * @return a list of proper nouns
   */
  static List<String> properNouns(List<Token> tokens, int size) {
    Map<String, Long> nounCount = new HashMap<>();
    for (Token t : tokens) {
      if (!t.partOfSpeech().equals("NNP")) {
        continue;
      }
      String word = t.contents();
      nounCount.put(word, nounCount.getOrDefault(word, 0L) + 1);
    }
    return topN(size, nounCount);
  }

  /**
   * Return the most frequent words in the text.
   *
   * @param tokens a list of the tokens in the text
   * @param size the number of words to return
   * @return a list of words
   */
  static List<String> vocabulary(List<Token> tokens, int size) {
    Map<String, Long> frequencyCount = new HashMap<>();
    for (Token t : tokens) {
      if (!t.isWord()) {
        continue;
      }
      String word = t.contents().toLowerCase();
      frequencyCount.put(word, frequencyCount.getOrDefault(word, 0L) + 1);
    }
    return topN(size, frequencyCount);
  }

  /**
   * Takes a map of items to their frequency and returns the most frequent items.
   *
   * @param size the number of items to return
   * @param frequencies a map of item to its frequency
   * @param <T> the type of the item (e.g. a String)
   * @return a list of the most frequent items
   */
  static <T> List<T> topN(int size, Map<T, Long> frequencies) {
    List<Map.Entry<T, Long>> items = new ArrayList<>(frequencies.entrySet());
    items.sort(Map.Entry.<T, Long>comparingByValue().reversed());
    Iterator<Map.Entry<T, Long>> iterator = items.iterator();
    List<T> result = new ArrayList<>();
    while (size-- > 0 && iterator.hasNext()) {
      result.add(iterator.next().getKey());
    }
    return result;
  }

  /**
   * Find the token with the lowest confidence associated with it or null if no tokens were
   * provided.
   */
  static Token leastConfidentToken(List<Token> tokens) {
    Token min = null;
    for (Token t : tokens) {
      if (min == null || t.confidence() < min.confidence()) {
        min = t;
      }
    }
    return min;
  }

  /**
   * Find the frequencies of each part of speech tag in the text.
   *
   * @param tokens a list of tokens in the text
   * @return a map from part of speech tag to its frequency
   */
  static Map<String, Long> posFrequencies(List<Token> tokens) {
    Map<String, Long> freq = new HashMap<>();
    for (Token t : tokens) {
      String pos = t.partOfSpeech();
      freq.put(pos, freq.getOrDefault(pos, 0L) + 1);
    }
    return freq;
  }
}