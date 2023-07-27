package com.vonage.api.interview.index;

import com.vonage.api.interview.WordsExtractor;
import com.vonage.api.interview.search.InvertedIndexPerIndexName;
import com.vonage.api.interview.util.StringValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import static com.vonage.api.interview.io.FileTestUtils.createFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IndexHandlerImplTest {

    @Nested
    public class IndexingTest {

        private final PrintStream err = mock(PrintStream.class);
        private final PrintStream out = mock(PrintStream.class);
        private final StringValidator stringValidator = mock(StringValidator.class);
        private final WordsExtractor wordsExtractor = mock(WordsExtractor.class);
        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);

        private final IndexHandlerImpl testObj = new IndexHandlerImpl(
                err,
                out,
                stringValidator,
                wordsExtractor,
                invertedIndexPerIndexNameMap);

        @Test
        void whenIndexNameIsEmpty_shouldNotIndexAnythingToMap() {
            //given
            String indexName = "";
            when(stringValidator.isEmpty(anyString())).thenReturn(true);
            //when
            testObj.index(indexName, "directory");
            //then
            verify(invertedIndexPerIndexNameMap, never()).put(anyString(), any());
            assertNull(invertedIndexPerIndexNameMap.get(indexName));
            verify(err).println("Error: indexName parameter or directory parameter is empty.");
        }

        @Test
        void whenDirectoryParameterIsEmpty_shouldNotIndexAnythingToMap() {
            //given
            String directory = "";
            when(stringValidator.isEmpty(anyString())).thenReturn(true);
            //when
            testObj.index("indexName", directory);
            //then
            verify(invertedIndexPerIndexNameMap, never()).put(anyString(), any());
            assertNull(invertedIndexPerIndexNameMap.get("indexName"));
            verify(err).println("Error: indexName parameter or directory parameter is empty.");
        }

        @Test
        void whenDirectoryParameterIsNotValid_shouldNotIndexAnythingToMap() {
            //given
            String directory = "certainly_not/directory";
            when(stringValidator.isEmpty(anyString())).thenReturn(false);
            //when
            testObj.index("indexName", directory);
            //then
            verify(invertedIndexPerIndexNameMap, never()).put(anyString(), any());
            assertNull(invertedIndexPerIndexNameMap.get("indexName"));
        }

        @Test
        void whenDirectoryIsValid_shouldFillMapWithPreprocessedValues(@TempDir File tmpDir) throws IOException {
            //given
            String indexName = "Qy1zx";
            createFile(tmpDir, "file1", "hello meep");
            createFile(tmpDir, "file2", "hello moop");
            when(wordsExtractor.extract("hello meep")).thenReturn(new String[]{"hello", "meep"});
            when(wordsExtractor.extract("hello moop")).thenReturn(new String[]{"hello", "moop"});
            InvertedIndexPerIndexName invertedIndexPerIndexName = new InvertedIndexPerIndexName();
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(invertedIndexPerIndexName);
            when(stringValidator.isEmpty(anyString())).thenReturn(false);
            //when
            testObj.index(indexName, tmpDir.getAbsolutePath());

            //then
            verify(invertedIndexPerIndexNameMap).put(eq("Qy1zx"), any(InvertedIndexPerIndexName.class));
            assertThat(invertedIndexPerIndexName.getAllFiles(), containsInAnyOrder(tmpDir.toPath().resolve("file1"), tmpDir.toPath().resolve("file2")));
            assertThat(invertedIndexPerIndexName.getInvertedIndex().get("hello"), containsInAnyOrder(tmpDir.toPath().resolve("file1"), tmpDir.toPath().resolve("file2")));
            assertThat(invertedIndexPerIndexName.getInvertedIndex().get("meep"), containsInAnyOrder(tmpDir.toPath().resolve("file1")));
        }

    }


    @Nested
    public class HelpfulTextTest {

        private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        private final PrintStream out = new PrintStream(outBuffer, true);

        private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        private final PrintStream err = new PrintStream(errBuffer, true);

        private final WordsExtractor wordsExtractor = mock(WordsExtractor.class);
        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);

        private final IndexHandlerImpl testObj = new IndexHandlerImpl(
                err,
                out,
                new StringValidator(),
                wordsExtractor,
                invertedIndexPerIndexNameMap);

        @AfterEach
        public void close() {
            err.close();
            out.close();
        }

        @Test
        void whenIndexingIsFinished_shouldPrintInfoText(@TempDir File tmpDir) {
            //given
            String indexName = "Qy1zx";
            InvertedIndexPerIndexName invertedIndexPerIndexName = new InvertedIndexPerIndexName();
            when(invertedIndexPerIndexNameMap.get(indexName)).thenReturn(invertedIndexPerIndexName);
            String directoryPathString = tmpDir.getAbsolutePath();
            //when
            testObj.index(indexName, directoryPathString);
            //then
            assertEquals("Indexed " + directoryPathString + " with name " + indexName + System.lineSeparator(), outBuffer.toString());
        }

    }
}