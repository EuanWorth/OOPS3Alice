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

import java.util.*;
import java.util.stream.Collectors;

public class Alice {

  /** Return the number of tokens whose contents is a word. */
  static long countWords(List<Token> tokens) {
    return tokens.stream().filter(Token::isWord).count();
  }

  /**
   * Find the most frequent proper nouns in the text.
   *
   * @param tokens a list of the tokens in the text
   * @param size the number of proper nouns to return
   * @return a list of proper nouns
   */
  static List<String> properNouns(List<Token> tokens, int size) {
    Map<String, Long> nounCount =
            tokens.stream()
                    .filter(t -> t.partOfSpeech().equals("NNP"))
                    .collect(Collectors.groupingBy(Token::contents,Collectors.counting()));

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
    Map<String, Long> frequencyCount = tokens.stream()
            .filter(Token::isWord)
            .collect(
                    Collectors.groupingBy(t -> t.contents().toLowerCase(),Collectors.counting())
            );
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
    return frequencies.entrySet().stream()
            .sorted(Map.Entry.<T, Long>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList()).subList(0, Math.min(size, frequencies.size()));
  }

  /**
   * Find the token with the lowest confidence associated with it or null if no tokens were
   * provided.
   */
  static Token leastConfidentToken(List<Token> tokens) {
    return tokens.stream().min(Comparator.comparing(Token::confidence)).orElse(null);
  }

  /**
   * Find the frequencies of each part of speech tag in the text.
   *
   * @param tokens a list of tokens in the text
   * @return a map from part of speech tag to its frequency
   */
  static Map<String, Long> posFrequencies(List<Token> tokens) {
    return tokens.stream().collect(Collectors.groupingBy(Token::partOfSpeech,Collectors.counting()));
  }
}
