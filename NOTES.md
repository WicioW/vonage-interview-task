# IMPORTANT
I had a problem with this sentence and what to do about it:
```
Your `SearchHandler` needs to implement the `FileScores search(String indexName, String searchString)` method and search the index,
providing an instance of `FileScores` that contains an **_in-order_** map of the files that contain the search string and a score; the highest score should be at the top of the ordered Map.
```
If I understand it correctly, we need to return `FileScores` object with ordered map, sorted by score.
But in `FileScores` class we have `Map<String, Integer> fileScores` and `Map<String, Integer> fileScores` cannot be ordered by value which is score.
I also cannot change anything in `FileScores` class. 

I was not sure if I should implement `NavigableMap` interface on my own and override `forEach` method that returns map sorted by value
or maybe this is a mistake and I just should use TreeMap. I assume I should override `forEach` method because it is used in line 78 in Application.class.
I decided to use TreeMap, but I am not sure if this is correct solution. In my solution I am sorting TreeMap in descending order to show that I know how to do it.
I am not sure if this is correct solution, but I decided to do it this way.

Right now if we use `search` command the output will not be sorted by score, but by filename. (line 78 in Application.class)

# !!! Considerations, Limitations, and Noteworthy Omissions are below. !!!

# Considerations

## Data Storage and Scalability:
I assumed that we will do more searches than indexing, so I optimized for search performance.
That is why I chose to use an inverted index, which is a data structure that is optimized for search.
The tradeoff is that indexing is slower.
Inverted index might not scale well for large datasets.
We could consider using a distributed storage system or a proper database for larger-scale applications to handle indexing efficiently.

## Scoring Mechanism:
The scoring mechanism is basic and could be enhanced.

## Tokenization:
The current implementation uses a simple tokenizer that splits the input string on whitespace and clears/replaces unwanted characters. 
Consider using a proper tokenizer library to handle language-specific tokenization and linguistic patterns.

## Search Mechanism:
The current implementation uses a simple tokenizer that splits the input string on whitespace and clears/replaces unwanted chars. We can consider using a proper tokenizer library.
It's important to note that the implementation might not handle special characters or complex linguistic patterns that could affect search results.
We can consider using more sophisticated search mechanisms to improve search accuracy and relevance.

## Special Character Handling:
The implementation might not handle special characters or complex linguistic patterns that could affect search results.
Consider additional processing or language-specific handling to improve search accuracy.

## Matching words and WordsExtractor.class
Currently, the implementation is removing char `'` from the words and replacing `-` with whitespace.
It works for easy cases like `haven't` and `in-between`, but it might not work for more complex cases.

In the future some better algorithm could be used to match words.
### Regarding apostrophes
For example:
```
haven't
wouldn't
couldn't
shouldn't
```
can be mapped to
```
have not
would not
could not
should not
```
BUT we do not know how this words should be mapped:
```
it's
we're
```
can be
```
it is/it has
we are/we were
```
### Regarding hyphens
```
in-between
```
can be mapped to
```
in between
```
BUT we do not know how this words should be mapped:
```
non-stop
```
can be
```
nonstop/non stop
```

and word `non` could be indexed and matched with other words, like `non existent` (first part)

# Limitations:
## Case-Insensitive Searching and Stemming:
The implementation does not support case-insensitive searching or stemming, limiting search flexibility and accuracy.

## Error Handling and Validation:
Error handling is limited in the current implementation. For production use, additional error handling and validation should be added to handle various scenarios gracefully.

## Persistence and Large-Scale Applications:
Same as in the considerations section, the inverted index may not be suitable for large-scale applications.
Consider using a proper database or distributed storage for efficient indexing and searching.

## Scoring Mechanism:
The current scoring mechanism is basic and may not fully represent the relevance of search results. Implementing more advanced scoring methods like TF-IDF could enhance search accuracy.

## Handling Stop Words and Special Characters:
The implementation does not account for stop words or special characters that might affect search results.

## File Format Handling:
The implementation assumes files being indexed are in text format.

# Noteworthy Omissions:

## Multilingual Support:
The implementation does not consider multilingual support. For a more comprehensive search solution, the program could be enhanced to handle various languages and linguistic patterns.