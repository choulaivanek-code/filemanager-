package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.service.api.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private FileManager fileManager;

    @InjectMocks
    private FileController controller;

    private FileInfo fileInfo;

    @BeforeEach
    void setup() {
        fileInfo = new FileInfo("test.txt", 5);
    }

    @Test
    void shouldListFiles() throws Exception {
        when(fileManager.list()).thenReturn(List.of(fileInfo));

        ResponseEntity<List<FileInfo>> response = controller.listFiles();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void shouldGetFile() throws Exception {
        when(fileManager.get("test.txt")).thenReturn(fileInfo);

        ResponseEntity<FileInfo> response = controller.getFile("test.txt");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("test.txt", response.getBody().getFilename());
    }

    @Test
    void shouldReturn404WhenFileNotFound() throws Exception {
        when(fileManager.get("missing.txt")).thenReturn(null);

        ResponseEntity<FileInfo> response = controller.getFile("missing.txt");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteFile() throws Exception {
        when(fileManager.delete("test.txt")).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteFile("test.txt");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturn404WhenDeleteFails() throws Exception {
        when(fileManager.delete("missing.txt")).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteFile("missing.txt");

        assertEquals(404, response.getStatusCodeValue());
    }
}
