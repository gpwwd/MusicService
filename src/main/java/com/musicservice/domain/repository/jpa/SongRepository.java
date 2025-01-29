package com.musicservice.domain.repository.jpa;

import com.musicservice.domain.model.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    Page<Song> findByUsersId(int userId, Pageable pageable);
}
