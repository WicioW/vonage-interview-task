package com.vonage.api.interview.test;

public class ScoreHelper {

    private final int allWordsCount;
    private int wordOccurrenceCount = 0;

    public ScoreHelper(int allWordsCount) {
        this.allWordsCount = allWordsCount;
    }

    public void incrementWordOccurrenceCount() {
        wordOccurrenceCount++;
    }

    public int getScore() {
        return (int) ((double) wordOccurrenceCount / allWordsCount * 100);
    }
}
