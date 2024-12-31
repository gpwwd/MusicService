package com.musicservice.repository.jpa;


import com.musicservice.model.VideoFileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoFileMetadataRepository extends JpaRepository<VideoFileMetadataEntity, String> {
}