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
        String sql = "SELECT COUNT(*) FROM public.user WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM public.user", new BeanPropertyRowMapper<>(User.class));
    }

    public User getUserById(int id) throws UserNotFoundException {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM public.user WHERE id = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO public.user (name) VALUES(?)", user.getName());
    }

    public void updateUser(int id, User updatedUser) throws UserNotFoundException {
        int rowsAffected = jdbcTemplate.update("UPDATE public.user SET name=? WHERE id=?", updatedUser.getName(), id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException(id);
        }
    }

    public void deleteUser(int id) throws UserNotFoundException {
        int rowsAffected = jdbcTemplate.update("DELETE FROM public.user WHERE id=?", id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException(id);
        }
    }

    public void addFavouriteSong(int userId, int songId) {
        String sql = "INSERT INTO user_favourite_songs (user_id, song_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, userId, songId);
        if (rowsAffected == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
