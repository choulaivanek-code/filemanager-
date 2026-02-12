package com.example.file_manager.service.api;

import com.example.file_manager.dto.FileInfo;
import java.io.InputStream;
import java.util.List;

public interface FileManager {

    FileInfo save(String filename, InputStream content);

    FileInfo get(String filename);

    List<FileInfo> list();

    boolean delete(String filename);

    byte[] read(String filename);

    InputStream download(String name);
}

