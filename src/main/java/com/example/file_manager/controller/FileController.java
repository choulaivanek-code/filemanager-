package com.example.file_manager.controller;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.service.api.FileManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/files")
@Tag(name = "File Manager API", description = "Opérations sur les fichiers")
public class FileController {

    private final FileManager fileManager;

    public FileController(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Operation(summary = "Uploader un fichier")
    @PutMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        FileInfo info = fileManager.save(file.getOriginalFilename(), file.getInputStream());
        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Télécharger un fichier")
    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> download(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(fileManager.download(name).readAllBytes());
    }

    @Operation(summary = "Lister les fichiers")
    @GetMapping
    public ResponseEntity<List<FileInfo>> listFiles() throws Exception {
        return ResponseEntity.ok(fileManager.list());
    }

    @Operation(summary = "Supprimer un fichier")
    @DeleteMapping("/{name}")
    public ResponseEntity<Boolean> delete(@PathVariable String name) throws Exception {
        return ResponseEntity.ok(fileManager.delete(name));
    }
}


