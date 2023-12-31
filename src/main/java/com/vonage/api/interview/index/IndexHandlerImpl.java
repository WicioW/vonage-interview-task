package com.vonage.api.interview.index;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.WordsExtractor;
import com.vonage.api.interview.search.InvertedIndexPerIndexName;
import com.vonage.api.interview.util.StringValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

public class IndexHandlerImpl implements CommandHandlers.IndexHandler {

    private final PrintStream err;
    private final PrintStream out;
    private final StringValidator stringValidator;
    private final WordsExtractor wordsExtractor;
    private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap;

    public IndexHandlerImpl(
            PrintStream err,
            PrintStream out,
            StringValidator stringValidator,
            WordsExtractor wordsExtractor,
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap) {
        if (err == null || out == null || stringValidator == null || wordsExtractor == null || invertedIndexPerIndexNameMap == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }
        this.err = err;
        this.out = out;
        this.stringValidator = stringValidator;
        this.wordsExtractor = wordsExtractor;
        this.invertedIndexPerIndexNameMap = invertedIndexPerIndexNameMap;
    }

    @Override
    public void index(String indexName, String directory) {
        if (stringValidator.isEmpty(indexName) || stringValidator.isEmpty(directory)) {
            err.println("Error: indexName parameter or directory parameter is empty.");
            return;
        }

        Path path = Paths.get(directory);
        if (!isPathADirectory(path)) return;

        updateOrCreateInvertedIndexForIndexName(indexName);
        preprocessFilesAndBuildInvertedIndex(path, indexName);

        out.println("Indexed " + path + " with name " + indexName);
    }

    private void updateOrCreateInvertedIndexForIndexName(String indexName) {
        invertedIndexPerIndexNameMap.put(indexName, new InvertedIndexPerIndexName());
    }

    private void preprocessFilesAndBuildInvertedIndex(Path directory, String indexName) {
        try (Stream<Path> files = Files.walk(directory)) {
            files
                    .filter(Files::isRegularFile)
                    .forEach(file -> preprocessSingleFileAndAddToIndex(file, indexName));
        } catch (Exception e) {
            err.println("Error preprocessing files: " + e.getMessage());
        }
    }

    private void preprocessSingleFileAndAddToIndex(Path file, String indexName) {
        InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                addWordsFromLineToInvertedIndex(file, invertedIndexPerIndexName, line);
            }
        } catch (Exception e) {
            err.println("Error preprocessing file: " + e.getMessage());
        }
    }

    private void addWordsFromLineToInvertedIndex(
            Path file,
            InvertedIndexPerIndexName invertedIndexPerIndexName,
            String line) {
        String[] words = wordsExtractor.extract(line);
        for (String word : words) {
            invertedIndexPerIndexName.addWord(word, file);
        }
    }

    private boolean isPathADirectory(Path path) {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            err.println("Error: The specified directory does not exist.");
            return false;
        }
        return true;
    }

}
