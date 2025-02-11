package com.musicservice.catalog.service;

import com.musicservice.catalog.dto.get.artist.ArtistGetDto;
import com.musicservice.catalog.dto.post.artist.ArtistPostDto;
import com.musicservice.catalog.mapper.ArtistMapperService;
import com.musicservice.domain.model.Artist;
import com.musicservice.domain.model.Genre;
import com.musicservice.domain.model.User;
import com.musicservice.domain.repository.jpa.ArtistRepository;
import com.musicservice.domain.repository.jpa.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapperService artistMapperService;
    private final GenreRepository genreRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, ArtistMapperService artistMapperService, GenreRepository genreRepository) {
        this.artistRepository = artistRepository;
        this.artistMapperService = artistMapperService;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public ArtistGetDto save(ArtistPostDto artistDto) {
        Artist artist = artistMapperService.artistPostDtoToEntity(artistDto);
        List<Genre> genres = genreRepository.findAllById(artistDto.getGenresIds());
        if (genres.size() != artistDto.getGenresIds().size()) {
            throw new RuntimeException("One or more genre IDs are invalid.");
        }
        artist.setGenres(genres);
        Artist saved = artistRepository.save(artist);
        return artistMapperService.entityToArtistGetDto(saved);
    }
}
