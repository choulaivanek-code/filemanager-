package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import com.example.file_manager.service.api.FileManager;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileManager fileManager;

    public FileController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @PostMapping
    public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile file) throws Exception {
        FileInfo saved = fileManager.save(
                file.getOriginalFilename(),
                file.getInputStream()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileInfo> get(@PathVariable String filename) throws Exception {
        FileInfo info = fileManager.get(filename);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping
    public ResponseEntity<List<FileInfo>> list() throws Exception {
        return ResponseEntity.ok(fileManager.list());
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Void> delete(@PathVariable String filename) throws Exception {
        boolean deleted = fileManager.delete(filename);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String filename) throws Exception {
        InputStream stream = fileManager.download(filename);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }
}
