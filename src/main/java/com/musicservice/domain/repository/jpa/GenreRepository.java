package com.musicservice.domain.repository.jpa;

import com.musicservice.domain.model.Artist;
import com.musicservice.domain.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
