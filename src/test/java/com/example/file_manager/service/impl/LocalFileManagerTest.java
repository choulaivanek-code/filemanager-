package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileManagerTest {

    private LocalFileManager FileManager;
    private static final String TEST_FOLDER = "test-files";

    @BeforeEach
    void setup() throws Exception {
        FileManager = new LocalFileManager();
        FileManager.storageFolder = TEST_FOLDER;

        Files.createDirectories(Paths.get(TEST_FOLDER));
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
        FileInfo info = FileManager.save(
                "test.txt",
                new ByteArrayInputStream("hello".getBytes())
        );

        assertNotNull(info);
        assertEquals("test.txt", info.getFilename());
        assertEquals(5, info.getSize());
    }

    @Test
    void shouldSaveEmptyFile() {
        FileManager.save(
                "empty.txt",
                new ByteArrayInputStream(new byte[0])
        );

        List<FileInfo> files = FileManager.list();
        assertTrue(files.stream()
                .anyMatch(f -> f.getFilename().equals("empty.txt")));
    }

    @Test
    void shouldGetExistingFile() {
        FileManager.save(
                "file.txt",
                new ByteArrayInputStream("data".getBytes())
        );

        FileInfo info = FileManager.get("file.txt");

        assertNotNull(info);
        assertEquals("file.txt", info.getFilename());
    }

    @Test
    void shouldThrowWhenFileNotFoundOnGet() {
        assertThrows(FileStorageException.class,
                () -> FileManager.get("unknown.txt"));
    }

    @Test
    void shouldListFiles() {
        FileManager.save("a.txt",
                new ByteArrayInputStream("a".getBytes()));
        FileManager.save("b.txt",
                new ByteArrayInputStream("b".getBytes()));

        List<FileInfo> files = FileManager.list();

        assertEquals(2, files.size());
    }

    @Test
    void shouldReturnEmptyListWhenFolderEmpty() {
        List<FileInfo> files = FileManager.list();
        assertTrue(files.isEmpty());
    }

    @Test
    void shouldDeleteFile() {
        FileManager.save("delete.txt",
                new ByteArrayInputStream("data".getBytes()));

        boolean deleted = FileManager.delete("delete.txt");

        assertTrue(deleted);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingFile() {
        assertThrows(FileStorageException.class,
                () -> FileManager.delete("notfound.txt"));
    }

    @Test
    void shouldReadFile() {
        FileManager.save("read.txt",
                new ByteArrayInputStream("content".getBytes()));

        byte[] data = FileManager.read("read.txt");

        assertEquals("content", new String(data));
    }

    @Test
    void shouldThrowWhenReadingNonExistingFile() {
        assertThrows(FileStorageException.class,
                () -> FileManager.read("missing.txt"));
    }

    @Test
    void shouldPreventPathTraversal() {
        assertThrows(FileStorageException.class,
                () -> FileManager.save(
                        "../hack.txt",
                        new ByteArrayInputStream("bad".getBytes())
                ));
    }
}
