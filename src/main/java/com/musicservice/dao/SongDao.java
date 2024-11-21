package com.musicservice.dao;

import com.musicservice.exception.SongNotFoundException;
import com.musicservice.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SongDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SongDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean songExists(int songId) {
        String sql = "SELECT COUNT(*) FROM songs WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, songId);
        return count != null && count > 0;
    }

    public List<Song> getSongs() {
        return jdbcTemplate.query("SELECT * FROM songs", new BeanPropertyRowMapper<>(Song.class));
    }

    public Song getSongById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM songs WHERE id = ?",
                    new BeanPropertyRowMapper<>(Song.class),
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new SongNotFoundException(id);
        }
    }

    public void addSong(Song song) {
        jdbcTemplate.update("INSERT INTO songs (title) VALUES(?)", song.getTitle());
    }

    public void updateSong(int id, Song updatedSong) {
        int rowsAffected = jdbcTemplate.update("UPDATE songs SET title=? WHERE id=?", updatedSong.getTitle(), id);
        if (rowsAffected == 0) {
            throw new SongNotFoundException(id);
        }
    }

    public void deleteSong(int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM songs WHERE id=?", id);
        if (rowsAffected == 0) {
            throw new SongNotFoundException(id);
        }
    }
}
