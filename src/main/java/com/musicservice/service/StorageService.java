package com.musicservice.service;

import java.io.InputStream;

public interface StorageService {
     boolean bucketExists(String bucketName) throws Exception;
     String uploadFile(String bucketName, String objectNamePath, InputStream inputStream, String contentType);
}
