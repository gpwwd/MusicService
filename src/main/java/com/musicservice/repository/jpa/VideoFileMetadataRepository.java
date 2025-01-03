package com.musicservice.repository.jpa;


import com.musicservice.model.SongAudioMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoFileMetadataRepository extends JpaRepository<SongAudioMetadataEntity, String> {
}