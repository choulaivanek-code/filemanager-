package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.exception.FileStorageException;
import com.example.file_manager.service.api.fileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocalFileManager implements fileManager {

    @Value("${storage.folder:files}")
    String storageFolder;

    private Path getStoragePath() {
        return Paths.get(storageFolder).toAbsolutePath().normalize();
    }

    private Path resolveSecurePath(String filename) {

        Path storagePath = getStoragePath();

        Path targetPath = storagePath.resolve(filename).normalize();

        if (!targetPath.startsWith(storagePath)) {
            throw new FileStorageException("Invalid file path detected.");
        }

        return targetPath;
    }

    @Override
    public FileInfo save(String filename, InputStream content) {

        try {
            Path storagePath = getStoragePath();
            Files.createDirectories(storagePath);

            Path targetPath = resolveSecurePath(filename);

            Files.copy(content, targetPath, StandardCopyOption.REPLACE_EXISTING);

            return new FileInfo(
                    targetPath.getFileName().toString(),
                    targetPath.toString(),
                    Files.size(targetPath)
            );

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    @Override
    public FileInfo get(String filename) {
        try {
            Path path = resolveSecurePath(filename);

            if (!Files.exists(path)) {
                return null;
            }

            return new FileInfo(
                    path.getFileName().toString(),
                    path.toString(),
                    Files.size(path)
            );

        } catch (IOException e) {
            throw new FileStorageException("Failed to read file info", e);
        }
    }

    @Override
    public List<FileInfo> list() {
        try (Stream<Path> paths = Files.list(getStoragePath())) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            return new FileInfo(
                                    path.getFileName().toString(),
                                    path.toString(),
                                    Files.size(path)
                            );
                        } catch (IOException e) {
                            throw new FileStorageException("Failed to list files", e);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new FileStorageException("Failed to list directory", e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path path = resolveSecurePath(filename);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file", e);
        }
    }

    @Override
    public InputStream download(String filename) {
        try {
            Path path = resolveSecurePath(filename);

            if (!Files.exists(path)) {
                return null;
            }

            return Files.newInputStream(path);

        } catch (IOException e) {
            throw new FileStorageException("Failed to download file", e);
        }
    }
}
