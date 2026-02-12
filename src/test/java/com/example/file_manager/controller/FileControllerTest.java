package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.service.api.FileManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileManager FileManager;

    @Test
    void shouldUploadFile() throws Exception {

        // Fake fichier envoyé
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello".getBytes()
        );

        // Fake réponse du service
        FileInfo fileInfo = new FileInfo("test.txt", null, 5);

        Mockito.when(FileManager.save(eq("test.txt"), any(ByteArrayInputStream.class)))
                .thenReturn(fileInfo);

        mockMvc.perform(multipart("/files/upload")
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.filename").value("test.txt"))
                .andExpect(jsonPath("$.size").value(5));
    }
}
