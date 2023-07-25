package com.vonage.api.interview;

import com.vonage.api.interview.Application.Command.ParseException;
import com.vonage.api.interview.scoring.FileScores;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Map;
import java.util.TreeMap;

import static com.vonage.api.interview.Application.Command.EXIT;
import static com.vonage.api.interview.Application.Command.INDEX;
import static com.vonage.api.interview.Application.Command.SEARCH;
import static com.vonage.api.interview.Application.Command.UNKNOWN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
class ApplicationTests {

    public static final String HELLO = "Hello :)\n";
    public static final String VALID_COMMANDS = "Valid commands are:\n  exit - Exits the program\n  " +
            "index <index-name> <directory> - Index the supplied directory\n  search <index-name> <search string> -" +
            " Search the index for the supplied string\n";
    public static final String HAVE_FUN = "Have fun!\n";
    public static final String PROMPT = "> ";
    public static final String GOODBYE = "Goodbye :)\n";
    public static final String INVALID_COMMAND = "Invalid command\n";
    public static final String INVALID_NUMBER_OF_ARGS = "Incorrect number of args supplied\n";

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    @Nested
    public class Interactions {

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        private final CommandHandlers commandHandlers = mock(CommandHandlers.class);
        private final CommandHandlers.IndexHandler indexHandler = mock(CommandHandlers.IndexHandler.class);
        private final CommandHandlers.SearchHandler searchHandler = mock(CommandHandlers.SearchHandler.class);

        private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        private final PrintStream out = new PrintStream(outBuffer, true);
        private final PrintStream err = mock(PrintStream.class);

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @AfterEach
        public void close() {
            out.close();
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void index() throws IOException {
            when(commandHandlers.indexer()).thenReturn(indexHandler);

            var in = new StringReader("index name directory\n");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo(""));
            verify(indexHandler).index("name", "directory");
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void search() throws IOException {
            when(commandHandlers.searcher()).thenReturn(searchHandler);
            Map<String, Integer> fileScores = new TreeMap<>();
            fileScores.put("file1", 100);
            fileScores.put("file2", 40);
            when(searchHandler.search("name", "search string")).thenReturn(new FileScores(fileScores));

            var in = new StringReader("search name search string");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo("file1: 100\nfile2: 40\n"));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    @Nested
    public class HelpfulTextTest {

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        private final PrintStream out = new PrintStream(outBuffer, true);

        private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
        private final PrintStream err = new PrintStream(errBuffer, true);

        private final CommandHandlers commandHandlers = mock(CommandHandlers.class);
        private final CommandHandlers.IndexHandler indexHandler = mock(CommandHandlers.IndexHandler.class);

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @AfterEach
        public void close() {
            err.close();
            out.close();
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void exit() throws IOException {
            var in = new StringReader("exit");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo(""));
            assertThat(errBuffer.toString(), equalTo(HELLO + VALID_COMMANDS + HAVE_FUN + PROMPT + GOODBYE));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void invalidNumberOfArgs() throws IOException {
            when(commandHandlers.indexer()).thenReturn(indexHandler);

            var in = new StringReader("index abc");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo(""));
            assertThat(errBuffer.toString(), equalTo(HELLO + VALID_COMMANDS + HAVE_FUN + PROMPT + INVALID_NUMBER_OF_ARGS + VALID_COMMANDS + PROMPT));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void invalidCommand() throws IOException {
            var in = new StringReader("abc");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo(""));
            assertThat(errBuffer.toString(), equalTo(HELLO + VALID_COMMANDS + HAVE_FUN + PROMPT + INVALID_COMMAND + VALID_COMMANDS + PROMPT));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Test
        public void multipleCommands() throws IOException {
            var in = new StringReader("abc\nexit");
            new Application(in, out, err, commandHandlers).start();

            assertThat(outBuffer.toString(), equalTo(""));
            assertThat(errBuffer.toString(), equalTo(HELLO + VALID_COMMANDS + HAVE_FUN + PROMPT + INVALID_COMMAND + VALID_COMMANDS + PROMPT + GOODBYE));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    @Nested
    public class CommandTest {

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @ParameterizedTest
        @ValueSource(strings = {"exit", "exit ", "exit ignore", "exit ignore ignore"})
        void validExitCommands(String commandStr) {
            var command = Application.Command.parse(commandStr);
            assertThat(command, equalTo(EXIT));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @ParameterizedTest
        @ValueSource(strings = {"index name dir", "index name dir", "index name dir dir dir ", "index name /dir/a/b"})
        void validIndexCommands(String commandStr) {
            var command = Application.Command.parse(commandStr);
            assertThat(command, equalTo(INDEX));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @ParameterizedTest
        @ValueSource(strings = {"search index a", "search index a", "search index a b", "search index i'm a funny little string"})
        void validSearchCommands(String commandStr) {
            var command = Application.Command.parse(commandStr);
            assertThat(command, equalTo(SEARCH));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @ParameterizedTest
        @ValueSource(strings = {"a", "  ", "ab", "a b", "a    b", "    a"})
        void invalidCommands(String commandStr) {
            var command = Application.Command.parse(commandStr);
            assertThat(command, equalTo(UNKNOWN));
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Nested
        public class SearchCommandTest {

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @ParameterizedTest
            @ValueSource(strings = {"search index a", "search index a b", "search index i'm a funny little string"})
            void validSearchCommandExtractsIndex(String commandStr) {
                var args = SEARCH.argsFrom(commandStr);
                assertThat(args, arrayWithSize(2));
                assertThat(args[0], equalTo("index"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @Test
            void validSearchCommandExtractsSearchString() {
                var args = SEARCH.argsFrom("search index i'm a funny     little string");
                assertThat(args[1], equalTo("i'm a funny     little string"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @ParameterizedTest
            @ValueSource(strings = {"search", "search ", "search index"})
            void invalidSearchCommandThrowsParseException(String commandStr) {
                var exception = assertThrows(ParseException.class, () -> SEARCH.argsFrom(commandStr));
                assertThat(exception.getMessage(), equalTo("Incorrect number of args supplied"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        @Nested
        public class IndexCommandTest {

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @ParameterizedTest
            @ValueSource(strings = {"index name a", "index name a b", "index name i'm a funny little string"})
            void validIndexCommandExtractsIndex(String commandStr) {
                var args = INDEX.argsFrom(commandStr);
                assertThat(args, arrayWithSize(2));
                assertThat(args[0], equalTo("name"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @Test
            void validIndexCommandExtractsDirectoryString() {
                var args = INDEX.argsFrom("index name /valid/directory/");
                assertThat(args[1], equalTo("/valid/directory/"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @Test
            void validIndexCommandExtractsDirectoryStringWithSpaces() {
                var args = INDEX.argsFrom("index name /valid/directory/    /something");
                assertThat(args[1], equalTo("/valid/directory/    /something"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

            @ParameterizedTest
            @ValueSource(strings = {"index", "index ", "index name"})
            void invalidIndexCommandThrowsParseException(String commandStr) {
                var exception = assertThrows(ParseException.class, () -> SEARCH.argsFrom(commandStr));
                assertThat(exception.getMessage(), equalTo("Incorrect number of args supplied"));
            }

            /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

}
