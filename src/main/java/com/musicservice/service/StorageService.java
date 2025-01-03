package com.musicservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {
     void save(MultipartFile file, String path, UUID id) throws Exception;
     InputStream getInputStream(String path, UUID id, long offset, long length) throws Exception;
     void delete(String path, UUID id) throws Exception;
}
