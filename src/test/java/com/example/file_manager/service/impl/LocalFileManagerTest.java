package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileManagerTest {

    private LocalFileManager fileManager;
    private static final String TEST_FOLDER = "test-files";

    @BeforeEach
    void setup() {
        fileManager = new LocalFileManager();
        fileManager.storageFolder = TEST_FOLDER;
    }

    @AfterEach
    void cleanup() throws Exception {
        Path path = Paths.get(TEST_FOLDER);
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(p -> p.toFile().delete());
        }
    }

    @Test
    void shouldSaveFile() {
        ByteArrayInputStream content =
                new ByteArrayInputStream("hello".getBytes());

        FileInfo info = fileManager.save("test.txt", content);

        assertNotNull(info);
        assertEquals("test.txt", info.getFilename());
        assertEquals(5, info.getSize());
    }

    @Test
    void shouldGetExistingFile() {
        fileManager.save("file.txt",
                new ByteArrayInputStream("data".getBytes()));

        FileInfo info = fileManager.get("file.txt");

        assertNotNull(info);
        assertEquals("file.txt", info.getFilename());
    }

    @Test
    void shouldReturnNullWhenFileNotFound() {
        assertNull(fileManager.get("unknown.txt"));
    }

    @Test
    void shouldListFiles() {
        fileManager.save("a.txt",
                new ByteArrayInputStream("a".getBytes()));
        fileManager.save("b.txt",
                new ByteArrayInputStream("b".getBytes()));

        List<FileInfo> files = fileManager.list();

        assertEquals(2, files.size());
    }

    @Test
    void shouldDeleteFile() {
        fileManager.save("delete.txt",
                new ByteArrayInputStream("data".getBytes()));

        boolean deleted = fileManager.delete("delete.txt");

        assertTrue(deleted);
    }

    @Test
    void shouldPreventPathTraversal() {
        assertThrows(FileStorageException.class, () ->
                fileManager.save("../hack.txt",
                        new ByteArrayInputStream("bad".getBytes()))
        );
    }
}
