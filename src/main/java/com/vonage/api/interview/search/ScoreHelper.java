package com.vonage.api.interview.search;

public class ScoreHelper {

    private static final String ERROR_ALL_WORDS_COUNT_INVALID = "allWordsCount must be greater than zero";
    private static final String ERROR_CANNOT_CALL_INCREMENT_METHOD = "Cannot call increment method. wordOccurrenceCount must be less than or equal to allWordsCount";

    private final int totalWordsCount;
    private int wordOccurrenceCount = 0;

    public ScoreHelper(int totalWordsCount) {
        if (totalWordsCount < 0) {
            throw new IllegalArgumentException(ERROR_ALL_WORDS_COUNT_INVALID);
        }
        this.totalWordsCount = totalWordsCount;
    }

    public void incrementWordOccurrenceCount() {
        if (wordOccurrenceCount >= totalWordsCount) {
            throw new IllegalStateException(ERROR_CANNOT_CALL_INCREMENT_METHOD);
        }
        wordOccurrenceCount++;
    }

    public int getScore() {
        if (totalWordsCount == 0) {
            return 0;
        }
        return (int) (((double) wordOccurrenceCount / totalWordsCount) * 100);
    }
}
