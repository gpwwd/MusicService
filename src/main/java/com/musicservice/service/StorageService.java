package com.musicservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {
     boolean bucketExists(String bucketName) throws Exception;
     String uploadFile(String bucketName, String objectNamePath, InputStream inputStream, String contentType);
     void save(MultipartFile file, String path, UUID id) throws Exception;
     InputStream getInputStream(String path, UUID id, long offset, long length) throws Exception;
}
