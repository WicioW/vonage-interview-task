package com.vonage.api.interview.search;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.WordsExtractor;
import com.vonage.api.interview.scoring.FileScores;
import com.vonage.api.interview.util.StringValidator;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.*;


public class SearchHandlerImpl implements CommandHandlers.SearchHandler {

    private final PrintStream err;
    private final StringValidator stringValidator;
    private final WordsExtractor wordsExtractor;
    private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap;

    private final FileScores empty = new FileScores(new HashMap<>());

    public SearchHandlerImpl(
            PrintStream err,
            StringValidator stringValidator,
            WordsExtractor wordsExtractor, Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap) {
        this.err = err;
        this.stringValidator = stringValidator;
        this.wordsExtractor = wordsExtractor;
        this.invertedIndexPerIndexNameMap = invertedIndexPerIndexNameMap;
    }

    @Override
    public FileScores search(String indexName, String searchString) {
        if (stringValidator.isEmpty(indexName, "IndexName")) return empty;
        if (stringValidator.isEmpty(searchString, "SearchString")) return empty;

        InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
        if (invertedIndexPerIndexName == null) {
            err.println("Index with name " + indexName + " does not exist.");
            return empty;
        }

        String[] searchWords = wordsExtractor.extract(searchString);

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

        return new FileScores(orderedFileScores);
    }
}
