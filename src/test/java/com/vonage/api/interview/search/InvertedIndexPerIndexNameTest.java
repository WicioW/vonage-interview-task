package com.vonage.api.interview.search;

import com.vonage.api.interview.search.InvertedIndexPerIndexName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InvertedIndexPerIndexNameTest {

    @Test
    void initializedObject_shouldHaveNonNullFields(){
        //given
        InvertedIndexPerIndexName testObj = new InvertedIndexPerIndexName();
        //then
        assertNotNull(testObj.getAllFiles());
        assertNotNull(testObj.getInvertedIndex());
    }

    @Test
    void addWord_shouldAddFileToAllFilesSetAndWordWithFileToMap(){
        //given
        InvertedIndexPerIndexName testObj = new InvertedIndexPerIndexName();

        //when
        assertEquals(0, testObj.getAllFiles().size());
        assertEquals(0,testObj.getInvertedIndex().size());

        Path file = Path.of("file");
        String word = "word";
        testObj.addWord(word, file);

        //then
        assertEquals(Set.of(file), testObj.getAllFiles());
        assertEquals(Map.of(word, Set.of(file)), testObj.getInvertedIndex());
    }

    @Test
    void addingSameWordWithDifferentFiles_shouldUpdateMapToHaveBothFilesForWordKey(){
        //given
        InvertedIndexPerIndexName testObj = new InvertedIndexPerIndexName();

        //when
        Path file1 = Path.of("file1");
        Path file2 = Path.of("file2");
        String word = "word";
        testObj.addWord(word, file1);
        testObj.addWord(word, file2);

        //then
        assertEquals(Set.of(file1, file2), testObj.getAllFiles());
        assertEquals(Map.of(word, Set.of(file1, file2)), testObj.getInvertedIndex());
    }


}