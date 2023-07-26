package com.vonage.api.interview.search;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.scoring.FileScores;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.*;

public class TestImpl implements CommandHandlers.SearchHandler{

    private final PrintStream err = System.err;
    private final PrintStream out = System.out;

    private final Map<String, Path> indexDirectoryMap;
    private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap;


    public TestImpl(Map<String, Path> indexDirectoryMap,
                    Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap) {
        this.indexDirectoryMap = indexDirectoryMap;
        this.invertedIndexPerIndexNameMap = invertedIndexPerIndexNameMap;
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
        String[] searchWords = searchString.split("\\s+");
        int searchStringWordCount = searchWords.length;

//        try {
//            Files.walk(directory)
//                    .filter(Files::isRegularFile)
//                    .forEach(file -> {
//                        int score = calculateScore(file, searchString);
//                        orderedFileScores.put(file.toString(), score);
//                    });
//        } catch (Exception e) {
//            System.err.println("Error searching the index: " + e.getMessage());
//        }


        //TODO
//        for (String searchWord : searchWords) {
//            Set<String> filesWithWord = invertedIndex.getOrDefault(searchWord, new HashSet<>());
//            for (String file : filesWithWord) {
//                int score = calculateScore(file, searchWords);
//                orderedFileScores.put(file, score);
//            }
//        }

        return new FileScores(orderedFileScores);
    }

    private int calculateScore(String file, String[] searchWords) {
        // Implement your scoring mechanism here
        // You can use any logic that fits your requirements to calculate the score
        // For simplicity, let's use a basic approach: score = number of matched words / total words in search string

        int totalWords = searchWords.length;
        int matches = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (Arrays.asList(searchWords).contains(word)) {
                        matches++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return (int) ((double) matches / totalWords * 100);
    }


//    private int calculateScore(Path file, String searchString) {
//        int totalWords = searchString.split("\\s+").length;
//        int matches = 0;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] words = line.split("\\s+");
//                for (String word : words) {
//                    if (searchString.contains(word)) {
//                        matches++;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error reading file: " + e.getMessage());
//        }
//
//        return (int) ((double) matches / totalWords * 100);
//    }
}
