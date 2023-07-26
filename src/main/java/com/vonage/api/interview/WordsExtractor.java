package com.vonage.api.interview;

public class WordsExtractor {

    public String[] extract(String text) {
        return text
                .trim()
                .toLowerCase()
                .replaceAll("-"," ")
                .replaceAll("'","")
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .split("\\s+");
    }

}
