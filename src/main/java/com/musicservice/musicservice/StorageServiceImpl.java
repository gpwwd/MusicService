package com.musicservice.musicservice;

import com.musicservice.service.StorageService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private MinioClient minioClient;

    public void StorageServiceImpl(MinioClient minioClient) {
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    private void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    public String uploadFile(String bucketName, String objectNamePath, InputStream inputStream, String contentType) {
        try {
            boolean found = bucketExists(bucketName);
            if (!found) {
                this.createBucket(bucketName);
           }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectNamePath)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(contentType)
                            .build());
            return objectNamePath;
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading file: " + e.getMessage());
        }
    }

    public InputStream getFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving file: " + e.getMessage());
        }
    }
}
