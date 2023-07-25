package com.vonage.api.interview.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
public class FileTestUtils {

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public static String filePath(File tmpDir, String filename) {
        return new File(tmpDir, filename).getAbsolutePath();
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public static void createFile(File baseDir, String filename, String content) throws IOException {
        var file = new File(baseDir, filename);
        try (var fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        }
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    public static File createDir(File baseDir, String dirName) {
        var dir = new File(baseDir, dirName);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdir();
        return dir;
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */
}
