package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileManagerTest {

    private LocalFileManager FileManager;
    private static final String TEST_FOLDER = "test-files";

    @BeforeEach
    void setup() {
        FileManager = new LocalFileManager();
        FileManager.storageFolder = TEST_FOLDER;
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

        FileInfo info = FileManager.save("test.txt", content);

        assertNotNull(info);
        assertEquals("test.txt", info.getFilename());
        assertEquals(5, info.getSize());
    }

    @Test
    void shouldGetExistingFile() {
        FileManager.save("file.txt",
                new ByteArrayInputStream("data".getBytes()));

        FileInfo info = FileManager.get("file.txt");

        assertNotNull(info);
        assertEquals("file.txt", info.getFilename());
        assertEquals(4, info.getSize());
    }

    @Test
    void shouldReturnNullWhenFileNotFound() {
        assertNull(FileManager.get("unknown.txt"));
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
    void shouldDeleteFile() {
        FileManager.save("delete.txt",
                new ByteArrayInputStream("data".getBytes()));

        boolean deleted = FileManager.delete("delete.txt");

        assertTrue(deleted);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingFile() {
        assertThrows(FileStorageException.class, () ->
                FileManager.delete("missing.txt")
        );
    }

    @Test
    void shouldDownloadExistingFile() throws Exception {
        FileManager.save("download.txt",
                new ByteArrayInputStream("abc".getBytes()));

        InputStream input = FileManager.download("download.txt");

        assertNotNull(input);
        byte[] content = input.readAllBytes();
        assertEquals("abc", new String(content));
    }

    @Test
    void shouldThrowExceptionWhenDownloadingMissingFile() {
        assertThrows(FileStorageException.class, () ->
                FileManager.download("missing.txt")
        );
    }

    @Test
    void shouldPreventPathTraversal() {
        assertThrows(FileStorageException.class, () ->
                FileManager.save("../hack.txt",
                        new ByteArrayInputStream("bad".getBytes()))
        );
    }

    @Test
    void shouldReturnEmptyListWhenFolderEmpty() {
        List<FileInfo> files = FileManager.list();
        assertNotNull(files);
        assertTrue(files.isEmpty());
    }

}
