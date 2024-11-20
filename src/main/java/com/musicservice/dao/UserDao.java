package com.musicservice.dao;

import com.musicservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM public.user", new BeanPropertyRowMapper<>(User.class));
    }

    public User getUserById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM public.user WHERE id=?", new BeanPropertyRowMapper<>(User.class), id);
    }

    public void addUser(User user) {
        jdbcTemplate.update("INSERT INTO public.user (name) VALUES(?)", user.getName());
    }

    public void updateUser(int id, User updatedPerson) {
        jdbcTemplate.update("UPDATE public.user SET name=? WHERE id=?", updatedPerson.getName(), id);
    }

    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM public.user WHERE id=?", id);
    }
}
