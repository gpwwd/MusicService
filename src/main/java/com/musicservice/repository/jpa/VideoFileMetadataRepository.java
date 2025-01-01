package com.musicservice.repository.jpa;


import com.musicservice.model.SongFileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoFileMetadataRepository extends JpaRepository<SongFileMetadataEntity, String> {
}