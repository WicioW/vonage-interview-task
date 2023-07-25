package com.vonage.api.interview.index;

import com.vonage.api.interview.CommandHandlers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class IndexHandlerImpl implements CommandHandlers.IndexHandler {

    private final Map<String, Path> indexDirectoryMap;

    public IndexHandlerImpl(Map<String, Path> indexDirectoryMap) {
        this.indexDirectoryMap = indexDirectoryMap;
    }

    @Override
    public void index(String indexName, String directory) {
        Path path = Paths.get(directory);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            System.err.println("Error: The specified directory does not exist.");
            return;
        }

        indexDirectoryMap.put(indexName, path);
        //TODO delete after
        System.out.println("Indexed " + path + " with name " + indexName);
    }
}
