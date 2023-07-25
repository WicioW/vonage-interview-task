package com.vonage.api.interview;

import com.vonage.api.interview.scoring.FileScores;

import java.util.HashMap;

public class OurWishIsYourCommands {

    public static CommandHandlers createCommandHandlers() {
        // Note that you must not change the signature of this method, or the name of this class.
        // TODO: Instantiate your dependencies here, DO NOT use any kind of dependency injection frameworks
        return new CommandHandlers(
                (indexName, directory) -> {
                },
                (indexName, searchString) -> new FileScores(new HashMap<>(0))
        );
    }

}
