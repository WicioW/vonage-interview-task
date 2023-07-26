package com.vonage.api.interview;

import com.vonage.api.interview.index.IndexHandlerImpl;
import com.vonage.api.interview.search.SearchHandlerImpl;
import com.vonage.api.interview.test.InvertedIndexPerIndexName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OurWishIsYourCommandsTest {

    @Test
    void testCreateCommandHandlersFillsIndexerAndSearcher() {
        //given-when
        CommandHandlers commandHandlers = OurWishIsYourCommands.createCommandHandlers();
        //then
        assertNotNull(commandHandlers);
        assertNotNull(commandHandlers.indexer());
        assertNotNull(commandHandlers.searcher());
    }

    @Test
    void testCreateCommandHandlersFillsIndexerAndSearcherWithCorrectInstances() {
        //given-when
        CommandHandlers commandHandlers = OurWishIsYourCommands.createCommandHandlers();
        //then
        assertTrue(commandHandlers.indexer() instanceof IndexHandlerImpl);
        assertTrue(commandHandlers.searcher() instanceof SearchHandlerImpl);
    }

    @Test
    void testCreateCommandHandlersFillsIndexerAndSearcherHaveTheSameMap() throws NoSuchFieldException, IllegalAccessException {
        //given-when
        CommandHandlers commandHandlers = OurWishIsYourCommands.createCommandHandlers();

        Field fieldWithMapFromIndexer = commandHandlers.indexer().getClass().getDeclaredField("invertedIndexPerIndexNameMap");
        Field fieldWithMapFromSearcher = commandHandlers.searcher().getClass().getDeclaredField("invertedIndexPerIndexNameMap");

        fieldWithMapFromIndexer.setAccessible(true);
        fieldWithMapFromSearcher.setAccessible(true);

        Map<String, InvertedIndexPerIndexName> invertedIndexMapFromIndexer =
                (Map<String, InvertedIndexPerIndexName>) fieldWithMapFromIndexer.get(commandHandlers.indexer());
        Map<String, InvertedIndexPerIndexName> invertedIndexMapFromSearcher =
                (Map<String, InvertedIndexPerIndexName>) fieldWithMapFromSearcher.get(commandHandlers.searcher());
        //then
        assertEquals(invertedIndexMapFromIndexer, invertedIndexMapFromSearcher);
    }

}