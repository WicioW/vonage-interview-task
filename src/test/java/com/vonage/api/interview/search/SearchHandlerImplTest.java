package com.vonage.api.interview.search;

import com.vonage.api.interview.index.IndexHandlerImpl;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;
import com.vonage.api.interview.util.StringValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchHandlerImplTest {


    @Nested
    public class HelpfulTextTest {

        private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        private final PrintStream err = new PrintStream(errBuffer, true);

        private final Map<String, InvertedIndexPerIndexName> invertedIndexPerIndexNameMap = mock(Map.class);

        private final SearchHandlerImpl testObj = new SearchHandlerImpl(
                err,
                new StringValidator(err),
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
            assertEquals("Index with name " + indexName +" does not exist." + System.lineSeparator(), errBuffer.toString());
        }

    }
}