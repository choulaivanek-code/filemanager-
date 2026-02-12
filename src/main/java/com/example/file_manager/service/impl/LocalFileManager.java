package com.example.file_manager.service.impl;

import com.example.file_manager.dto.FileInfo;
import com.example.file_manager.service.api.fileManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocalFileManager implements fileManager {

    @Value("${storage.folder:files}")
    String storageFolder;

    @Override
    public FileInfo save(String filename, InputStream content) throws Exception {
        File folder = new File(storageFolder);
        if (!folder.exists()) folder.mkdirs();

        File target = new File(folder, filename);
        try (FileOutputStream out = new FileOutputStream(target)) {
            out.write(content.readAllBytes());
        }

        return new FileInfo(filename, target.getAbsolutePath(), target.length());
    }

    @Override
    public FileInfo get(String filename) throws Exception {
        File file = new File(storageFolder, filename);
        if (!file.exists()) return null;
        return new FileInfo(filename, file.getAbsolutePath(), file.length());
    }

    @Override
    public List<FileInfo> list() throws Exception {
        List<FileInfo> files = new ArrayList<>();
        File folder = new File(storageFolder);

        if (!folder.exists()) return files;

        for (File f : folder.listFiles()) {
            files.add(new FileInfo(
                    f.getName(),
                    f.getAbsolutePath(),
                    f.length()
            ));
        }
        return files;
    }

    @Override
    public boolean delete(String filename) throws Exception {
        File file = new File(storageFolder, filename);
        return file.exists() && file.delete();
    }

    @Override
    public InputStream download(String filename) throws Exception {
        File file = new File(storageFolder, filename);
        return file.exists() ? new java.io.FileInputStream(file) : null;
    }
}
