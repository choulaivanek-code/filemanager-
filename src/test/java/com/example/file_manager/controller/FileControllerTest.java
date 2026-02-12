package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import com.example.file_manager.service.api.FileManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileManager fileManager;

    @Test
    void shouldUploadFile() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt",
                        MediaType.TEXT_PLAIN_VALUE,
                        "hello".getBytes());

        Mockito.when(fileManager.save(anyString(), any()))
                .thenReturn(new FileInfo("test.txt", 5));

        mockMvc.perform(multipart("/files").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.filename").value("test.txt"));
    }

    @Test
    void shouldGetFile() throws Exception {
        Mockito.when(fileManager.get("test.txt"))
                .thenReturn(new FileInfo("test.txt", 5));

        mockMvc.perform(get("/files/test.txt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filename").value("test.txt"));
    }

    @Test
    void shouldReturn404WhenFileNotFound() throws Exception {
        Mockito.when(fileManager.get("missing.txt"))
                .thenReturn(null);

        mockMvc.perform(get("/files/missing.txt"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListFiles() throws Exception {
        Mockito.when(fileManager.list())
                .thenReturn(List.of(
                        new FileInfo("a.txt", 1),
                        new FileInfo("b.txt", 1)
                ));

        mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldDeleteFile() throws Exception {
        Mockito.when(fileManager.delete("test.txt"))
                .thenReturn(true);

        mockMvc.perform(delete("/files/test.txt"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeleteFails() throws Exception {
        Mockito.when(fileManager.delete("missing.txt"))
                .thenReturn(false);

        mockMvc.perform(delete("/files/missing.txt"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDownloadFile() throws Exception {
        Mockito.when(fileManager.download("file.txt"))
                .thenReturn(new ByteArrayInputStream("data".getBytes()));

        mockMvc.perform(get("/files/download/file.txt"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldHandleException() throws Exception {
        Mockito.when(fileManager.get("error.txt"))
                .thenThrow(new FileStorageException("Error"));

        mockMvc.perform(get("/files/error.txt"))
                .andExpect(status().isInternalServerError());
    }

}
