package com.vonage.api.interview;

import com.vonage.api.interview.index.IndexHandlerImpl;
import com.vonage.api.interview.search.InvertedIndexPerIndexName;
import com.vonage.api.interview.search.SearchHandlerImpl;
import com.vonage.api.interview.util.StringValidator;

import java.util.HashMap;
import java.util.Map;

public class OurWishIsYourCommands {

    public static CommandHandlers createCommandHandlers() {
        // Note that you must not change the signature of this method, or the name of this class.
        // TODO: Instantiate your dependencies here, DO NOT use any kind of dependency injection frameworks
        final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = new HashMap<>();
        WordsExtractor wordsExtractor = new WordsExtractor();
        StringValidator stringValidator = new StringValidator(System.err);
        return new CommandHandlers(
                new IndexHandlerImpl(
                        System.err,
                        System.out,
                        new StringValidator(System.err),
                        wordsExtractor,
                        invertedIndexPerIndexNameMap),
                new SearchHandlerImpl(
                        System.err,
                        stringValidator,
                        wordsExtractor,
                        invertedIndexPerIndexNameMap));
    }

}
