package com.vonage.api.interview;


import com.vonage.api.interview.scoring.FileScores;

/* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

public class CommandHandlers {

    private final IndexHandler indexHandler;
    private final SearchHandler searchHandler;

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public CommandHandlers(IndexHandler indexHandler, SearchHandler searchHandler) {
        this.indexHandler = indexHandler;
        this.searchHandler = searchHandler;
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public IndexHandler indexer() {
        return this.indexHandler;
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public SearchHandler searcher() {
        return this.searchHandler;
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public interface IndexHandler {
        void index(String indexName, String directory);
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public interface SearchHandler {
        FileScores search(String indexName, String searchString);
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

}
