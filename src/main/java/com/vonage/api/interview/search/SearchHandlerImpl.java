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

        Map<Path, ScoreHelper> scoreHelperMap = createScoreHelperMapForEveryFile(
                invertedIndexPerIndexName.getAllFiles(),
                searchWords.length);

        updateScoreHelperMapForEveryFoundWord(
                searchWords,
                scoreHelperMap,
                invertedIndexPerIndexName.getInvertedIndex()
        );

        TreeMap<String, Integer> orderedFileScores = mapScoreHelperMapToOrderedScoreMap(scoreHelperMap);
        return new FileScores(orderedFileScores);
    }

    private Map<Path, ScoreHelper> createScoreHelperMapForEveryFile(Set<Path> allFiles, int searchWordsCount) {
        Map<Path, ScoreHelper> scoreHelperMap = new HashMap<>(allFiles.size());

        allFiles.forEach(file ->
                        scoreHelperMap.put(file, new ScoreHelper(searchWordsCount)));
        return scoreHelperMap;
    }

    private void updateScoreHelperMapForEveryFoundWord(String[] searchWords, Map<Path, ScoreHelper> scoreHelperMap, Map<String, Set<Path>> invertedIndex) {
        Arrays.asList(searchWords)
                .forEach(searchWord ->
                        updateEachFileInScoreHelperMapByWord(
                                scoreHelperMap,
                                invertedIndex.getOrDefault(searchWord, new HashSet<>())));
    }

    private void updateEachFileInScoreHelperMapByWord(Map<Path, ScoreHelper> scoreHelperMap, Set<Path> filesWithWord) {
        filesWithWord.forEach(file -> scoreHelperMap.get(file).incrementWordOccurrenceCount());
    }

    private TreeMap<String, Integer> mapScoreHelperMapToOrderedScoreMap(Map<Path, ScoreHelper> scoreHelperMap) {
        TreeMap<String, Integer> orderedFileScores = new TreeMap<>(Collections.reverseOrder());
        scoreHelperMap.forEach((key, value) ->
                orderedFileScores.put(
                        key.toString(),
                        value.getScore()));
        return orderedFileScores;
    }
}
