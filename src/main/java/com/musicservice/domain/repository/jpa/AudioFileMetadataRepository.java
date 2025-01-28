package com.musicservice.domain.repository.jpa;


import com.musicservice.domain.model.SongAudioMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioFileMetadataRepository extends JpaRepository<SongAudioMetadataEntity, String> {
}