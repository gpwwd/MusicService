package com.musicservice.dao;

import com.musicservice.exception.SongNotFoundException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.Comment;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SongDao {

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public SongDao(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public boolean songExists(int songId) {
        Session session = sessionFactory.getCurrentSession();
        Song song = session.get(Song.class, songId);
        return song != null;
    }

    @Transactional(readOnly = true)
    public List<Song> getSongs() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select s from Song s", Song.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Song getSongById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Song song = session.get(Song.class, id);

        if (song == null) {
            throw new SongNotFoundException(id);
        }

        return song;
    }

    @Transactional
    public void addSong(Song song) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(song);
    }

    @Transactional
    public void updateSong(int id, Song updatedSong) {
        Session session = sessionFactory.getCurrentSession();
        Song songToBeUpdated = session.get(Song.class, id);

        if(songToBeUpdated == null) {
            throw new SongNotFoundException(id);
        }

        songToBeUpdated.setTitle(updatedSong.getTitle());
    }

    @Transactional
    public void deleteSong(int id) {
        Session session = sessionFactory.getCurrentSession();
        Song song = session.get(Song.class, id);
        if(song == null) {
            throw new SongNotFoundException(id);
        }
        session.remove(song);
    }

    @Transactional
    public Song getSongByIdWithComments(int id) {
        Session session = sessionFactory.getCurrentSession();
        Song song = session.get(Song.class, id);
        if(song == null) {
            throw new SongNotFoundException(id);
        }
        Hibernate.initialize(song.getComments());
        return song;
    }

//    @Transactional
    public List<Comment> findCommentsBySongId(int songId) {
        String sql = "SELECT * FROM comments WHERE song_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Comment comment = new Comment(rs.getInt("id"),
                    rs.getString("text"),
                    rs.getInt("rating"));
            return comment;
        }, songId);

//        Session session = sessionFactory.getCurrentSession();
//        String hql = "FROM Comment c WHERE c.song.id = :songId";
//
//        Query<Comment> query = session.createQuery(hql, Comment.class);
//        query.setParameter("songId", songId);
//
//        return query.getResultList();
    }

    public void addComment(Comment comment, int songId) {
        String sql = "INSERT INTO comments (song_id, text, rating) VALUES (?, ?)";
        jdbcTemplate.update(sql, songId, comment.getComment(), comment.getRating());
    }
}
