package com.vonage.api.interview;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WordsExtractorTest {

    private final WordsExtractor testObj = new WordsExtractor();

    @Test
    void shouldExtractWordsWithoutPunctuation() {
        String searchString = "test.";
        String[] expected = {"test"};
        String[] actual = testObj.extract(searchString);
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldExtractLowerCaseWords(){
        String searchString = "TEST";
        String[] expected = {"test"};
        String[] actual = testObj.extract(searchString);
        assertArrayEquals(expected, actual);
    }

    @Test
    void extractWords() {
        String searchString = " in-between haven't test.";
        String[] expected = {"in", "between", "havent", "test"};
        String[] actual = testObj.extract(searchString);
        System.out.println(Arrays.toString(actual));
        assertArrayEquals(expected, actual);
    }
}