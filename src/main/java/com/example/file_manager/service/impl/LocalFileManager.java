package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import com.example.file_manager.service.api.FileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalFileManager implements FileManager {

    @Value("${storage.folder:files}")
    public String storageFolder;

    private Path getStoragePath() {
        return Paths.get(storageFolder)
                .toAbsolutePath()
                .normalize();
    }

    private Path resolveSecurePath(String filename) {
        try {
            Path storagePath = getStoragePath();
            Path target = storagePath.resolve(filename).normalize();

            if (!target.startsWith(storagePath)) {
                throw new FileStorageException("Invalid file path");
            }

            return target;
        } catch (Exception e) {
            throw new FileStorageException("Invalid file path", e);
        }
    }

    @Override
    public FileInfo save(String filename, InputStream content) {
        try {
            Path storagePath = getStoragePath();
            Files.createDirectories(storagePath);

            Path target = resolveSecurePath(filename);

            Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);

            return new FileInfo(
                    filename,
                    Files.size(target)
            );

        } catch (IOException e) {
            throw new FileStorageException("Failed to save file", e);
        }
    }

    @Override
    public FileInfo get(String filename) {
        try {
            Path target = resolveSecurePath(filename);

            if (!Files.exists(target)) {
                throw new FileStorageException("File not found");
            }

            return new FileInfo(
                    filename,
                    Files.size(target)
            );

        } catch (IOException e) {
            throw new FileStorageException("Failed to get file", e);
        }
    }

    @Override
    public List<FileInfo> list() {
        try {
            Path storagePath = getStoragePath();

            if (!Files.exists(storagePath)) {
                return List.of();
            }

            try (Stream<Path> stream = Files.list(storagePath)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(path -> {
                            try {
                                return new FileInfo(
                                        path.getFileName().toString(),
                                        Files.size(path)
                                );
                            } catch (IOException e) {
                                throw new FileStorageException("Failed to read file size", e);
                            }
                        })
                        .collect(Collectors.toList());
            }

        } catch (IOException e) {
            throw new FileStorageException("Failed to list directory", e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path target = resolveSecurePath(filename);

            if (!Files.exists(target)) {
                throw new FileStorageException("File not found");
            }

            Files.delete(target);
            return true;

        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file", e);
        }
    }

    @Override
    public byte[] read(String filename) {
        try {
            Path target = resolveSecurePath(filename);

            if (!Files.exists(target)) {
                throw new FileStorageException("File not found");
            }

            return Files.readAllBytes(target);

        } catch (IOException e) {
            throw new FileStorageException("Failed to read file", e);
        }
    }

    @Override
    public InputStream download(String name) {
        return null;
    }
}
