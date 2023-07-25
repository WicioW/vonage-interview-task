package com.vonage.api.interview.search;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.scoring.FileScores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SearchHandlerImpl implements CommandHandlers.SearchHandler{

    private final Map<String, Path> indexDirectoryMap;

    public SearchHandlerImpl(Map<String, Path> indexDirectoryMap) {
        this.indexDirectoryMap = indexDirectoryMap;
    }

    @Override
    public FileScores search(String indexName, String searchString) {
        Path directory = indexDirectoryMap.get(indexName);
        if (directory == null) {
            //TODO delete after
            System.err.println("Index with name " + indexName + " does not exist.");
            return new FileScores(new HashMap<>());
        }

        TreeMap<String, Integer> orderedFileScores = new TreeMap<>(Collections.reverseOrder());

        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        int score = calculateScore(file, searchString);
                        orderedFileScores.put(file.toString(), score);
                    });
        } catch (Exception e) {
            System.err.println("Error searching the index: " + e.getMessage());
        }

        return new FileScores(orderedFileScores);
    }

    private int calculateScore(Path file, String searchString) {
        int totalWords = searchString.split("\\s+").length;
        int matches = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (searchString.contains(word)) {
                        matches++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return (int) ((double) matches / totalWords * 100);
    }
}
