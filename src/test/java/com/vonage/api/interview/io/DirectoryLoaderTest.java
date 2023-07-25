package com.vonage.api.interview.io;

import com.vonage.api.interview.io.DirectoryLoader.FileContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static com.vonage.api.interview.io.FileTestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class DirectoryLoaderTest {

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    private final DirectoryLoader directoryLoader = new DirectoryLoader();

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    @Test
    void testAllFilesFoundInFlatDirectory(@TempDir File tmpDir) throws IOException {

        createFile(tmpDir, "file1", "meep");
        createFile(tmpDir, "file2", "moop");
        createFile(tmpDir, "file3", "flaps");

        var directoryContent = directoryLoader.load(tmpDir.toPath());

        assertThat(directoryContent, containsInAnyOrder(new FileContent(filePath(tmpDir, "file1"), "meep"),
                                                        new FileContent(filePath(tmpDir, "file2"), "moop"),
                                                        new FileContent(filePath(tmpDir, "file3"), "flaps")));
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

    @Test
    void testAllFilesFoundInNestedDirectory(@TempDir File tmpDir) throws IOException {

        createFile(tmpDir, "file1", "meep");
        var subdir = createDir(tmpDir, "subdir");
        createFile(subdir, "file2", "flaps");

        var directoryContent = directoryLoader.load(tmpDir.toPath());

        assertThat(directoryContent, containsInAnyOrder(new FileContent(filePath(tmpDir,"file1"), "meep"),
                                                        new FileContent(filePath(subdir,"file2"), "flaps")));
    }

    /* YOU MUST NOT CHANGE ANYTHING IN THIS FILE! NOT EVEN SLIGHTLY */

}
