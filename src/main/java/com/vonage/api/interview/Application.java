package com.vonage.api.interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.vonage.api.interview.Application.Command.UNKNOWN;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

public class Application {

    public static final String PROMPT = "> ";
    private final CommandHandlers handlers;
    private final Reader in;
    private final PrintStream out;
    private final PrintStream err;

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public Application(Reader in, PrintStream out, PrintStream err, CommandHandlers handlers) {
        this.in = in;
        this.out = out;
        this.err = err;
        this.handlers = handlers;
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public static void main(String[] args) {
        try {
            new Application(new InputStreamReader(System.in),
                            System.out,
                            System.err,
                            OurWishIsYourCommands.createCommandHandlers()).start();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    void start() throws IOException {
        BufferedReader reader = new BufferedReader(in);
        err.println(welcomeMessage());
        String line;
        err.print(PROMPT);
        while ((line = reader.readLine()) != null) {
            var command = Command.parse(line);

            String[] args;
            try {
                args = command.argsFrom(line);
            } catch (Command.ParseException ex) {
                err.print(ex.getMessage() + "\n" + validCommands());
                err.print(PROMPT);
                continue;
            }

            try {
                switch (command) {
                    case EXIT -> {
                        err.println("Goodbye :)");
                        return;
                    }
                    case INDEX -> handlers.indexer().index(args[0], args[1]);
                    case SEARCH -> {
                        var results = handlers.searcher().search(args[0], args[1]);
                        results.getFileScores().forEach((filename, score) -> out.println(filename + ": " + score));
                    }
                    case UNKNOWN -> err.print("Invalid command\n" + validCommands());
                }
            } catch (Exception ex) {
                err.println(ex.getMessage());
            }
            err.print(PROMPT);
        }
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    private String welcomeMessage() {
        return "Hello :)\n" + validCommands() + "Have fun!";
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    private String validCommands() {
        var validCommands = new StringBuilder("Valid commands are:\n");
        Stream.of(Command.values())
              .filter(type -> type != UNKNOWN)
              .forEach(type -> validCommands.append("  ")
                                            .append(type.commandStr())
                                            .append(" ")
                                            .append(type.helpStr())
                                            .append("\n"));
        return validCommands.toString();
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public enum Command {

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        UNKNOWN("unknown", 0, "control command"),
        EXIT("exit", 0, "- Exits the program"),
        INDEX("index", 2, "<index-name> <directory> - Index the supplied directory"),
        SEARCH("search", 2, "<index-name> <search string> - Search the index for the supplied string");

        private static final Map<String, Command> COMMAND_TYPES =
                stream(values()).collect(toMap(e -> e.commandStr, identity()));

        private final String commandStr;
        private final int numArgs;
        private final String helpStr;

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        Command(String commandStr, int numArgs, String helpStr) {
            this.commandStr = commandStr;
            this.numArgs = numArgs;
            this.helpStr = helpStr;
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        public static Command parse(String commandStr) {
            var commandParts = commandStr.split(" ", 2);
            return Optional.ofNullable(COMMAND_TYPES.get(commandParts[0])).orElse(UNKNOWN);
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        public String commandStr() {
            return commandStr;
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        public String helpStr() {
            return helpStr;
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        public String[] argsFrom(String commandStr) {
            var commandTokens = commandStr.split(" ", numArgs + 1);
            var commandArgs = copyOfRange(commandTokens, 1, commandTokens.length);
            if (commandArgs.length != numArgs) {
                throw new ParseException("Incorrect number of args supplied");
            }
            return commandArgs;
        }

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

        public static class ParseException extends RuntimeException {

            public ParseException(String reason) {
                super(reason);
            }
        }

    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
}
