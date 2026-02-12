package com.example.file_manager.dto;

public class FileInfo {

    private String filename;
    private String path;
    private long size;

    public FileInfo() {}

    public FileInfo(String filename, long size) {
        this.filename = filename;
        this.size = size;
    }

    public FileInfo(String filename, String path, long size) {
        this.filename = filename;
        this.path = path;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
