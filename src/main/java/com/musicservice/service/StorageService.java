package com.musicservice.service;

import java.io.InputStream;

public interface StorageService {
    public boolean bucketExists(String bucketName) throws Exception;
    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType);
}
