package com.musicservice.dao;

import com.musicservice.exception.SongNotFoundException;
import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.Song;
import com.musicservice.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate, SessionFactory sessionFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public boolean userExists(int userId) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        return user != null;
    }

    @Transactional(readOnly = true)
    public boolean userExists(String email) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, email);
        return user != null;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public User getUserById(int id) throws UserNotFoundException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);

        if (user == null) {
            throw new UserNotFoundException(id);
        }

        return user;
    }

    @Transactional
    public void addUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Transactional
    public void updateUser(int id, User updatedUser) throws UserNotFoundException {
        Session session = sessionFactory.getCurrentSession();
        User personToBeUpdated = session.get(User.class, id);

        if(personToBeUpdated == null) {
            throw new UserNotFoundException(id);
        }

        personToBeUpdated.setName(updatedUser.getName());
        personToBeUpdated.setEmail(updatedUser.getEmail());
    }

    @Transactional
    public void deleteUser(int id) throws UserNotFoundException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, id);
        if(user == null) {
            throw new UserNotFoundException(id);
        }
        session.remove(user);
    }

    @Transactional(readOnly = true)
    public User getUserWithFavouriteSong(int userId) throws UserNotFoundException {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        if(user == null) {
            throw new UserNotFoundException(userId);
        }
        Hibernate.initialize(user.getFavouriteSongs());
        return user;
    }

//    @Transactional
    public void addFavouriteSong(int userId, int songId) {
//        Session session = sessionFactory.getCurrentSession();
//        User user = session.get(User.class, userId);
//        if (user == null) {
//            throw new UserNotFoundException(userId);
//        }
//
//        Song song = session.get(Song.class, songId);
//        if (song == null) {
//            throw new SongNotFoundException(songId);
//        }
//
//        if (user.getFavouriteSongs() == null) {
//            user.setFavouriteSongs(new ArrayList<>());
//        }
//
//        user.getFavouriteSongs().add(song);
        String sql = "INSERT INTO user_songs (user_id, song_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, userId, songId);
        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
