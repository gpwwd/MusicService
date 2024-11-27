package com.musicservice.dao;

import com.musicservice.exception.SongNotFoundException;
import com.musicservice.model.Comment;
import com.musicservice.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

    public Song getSongByIdWithComments(int id) {
        String sql = "SELECT s.*, c.id AS comment_id, c.text AS comment_text , c.rating AS comment_rating " +
                "FROM songs s " +
                "LEFT JOIN comments c ON s.id = c.song_id " +
                "WHERE s.id = ?";

        List<Song> results = jdbcTemplate.query(sql, new SongMapper(), id);

        Song song = null;
        List<Comment> comments = new ArrayList<>();

        for (Song songWithComments : results) {
            if (song == null) {
                song = songWithComments;
            }
            comments.addAll(songWithComments.getComments());
        }

        if(song == null) {
            throw new SongNotFoundException(id);
        }

        song.setComments(comments);

        return song;
    }

    public List<Comment> findCommentsBySongId(int songId) {
        String sql = "SELECT * FROM comments WHERE song_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Comment comment = new Comment();
            comment.setId(rs.getInt("id"));
            comment.setComment(rs.getString("text"));
            comment.setRating(rs.getInt("rating"));
            return comment;
        }, songId);
    }

    public void addComment(Comment comment, int songId) {
        String sql = "INSERT INTO comments (song_id, text, rating) VALUES (?, ?)";
        jdbcTemplate.update(sql, songId, comment.getComment(), comment.getRating());
    }
}
