package com.vonage.api.interview;

import com.vonage.api.interview.index.IndexHandlerImpl;
import com.vonage.api.interview.scoring.FileScores;
import com.vonage.api.interview.search.SearchHandlerImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OurWishIsYourCommands {

    private static final Map<String, Path> indexDirectoryMap = new HashMap<>();

    public static CommandHandlers createCommandHandlers() {
        // Note that you must not change the signature of this method, or the name of this class.
        // TODO: Instantiate your dependencies here, DO NOT use any kind of dependency injection frameworks
//        return new CommandHandlers(
//                (indexName, directory) -> {
//                },
//                (indexName, searchString) -> new FileScores(new HashMap<>(0))
//        );

        return new CommandHandlers(
                new IndexHandlerImpl(indexDirectoryMap),
                new SearchHandlerImpl(indexDirectoryMap)
        );
    }

}
