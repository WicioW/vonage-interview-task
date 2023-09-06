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
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.vonage.api.interview.io.FileTestUtils.createDir;
import static com.vonage.api.interview.io.FileTestUtils.createFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IndexHandlerImplTest {

    @Nested
    public class ConstructorTest{
        @Test
        public void testConstructorWithNullErr() {
            //given
            PrintStream out = mock(PrintStream.class);
            StringValidator stringValidator = mock(StringValidator.class);
            WordsExtractor wordsExtractor = mock(WordsExtractor.class);
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);
            //when-then
            assertThrows(IllegalArgumentException.class, () -> new IndexHandlerImpl(null, out, stringValidator, wordsExtractor, invertedIndexPerIndexNameMap));
        }

        @Test
        public void testConstructorWithNullOut() {
            //given
            PrintStream err = mock(PrintStream.class);
            StringValidator stringValidator = mock(StringValidator.class);
            WordsExtractor wordsExtractor = mock(WordsExtractor.class);
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);
            //when-then
            assertThrows(IllegalArgumentException.class, () -> new IndexHandlerImpl(err, null, stringValidator, wordsExtractor, invertedIndexPerIndexNameMap));
        }

        @Test
        public void testConstructorWithNullStringValidator() {
            //given
            PrintStream err = mock(PrintStream.class);
            PrintStream out = mock(PrintStream.class);
            WordsExtractor wordsExtractor = mock(WordsExtractor.class);
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);
            //when-then
            assertThrows(IllegalArgumentException.class, () -> new IndexHandlerImpl(err, out, null, wordsExtractor, invertedIndexPerIndexNameMap));
        }

        @Test
        public void testConstructorWithNullWordsExtractor() {
            //given
            PrintStream err = mock(PrintStream.class);
            PrintStream out = mock(PrintStream.class);
            StringValidator stringValidator = mock(StringValidator.class);
            Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);
            //when-then
            assertThrows(IllegalArgumentException.class, () -> new IndexHandlerImpl(err, out, stringValidator, null, invertedIndexPerIndexNameMap));
        }

        @Test
        public void testConstructorWithNullInvertedIndexPerIndexNameMap() {
            //given
            PrintStream err = mock(PrintStream.class);
            PrintStream out = mock(PrintStream.class);
            StringValidator stringValidator = mock(StringValidator.class);
            WordsExtractor wordsExtractor = mock(WordsExtractor.class);
            //when-then
            assertThrows(IllegalArgumentException.class, () -> new IndexHandlerImpl(err, out, stringValidator, wordsExtractor, null));
        }
    }

    @Nested
    public class IndexingTest {

        private final PrintStream err = mock(PrintStream.class);
        private final PrintStream out = mock(PrintStream.class);
        private final StringValidator stringValidator = mock(StringValidator.class);
        private final WordsExtractor wordsExtractor = mock(WordsExtractor.class);

        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = new HashMap<>();

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
            assertNull(invertedIndexPerIndexNameMap.get("indexName"));
        }

        @Test
        void whenDirectoryHasNoFiles_shouldNotIndexAnythingToMap(@TempDir File tmpDir) {
            //given
            String indexName = "Qy1zx";
            when(stringValidator.isEmpty(anyString())).thenReturn(false);
            //when
            testObj.index(indexName, tmpDir.getAbsolutePath());
            //then
            InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
            assertTrue(invertedIndexPerIndexName.getAllFiles().isEmpty());
            assertTrue(invertedIndexPerIndexName.getInvertedIndex().isEmpty());
        }

        @Test
        void whenDirectoryHasFilesButNoWords_shouldNotIndexAnythingToMap(@TempDir File tmpDir) throws IOException {
            //given
            String indexName = "Qy1zx";
            createFile(tmpDir, "file1", "");
            createFile(tmpDir, "file2", "");
            when(stringValidator.isEmpty(anyString())).thenReturn(false);

            //when
            testObj.index(indexName, tmpDir.getAbsolutePath());
            //then
            InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
            assertTrue(invertedIndexPerIndexName.getAllFiles().isEmpty());
            assertTrue(invertedIndexPerIndexName.getInvertedIndex().isEmpty());
        }

        @Test
        void whenDirectoryIsValid_shouldFillMapWithPreprocessedValues(@TempDir File tmpDir) throws IOException {
            //given
            String indexName = "Qy1zx";
            createFile(tmpDir, "file1", "hello meep");
            createFile(tmpDir, "file2", "hello moop");
            when(wordsExtractor.extract("hello meep")).thenReturn(new String[]{"hello", "meep"});
            when(wordsExtractor.extract("hello moop")).thenReturn(new String[]{"hello", "moop"});
            when(stringValidator.isEmpty(anyString())).thenReturn(false);
            //when
            testObj.index(indexName, tmpDir.getAbsolutePath());

            //then
            InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
            Path file1 = tmpDir.toPath().resolve("file1");
            Path file2 = tmpDir.toPath().resolve("file2");
            assertThat(invertedIndexPerIndexName.getAllFiles(),
                    containsInAnyOrder(
                            file1,
                            file2));
            assertThat(invertedIndexPerIndexName.getInvertedIndex()
                    .get("hello"),
                    containsInAnyOrder(
                            file1,
                            file2));
            assertThat(invertedIndexPerIndexName.getInvertedIndex()
                    .get("meep"),
                    containsInAnyOrder(
                            file1));
        }

        @Test
        void whenDirectoryHasSubdirectoriesWithFilesInside_shouldFillMapProperly(@TempDir File tmpDir) throws IOException {
            //given
            createFile(tmpDir, "file1", "hello test");
            File subdir = createDir(tmpDir, "subdir");
            createFile(subdir, "file2", "text hello");
            File subdir2 = createDir(subdir, "subdir2");
            createFile(subdir2, "file3", "hello test text");

            String indexName = "Qy1zx";
            when(stringValidator.isEmpty(anyString())).thenReturn(false);

            when(wordsExtractor.extract("hello test")).thenReturn(new String[]{"hello", "test"});
            when(wordsExtractor.extract("text hello")).thenReturn(new String[]{"text", "hello"});
            when(wordsExtractor.extract("hello test text")).thenReturn(new String[]{"hello", "test", "text"});

            //when
            testObj.index(indexName, tmpDir.getAbsolutePath());

            //then
            Path file1 = tmpDir.toPath().resolve("file1");
            Path file2 = tmpDir.toPath().resolve("subdir").resolve("file2");
            Path file3 = tmpDir.toPath().resolve("subdir").resolve("subdir2").resolve("file3");
            InvertedIndexPerIndexName invertedIndexPerIndexName = invertedIndexPerIndexNameMap.get(indexName);
            assertThat(invertedIndexPerIndexName.getAllFiles(),
                    containsInAnyOrder(
                            file1,
                            file2,
                            file3));
            assertThat(invertedIndexPerIndexName
                    .getInvertedIndex().get("hello"), containsInAnyOrder(file1, file2, file3));
            assertThat(invertedIndexPerIndexName
                    .getInvertedIndex().get("test"), containsInAnyOrder(file1, file3));
            assertThat(invertedIndexPerIndexName
                    .getInvertedIndex().get("text"), containsInAnyOrder(file2, file3));
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