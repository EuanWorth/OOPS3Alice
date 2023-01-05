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

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AliceTest {

  @Test
  public void countWords_returns0_forEmptyList() {
    // ARRANGE
    List<Token> words = List.of();

    // ACT
    long count = Alice.countWords(words);

    // ASSERT
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void countWords_returns0_whenOnlyPunctuation() {
    // ARRANGE
    List<Token> words = List.of(new Token(".", ".", 1.0), new Token(",", ",", 1.0));

    // ACT
    long count = Alice.countWords(words);

    // ASSERT
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void countWords_returnCorrect_WhenWordsPresent() {
    //ARRANGE
    List<Token> words = List.of(new Token(".", ".", 1.0), new Token("Good","A",1), new Token("Cheese","N",1), new Token("Dog","N",1), new Token(",", ",", 1.0));

    // ACT
    long count = Alice.countWords(words);

    // ASSERT
    assertThat(count).isEqualTo(3);
  }

  @Test
  public void properNounsReturns0WhenNonePresent() {
    //ARRANGE
    List<Token> words = List.of(new Token(".", ".", 1.0), new Token("Good","A",1), new Token("Cheese","N",1), new Token("Dog","N",1), new Token(",", ",", 1.0));

    // ACT
    List<String> count = Alice.properNouns(words, 6);

    // ASSERT
    assertThat(count.toString()).isEqualTo("[]");
  }

  @Test
  public void properNounsReturnsInOrderWhenPresent() {
    //ARRANGE
    List<Token> words = List.of(new Token(".", ".", 1.0), new Token("Good","NNP",1), new Token("Cheese","NNP",1), new Token("Cheese","NNP",1), new Token("Dog","N",1), new Token(",", ",", 1.0));

    // ACT
    List<String> count = Alice.properNouns(words, 6);

    // ASSERT
    assertThat(count.toString()).isEqualTo("[Cheese, Good]");
  }
  @Test
  public void properNounsReturns0WhenNoTokensPresent() {
    //ARRANGE
    List<Token> words = List.of();

    // ACT
    List<String> count = Alice.properNouns(words, 6);

    // ASSERT
    assertThat(count.toString()).isEqualTo("[]");
  }

  @Test
  public void vocabularyReturns0WhenNoWordsPresent() {
    //ARRANGE
    List<Token> words = List.of(new Token(".", ".", 1.0), new Token(",", ",", 1.0));

    // ACT
    List<String> count = Alice.vocabulary(words, 6);

    // ASSERT
    assertThat(count.toString()).isEqualTo("[]");
  }

  @Test
  public void vocabularyReturns0WhenNoTokensPresent() {
    //ARRANGE
    List<Token> words = List.of();

    // ACT
    List<String> count = Alice.vocabulary(words, 6);

    // ASSERT
    assertThat(count.toString()).isEqualTo("[]");
  }

  @Test
  public void vocabulary_ignoresCase() {
    // ARRANGE
    List<Token> words =
        List.of(
            new Token("Alice", "NNP", 1.0),
            new Token("alice", "NNP", 1.0),
            new Token("Queen", "NNP", 1.0),
            new Token("King", "NNP", 1.0),
            new Token("King", "NNP", 1.0));

    // ACT
    List<String> vocab = Alice.vocabulary(words, 2);

    // ASSERT
    assertThat(vocab).containsExactly("alice", "king");
  }

  @Test
  public void vocabulary_returnsEnough() {
    // ARRANGE
    List<Token> words =
            List.of(
                    new Token("Alice", "NNP", 1.0),
                    new Token("alice", "NNP", 1.0),
                    new Token("Queen", "NNP", 1.0),
                    new Token("King", "NNP", 1.0),
                    new Token("King", "NNP", 1.0));

    // ACT
    List<String> vocab = Alice.vocabulary(words, 9);

    // ASSERT
    assertThat(vocab).containsExactly("alice", "king", "queen");
  }
  @Test
  public void topN_returnsTopOne() {
    // ARRANGE
    Map<String, Long> frequencies = Map.of("apple", 10L, "pear", 5L, "banana", 1L);

    // ACT
    List<String> top = Alice.topN(1, frequencies);

    // ASSERT
    assertThat(top).containsExactly("apple");
  }

  @Test
  public void topN_returnsAll_ifNotEnoughPresent() {
    // ARRANGE
    Map<String, Long> frequencies = Map.of("apple", 10L, "pear", 5L, "banana", 1L);

    // ACT
    List<String> top = Alice.topN(10, frequencies);

    // ASSERT
    assertThat(top).containsExactly("apple", "pear", "banana");
  }

  @Test
  public void leastConfidentToken_ReturnsNull_IfEmpty() {
    //ARRANGE
    List<Token> list = List.of();

    //ACT
    Token LeastConfident = Alice.leastConfidentToken(list);

    //ASSERT
    assertThat(LeastConfident).isNull();
  }

  @Test
  public void leastConfidentToken_Returns_IfNotEmpty() {
    //ARRANGE
    List<Token> list = List.of(
            new Token("Alice", "NNP", 1.0),
            new Token("alice", "NNP", 1.0),
            new Token("Queen", "NNP", 0.7),
            new Token("King", "NNP", 1.0),
            new Token("King", "NNP", 1.0));

    //ACT
    Token LeastConfident = Alice.leastConfidentToken(list);

    //ASSERT
    assertThat(LeastConfident.contents()).isEqualTo("Queen");
  }

  @Test
  public void PoSFrequenciesReturnsCorrectMapIfToekns() {
    //ARRANGE
    List<Token> tokens = List.of(
            new Token("Alice", "A", 1.0),
            new Token("alice", "A", 1.0),
            new Token("Queen", "Q", 1.0),
            new Token("King", "K", 1.0),
            new Token("King", "K", 1.0)
    );

    //ACT
    Map<String, Long> PoSFrequencyMap = Alice.posFrequencies(tokens);

    //ASSERT
    assertThat(PoSFrequencyMap).isEqualTo(Map.of("A",2L,"Q",1L,"K",2L));
  }

  @Test
  public void PoSFrequenciesReturnsEmptyMapIfNoTokens() {
    //ARRANGE
    List<Token> tokens = List.of();

    //ACT
    Map<String, Long> PoSFrequencyMap = Alice.posFrequencies(tokens);

    //ASSERT
    assertThat(PoSFrequencyMap).isEqualTo(Map.of());
  }

  // This test is not really useful but its here to make sure we get coverage of the Token class
  @Test
  public void tokenToString_returnsOneDecimalPlace() {
    // ARRANGE
    Token token = new Token("Alice", "NNP", 1.888);

    // ACT
    String string = token.toString();

    // ASSERT
    assertThat(string).isEqualTo(String.format("Alice(NNP:%.1f)", 1.9));
  }
}
