package com.vonage.api.interview.search;

public class ScoreHelper {

    private final int allWordsCount;
    private int wordOccurrenceCount = 0;

    public ScoreHelper(int allWordsCount) {
        if(allWordsCount < 0){
            throw new IllegalArgumentException("allWordsCount must be greater than zero");
        }
        this.allWordsCount = allWordsCount;
    }

    public void incrementWordOccurrenceCount() {
        int incrementedWordOccurrenceCount = wordOccurrenceCount + 1;
        if(incrementedWordOccurrenceCount > allWordsCount){
            throw new IllegalStateException("Cannot call increment method. wordOccurrenceCount must be less than or equal to allWordsCount");
        }
        wordOccurrenceCount = incrementedWordOccurrenceCount;
    }

    public int getScore() {
        if(allWordsCount == 0){
            return 0;
        }
        return (int) (((double) wordOccurrenceCount / allWordsCount) * 100);
    }
}
