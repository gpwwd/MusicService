package com.musicservice.catalog.service;

import com.musicservice.catalog.dto.get.artist.ArtistGetDto;
import com.musicservice.catalog.dto.post.artist.ArtistPostDto;
import com.musicservice.catalog.mapper.ArtistMapperService;
import com.musicservice.domain.model.Artist;
import com.musicservice.domain.model.Genre;
import com.musicservice.domain.model.User;
import com.musicservice.domain.repository.jpa.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapperService artistMapperService;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, ArtistMapperService artistMapperService) {
        this.artistRepository = artistRepository;
        this.artistMapperService = artistMapperService;
    }

    @Transactional
    public ArtistGetDto save(ArtistPostDto artistDto) {
        Artist artist = artistMapperService.artistPostDtoToEntity(artistDto);
        Set<Genre> genres = validateGenres(artistDto.getGenres());
        artist.setGenres(genres);
        Artist savedArtist = artistRepository.save(artist);
        return artistMapperService.entityToArtistGetDto(savedArtist);
    }

    // перенести в кастомный валидатор в контроллер
    private Set<Genre> validateGenres(Set<String> genres) {
        EnumSet<Genre> validGenres = EnumSet.allOf(Genre.class);

        return genres.stream()
                .map(genreString -> {
                    try {
                        return Genre.valueOf(genreString);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid genre: " + genreString);
                    }
                })
                .filter(validGenres::contains)
                .collect(Collectors.toSet());
    }


}
