package com.musicservice.elasticsearch.service;

import com.musicservice.domain.model.Song;
import com.musicservice.domain.repository.jpa.SongRepository;
import com.musicservice.elasticsearch.documents.SongDoc;
import com.musicservice.elasticsearch.repository.SongElasticRepository;
import com.musicservice.exception.SongNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongIndexingService {
    private final SongElasticRepository songElasticRepository;
    private final SongRepository songRepository;

    @Autowired
    public SongIndexingService(SongElasticRepository songElasticRepository, SongRepository songRepository) {
        this.songElasticRepository = songElasticRepository;
        this.songRepository = songRepository;
    }

    @Transactional
    public void reindexAllSongs() {
        List<Song> songs = songRepository.findAll();

        songElasticRepository.saveAll(
                songs.stream().map(
                        song -> new SongDoc(
                                song.getId(),
                                song.getTitle(),
                                "artist"
                        )
                ).toList()
        );
    }

    @Transactional
    public int index(final Song song) {
        SongDoc songDoc = songElasticRepository.save(new SongDoc(
                song.getId(),
                song.getTitle(),
                "artist"
        ));
        return songDoc.getId();
    }

    @Transactional
    public void removeFromIndex(final int id) {
        SongDoc songToDelete = songElasticRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(id));

        songElasticRepository.deleteById(id);
    }
}
