package com.vonage.api.interview.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreHelperTest {

    @Test
    void getScore_shouldReturnZero_whenProvidedAllWordsCountWasZero() {
        //given
        ScoreHelper testObj = new ScoreHelper(0);
        //when
        int score = testObj.getScore();
        //then
        assertEquals(0, score);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenProvidedAllWordsCountWasNegative() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> new ScoreHelper(-1));
    }

    @Test
    void getScore_shouldReturnOneHundred_whenProvidedAllWordsCountWasThreeAndIncrementWasCalledThreeTimes() {
        //given
        ScoreHelper testObj = new ScoreHelper(3);
        testObj.incrementWordOccurrenceCount();
        testObj.incrementWordOccurrenceCount();
        testObj.incrementWordOccurrenceCount();
        //when
        int score = testObj.getScore();
        //then
        assertEquals(100, score);
    }

    @Test
    void shouldThrowIllegalStateException_whenCallingIncrementMoreTimesThanProvidedAllWordsCount() {
        //given
        ScoreHelper testObj = new ScoreHelper(3);
        testObj.incrementWordOccurrenceCount();
        testObj.incrementWordOccurrenceCount();
        testObj.incrementWordOccurrenceCount();
        //when
        //then
        assertThrows(IllegalStateException.class, testObj::incrementWordOccurrenceCount);
    }

    @Test
    void getScore_shouldReturn69_whenProvidedAllWordsCountWas13AndIncrementWasCalled9Times() {
        //given
        ScoreHelper testObj = new ScoreHelper(13);
        for (int i = 0; i < 9; i++) {
            testObj.incrementWordOccurrenceCount();
        }
        //when
        int score = testObj.getScore();
        //then
        assertEquals(69, score);
    }
}