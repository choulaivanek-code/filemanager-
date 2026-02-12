package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileManagerTest {

    private LocalFileManager fileManager;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileManager = new LocalFileManager();
        ReflectionTestUtils.setField(fileManager, "storageFolder", tempDir.toString());
    }

    @Test
    void shouldSaveFile() throws Exception {
        FileInfo info = fileManager.save(
                "test.txt",
                new ByteArrayInputStream("hello".getBytes())
        );

        assertNotNull(info);
        assertEquals("test.txt", info.getFilename());
    }

    @Test
    void shouldGetExistingFile() throws Exception {
        fileManager.save(
                "file.txt",
                new ByteArrayInputStream("content".getBytes())
        );

        FileInfo file = fileManager.get("file.txt");

        assertNotNull(file);
        assertEquals("file.txt", file.getFilename());
    }

    @Test
    void shouldDownloadFile() throws Exception {
        fileManager.save(
                "download.txt",
                new ByteArrayInputStream("data".getBytes())
        );

        InputStream inputStream = fileManager.download("download.txt");

        assertNotNull(inputStream);
        inputStream.close(); // 🔥 IMPORTANT
    }
}
