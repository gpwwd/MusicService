package com.musicservice.dao;

import com.musicservice.exception.UserNotFoundException;
import com.musicservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userExists(int userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public boolean userExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    public User getUserById(int id) throws UserNotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO users (name, email) VALUES(?, ?)", user.getName(), user.getEmail());
    }

    public void updateUser(int id, User updatedUser) throws UserNotFoundException {
        int rowsAffected = jdbcTemplate.update("UPDATE users SET name=?, email=? WHERE id=?", updatedUser.getName(), updatedUser.getEmail(), id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException(id);
        }
    }

    public void deleteUser(int id) throws UserNotFoundException {
        int rowsAffected = jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException(id);
        }
    }

    public void addFavouriteSong(int userId, int songId) {
        String sql = "INSERT INTO user_songs (user_id, song_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, userId, songId);
        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
