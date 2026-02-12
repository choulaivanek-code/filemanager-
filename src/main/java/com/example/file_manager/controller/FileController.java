package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.service.api.FileManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileManager fileManager;

    public FileController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @GetMapping
    public ResponseEntity<List<FileInfo>> listFiles() throws Exception {
        return ResponseEntity.ok(fileManager.list());
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileInfo> getFile(@PathVariable String filename) throws Exception {
        FileInfo file = fileManager.get(filename);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(file);
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) throws Exception {
        boolean deleted = fileManager.delete(filename);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
