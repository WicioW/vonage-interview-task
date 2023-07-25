package com.vonage.api.interview.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

/* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
public class DirectoryLoader {

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    public Collection<FileContent> load(Path directory) {
        try (var paths = Files.walk(directory)) {
            return paths.filter(Files::isRegularFile)
                        .map(this::loadFile)
                        .collect(toList());
        } catch (IOException e) {
            throw new DirectoryLoadException(e);
        }
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    private FileContent loadFile(Path path) {
        try {
            return new FileContent(path.toFile().getAbsolutePath(), Files.readString(path));
        } catch (IOException e) {
            throw new FileLoadException(path, e);
        }
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public abstract static class DirectoryLoaderException extends RuntimeException {
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
        public DirectoryLoaderException(String message, Throwable cause) {
            super(message, cause);
        }
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    private static class DirectoryLoadException extends DirectoryLoaderException {
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
        public DirectoryLoadException(Exception e) {
            super("An error occurred whilst loading the directory", e);
        }
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    private static class FileLoadException extends DirectoryLoaderException {
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
        public FileLoadException(Path path, Exception e) {
            super("An error occurred whilst loading the file '" + path + "'", e);
        }
        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public record FileContent(String path, String contents) {

        /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
    }
}
