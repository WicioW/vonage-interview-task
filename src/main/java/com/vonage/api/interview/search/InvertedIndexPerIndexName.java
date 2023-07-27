package com.vonage.api.interview.search;

import java.nio.file.Path;
import java.util.*;

public class InvertedIndexPerIndexName {

    private final Set<Path> allFiles = new HashSet<>();
    private final Map<String, Set<Path>> invertedIndex = new HashMap<>();

    public void addWord(String word, Path file) {
        allFiles.add(file);
        invertedIndex
                .computeIfAbsent(word, k -> new HashSet<>())
                .add(file);
    }

    public Set<Path> getAllFiles() {
        return Collections.unmodifiableSet(allFiles);
    }

    public Map<String, Set<Path>> getInvertedIndex() {
        return Collections.unmodifiableMap(invertedIndex);
    }
}
