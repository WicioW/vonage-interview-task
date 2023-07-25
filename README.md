# Vonage API Interview Excercise

Hey :D

Your task is to implement a basic command line indexer and searcher.

**Testing and code structure are the most important things we're looking for, and your code must compile**.

You **_MUST NOT_** change the interfaces of any of the provided classes, take note of all of the comments in the code.

The provided classes are there to help you and you should use them when appropriate;
they compile in Java 17 and can be run by either using the Gradle command
`./gradlew run -q --console=plain` or running the main class `com.vonage.api.interview.Application` within your IDE.

The commands implemented are `exit`, `index <index-name> <directory>`, and `search <index-name> <search-string>`, the provided `search` and `index` command implementations are NOOPs.

Implement the `OurWishIsYourCommands` class, providing an instance of `CommandHandlers` from the `createCommandHandlers` method.

An example creation method implementation might look like:
```java
    public static CommandHandlers createCommandHandlers() {
        return new CommandHandlers(new MyIndexHandler(), new MySearchHandler());
    }
```

Your `IndexHandler` needs to implement the `void index(String indexName, String directory)` method and index the provided directory.

Your `SearchHandler` needs to implement the `FileScores search(String indexName, String searchString)` method and search the index,
providing an instance of `FileScores` that contains an **_in-order_** map of the files that contain the search string and a score; the highest score should be at the top of the ordered Map.

The scoring mechanism is left for you to implement, but a file's score should be 100 if all the words in the search string are present in the file, 
0 if none of the words in the search string are present in the file, and somewhere in-between if only some words are present.

It's up to you to decide what a reasonable definition of a word 'match' is, e.g. if `haven't` is equal to `havent`, or `in between` is equal to `in-between`.

Once you've completed your implementation, create a `NOTES.md` file to list any considerations, limitations, or noteworthy omissions.

After running `/gradlew clean`, zip up the directory and send it back to us :)

Thank you for taking the time to complete the Vonage API Interview Excercise!
