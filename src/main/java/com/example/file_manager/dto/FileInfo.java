package com.example.file_manager.dto;

public class FileInfo {

    private String filename;
    private String path;
    private long size;
    private long lastModified;

    public FileInfo() {}

    public FileInfo(String filename, long size) {
        this.filename = filename;
        this.size = size;
    }

    public FileInfo(String filename, long size, long lastModified) {
        this.filename = filename;
        this.size = size;
        this.lastModified = lastModified;
    }

    public FileInfo(String filename, String path, long size) {
        this.filename = filename;
        this.path = path;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
