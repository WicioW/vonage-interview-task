package com.vonage.api.interview;

import com.vonage.api.interview.index.IndexHandlerImpl;
import com.vonage.api.interview.search.SearchHandlerImpl;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;
import com.vonage.api.interview.test.Testing;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OurWishIsYourCommands {

//    private static final Map<String, Path> indexDirectoryMap = new HashMap<>();
//    private final Map<String, Set<String>> invertedIndex = new HashMap<>();

//    private static final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = new HashMap<>();

    public static CommandHandlers createCommandHandlers() {
        final Map<String, Path> indexDirectoryMap = new HashMap<>();
        final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = new HashMap<>();
        // Note that you must not change the signature of this method, or the name of this class.
        // TODO: Instantiate your dependencies here, DO NOT use any kind of dependency injection frameworks
//        return new CommandHandlers(
//                (indexName, directory) -> {
//                },
//                (indexName, searchString) -> new FileScores(new HashMap<>(0))
//        );

        return new CommandHandlers(
                new IndexHandlerImpl(indexDirectoryMap, invertedIndexPerIndexNameMap),
                new Testing(invertedIndexPerIndexNameMap)
//                new SearchHandlerImpl(indexDirectoryMap, invertedIndexPerIndexNameMap)
        );
    }

}
