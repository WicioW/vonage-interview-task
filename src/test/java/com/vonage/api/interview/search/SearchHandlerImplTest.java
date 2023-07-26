package com.vonage.api.interview.search;

import com.vonage.api.interview.WordsExtractor;
import com.vonage.api.interview.scoring.FileScores;
import com.vonage.api.interview.util.StringValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchHandlerImplTest {

    @Nested
    public class SearchTest {
        private final PrintStream err = mock(PrintStream.class);
        private final StringValidator stringValidator = mock(StringValidator.class);
        private final WordsExtractor wordsExtractor = mock(WordsExtractor.class);
        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);

        private final SearchHandlerImpl testObj = new SearchHandlerImpl(
                err,
                new StringValidator(err),
                wordsExtractor,
                invertedIndexPerIndexNameMap);

        @Test
        void whenIndexNameIsEmpty_shouldReturnEmptyFileScore() {
            //given
            String indexName = "";
            String searchString = "1ngUXVa";
            when(stringValidator.isEmpty(indexName, "IndexName")).thenReturn(true);
            //when
            FileScores result = testObj.search(indexName, searchString);
            //then
            assertTrue(result.getFileScores().isEmpty());
        }

        @Test
        void whenSearchStringIsEmpty_shouldReturnEmptyFileScore() {
            //given
            String indexName = "Qy1zx";
            String searchString = "";
            when(stringValidator.isEmpty(searchString, "SearchString")).thenReturn(true);
            //when
            FileScores result = testObj.search(indexName, searchString);
            //then
            assertTrue(result.getFileScores().isEmpty());
        }

        @Test
        void whenInvertedIndexMapDoesNotHaveIndexName_shouldReturnEmptyFileScore() {
            //given
            String indexName = "Qy1zx";
            String searchString = "1ngUXVa";
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(null);
            //when
            FileScores result = testObj.search(indexName, searchString);
            //then
            assertTrue(result.getFileScores().isEmpty());
        }

        @Test
        void whenInvertedIndexMapIsEmpty_shouldReturnEmptyFileScore() {
            //given
            String indexName = "Qy1zx";
            String searchString = "1ngUXVa";
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(new InvertedIndexPerIndexName());
            when(wordsExtractor.extract(searchString)).thenReturn(new String[]{"1ngUXVa"});
            //when
            FileScores result = testObj.search(indexName, searchString);
            //then
            assertTrue(result.getFileScores().isEmpty());
        }

        @Test
        void searchMethod_shouldReturnOrderedDescendingMapByScore() {
            //given
            String indexName = "Qy1zx";
            String searchString = "1ngUXVa";
            when(wordsExtractor.extract(searchString)).thenReturn(new String[]{"1ngUXVa"});
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(mock(InvertedIndexPerIndexName.class));
            //when
            FileScores result = testObj.search(indexName, searchString);
            //then
            assertTrue(result.getFileScores() instanceof TreeMap);
            assertEquals(Collections.reverseOrder(), ((TreeMap) result.getFileScores()).comparator());
        }

        @Test
        void whenDifferentSearchStrings_shouldReturnDifferentScores() {
            //given
            String indexName = "Qy1zx";
            String searchString1 = "This is test.";
            String searchString2 = "This is my search string.";

            when(wordsExtractor.extract(searchString1)).thenReturn(new String[]{"This", "is", "test"});
            when(wordsExtractor.extract(searchString2)).thenReturn(new String[]{"This", "is", "my", "search", "string"});


            Path file1 = Path.of("file1");
            Path file2 = Path.of("file2");
            Path file3 = Path.of("file3");

            String word1 = "This";
            String word2 = "test";
            String word3 = "string";
            InvertedIndexPerIndexName invertedIndexPerIndexName = new InvertedIndexPerIndexName();
            invertedIndexPerIndexName.addWord(word1, file1);
            invertedIndexPerIndexName.addWord(word3, file1);

            invertedIndexPerIndexName.addWord(word1, file2);
            invertedIndexPerIndexName.addWord(word2, file2);
            invertedIndexPerIndexName.addWord(word3, file2);

            invertedIndexPerIndexName.addWord(word2, file3);

            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(invertedIndexPerIndexName);
            //when
            FileScores result1 = testObj.search(indexName, searchString1);
            FileScores result2 = testObj.search(indexName, searchString2);
            //then
            assertEquals(3, result1.getFileScores().size());
            //[This, is, test] and [This, string] overlapping words: This
            assertEquals(33, result1.getFileScores().get(file1.toString()));
            //[This, is, test] and [This, test, string] overlapping words: This, test
            assertEquals(66, result1.getFileScores().get(file2.toString()));
            //[This, is, test] and [test] overlapping words: test
            assertEquals(33, result1.getFileScores().get(file3.toString()));

            assertEquals(3, result2.getFileScores().size());
            //[This, is, my, search, string] and [This, string] overlapping words: This, string
            assertEquals(40, result2.getFileScores().get(file1.toString()));
            //[This, is, my, search, string] and [This, test, string] overlapping words: This, string
            assertEquals(40, result2.getFileScores().get(file2.toString()));
            //[This, is, my, search, string] and [test] overlapping words: []
            assertEquals(0, result2.getFileScores().get(file3.toString()));
        }

    }


    @Nested
    public class HelpfulTextTest {

        private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        private final PrintStream err = new PrintStream(errBuffer, true);
        private final WordsExtractor wordsExtractor = mock(WordsExtractor.class);
        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);

        private final SearchHandlerImpl testObj = new SearchHandlerImpl(
                err,
                new StringValidator(err),
                wordsExtractor,
                invertedIndexPerIndexNameMap);

        @AfterEach
        public void close() {
            err.close();
        }

        @Test
        void whenIndexedMapDoesNotHaveIndexName_shouldPrintError() {
            //given
            String indexName = "Qy1zx";
            String searchString = "1ngUXVa";
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(null);
            //when
            testObj.search(indexName, searchString);
            //then
            assertEquals("Index with name " + indexName + " does not exist." + System.lineSeparator(), errBuffer.toString());
        }

    }
}