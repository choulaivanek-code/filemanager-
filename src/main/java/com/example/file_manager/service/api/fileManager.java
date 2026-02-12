package com.example.file_manager.service.api;

import com.example.file_manager.dto.FileInfo;
import java.io.InputStream;
import java.util.List;


public interface fileManager {
    FileInfo save(String filename, InputStream content) throws Exception;
    FileInfo get(String filename) throws Exception;
    List<FileInfo> list() throws Exception;
    boolean delete(String filename) throws Exception;
    InputStream download(String filename) throws Exception;
}
