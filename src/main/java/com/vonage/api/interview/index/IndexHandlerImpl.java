package com.vonage.api.interview.index;

import com.vonage.api.interview.CommandHandlers;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;
import com.vonage.api.interview.util.StringValidator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class IndexHandlerImpl implements CommandHandlers.IndexHandler {

    private final PrintStream err;
    private final PrintStream out;
    private final StringValidator stringValidator;

    //    private final Map<String, Path> indexDirectoryMap;
    private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap;

    public IndexHandlerImpl(
            PrintStream err,
            PrintStream out,
            StringValidator stringValidator,
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap) {
        this.err = err;
        this.out = out;
        this.stringValidator = stringValidator;
        this.invertedIndexPerIndexNameMap = invertedIndexPerIndexNameMap;
    }

    @Override
    public void index(String indexName, String directory) {
        if (!stringValidator.validateIsNotEmpty(indexName, "IndexName")) return;
        if (!stringValidator.validateIsNotEmpty(directory, "Directory")) return;

        Path path = Paths.get(directory);
        if (!isPathADirectory(path)) return;

//        indexDirectoryMap.put(indexName, path);
        invertedIndexPerIndexNameMap.put(indexName, new InvertedIndexPerIndexName());


        preprocessFiles(path, indexName);
        out.println("Indexed " + path + " with name " + indexName);
    }

    private void preprocessFiles(Path directory, String indexName) {
        try {
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(f -> preprocessFile(f, indexName));
        } catch (Exception e) {
            System.err.println("Error preprocessing files: " + e.getMessage());
        }
    }

    private void preprocessFile(Path file, String indexName) {
        InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    invertedIndexPerIndexName.addFile(file);
                    invertedIndexPerIndexName.addWord(word, file);
                }
            }
        } catch (Exception e) {
            System.err.println("Error preprocessing file: " + e.getMessage());
        }
    }

    private boolean isPathADirectory(Path path) {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            err.println("Error: The specified directory does not exist.");
            return false;
        }
        return true;
    }

//    private boolean validateDirectoryParameter(String directory) {
//        return validateStringParameter(directory, "Directory");
//    }
//
//    private boolean validateIndexNameParameter(String indexName) {
//        return validateStringParameter(indexName, "Index name");
//    }
//
//    private boolean validateStringParameter(String string, String valueName) {
//        if (string.isEmpty()) {
//            err.println("Error: " + valueName + " cannot be empty.");
//            return false;
//        }
//        return true;
//    }
}
