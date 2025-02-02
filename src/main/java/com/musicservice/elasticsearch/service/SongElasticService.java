package com.musicservice.elasticsearch.service;

import com.musicservice.catalog.dto.get.SongGetDto;
import com.musicservice.catalog.mapper.SongMapperService;
import com.musicservice.domain.model.Song;
import com.musicservice.domain.repository.jpa.SongRepository;
import com.musicservice.elasticsearch.documents.SongDoc;
import com.musicservice.elasticsearch.repository.SongElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Comparator.comparingInt;

@Service
public class SongElasticService {

    private final SongElasticRepository repository;
    private final SongRepository songRepository;
    private final SongMapperService songMapper;

    @Autowired
    public SongElasticService(SongElasticRepository repository, SongRepository songRepository,
                              SongMapperService songMapper) {
        this.repository = repository;
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    public Page<SongGetDto> searchSongsViaElastic(String searchText, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SongDoc> searchResults = repository.searchSongsByTitle(searchText, pageable);

        Map<Integer, Integer> idsMap = new HashMap<>();
        List<SongDoc> songDocs = searchResults.getContent();
        for (int i = 0; i < songDocs.size(); i++) {
            idsMap.put(songDocs.get(i).getId(), i);
        }

        Set<Integer> ids = idsMap.keySet();

        List<Song> songsFromDb = songRepository.findAllById(ids);
        songsFromDb.sort(comparingInt(song -> idsMap.get(song.getId())));
        List<SongGetDto> songDtoList = songsFromDb.stream()
                .map(songMapper::songToSongGetDto)
                .toList();


        return new PageImpl<>(songDtoList, pageable, searchResults.getTotalElements());
    }

    public void save(final SongDoc person) {
        repository.save(person);
    }

    public SongDoc findById(final Integer id) {
        return repository.findById(id).orElse(null);
    }
}
