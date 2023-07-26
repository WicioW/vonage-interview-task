package com.vonage.api.interview.search;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.scoring.FileScores;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;
import com.vonage.api.interview.test.ScoreHelper;
import com.vonage.api.interview.util.StringValidator;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.*;


public class SearchHandlerImpl implements CommandHandlers.SearchHandler{

    private final PrintStream err;
    private final StringValidator stringValidator;
//    private final PrintStream out = System.out;

    private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap;

    public SearchHandlerImpl(
            PrintStream err,
            StringValidator stringValidator,
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap) {
        this.err = err;
        this.stringValidator = stringValidator;
        this.invertedIndexPerIndexNameMap = invertedIndexPerIndexNameMap;
    }

    @Override
    public FileScores search(String indexName, String searchString) {
        if (stringValidator.isEmpty(indexName, "IndexName")) return null;
        if (stringValidator.isEmpty(searchString, "SearchString")) return null;

        InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
        if(invertedIndexPerIndexName == null) {
            err.println("Index with name " + indexName + " does not exist.");
            return new FileScores(new HashMap<>());
        }
        String[] searchWords = searchString.split("\\s+");

        //create empty score map, every file should have score 0
        Set<Path> allFiles = invertedIndexPerIndexName.getAllFiles();
        Map<Path, ScoreHelper> scoreHelperMap = new HashMap<>(allFiles.size());
        for (Path file : allFiles) {
            scoreHelperMap.put(file, new ScoreHelper(searchWords.length));
        }

        Map<String, Set<Path>> invertedIndex = invertedIndexPerIndexName.getInvertedIndex();

        for (String searchWord : searchWords) {
            Set<Path> filesWithWord = invertedIndex.getOrDefault(searchWord, new HashSet<>());
            for (Path file : filesWithWord) {
                ScoreHelper scoreHelper = scoreHelperMap.get(file);
                scoreHelper.incrementWordOccurrenceCount();
            }
        }

        //create and fill FileScore
        TreeMap<String, Integer> orderedFileScores = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Path, ScoreHelper> entry : scoreHelperMap.entrySet()) {
            orderedFileScores.put(
                    entry.getKey().toString(),
                    entry.getValue().getScore()
            );
        }

//        Path directory = indexDirectoryMap.get(indexName);
//        if (directory == null) {
//            System.err.println("Index with name " + indexName + " does not exist.");
//            return new FileScores(new HashMap<>());
//        }
//
//        String[] searchWords = searchString.split("\\s+");
//        int searchStringWordCount = searchWords.length;
//
//
////        try {
////            Files.walk(directory)
////                    .filter(Files::isRegularFile)
////                    .forEach(file -> {
////                        int score = calculateScore(file, searchString);
////                        orderedFileScores.put(file.toString(), score);
////                    });
////        } catch (Exception e) {
////            System.err.println("Error searching the index: " + e.getMessage());
////        }
//
//
//
//                TreeMap<String, Integer> orderedFileScores = new TreeMap<>(Collections.reverseOrder());
//        for (String searchWord : searchWords) {
//            Set<String> filesWithWord = invertedIndex.getOrDefault(searchWord, new HashSet<>());
//            for (String file : filesWithWord) {
//                int score = calculateScore(file, searchWords);
//                orderedFileScores.put(file, score);
//            }
//        }

        return new FileScores(orderedFileScores);
    }
}
