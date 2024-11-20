package com.musicservice.dao;

import com.musicservice.model.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {

    private static Connection connection;

    {
        try{
            connection = getNewConnection();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }

    private Connection getNewConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Драйвер не найден в CLASSPATH: " + e.getMessage());
        }
        String url = "jdbc:postgresql://localhost:5432/users";
        String user = "postgres";
        String passwd = "rv6kn84l";
        return DriverManager.getConnection(url, user, passwd);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM public.user";
            ResultSet resultSet = statement.executeQuery(SQL);

            while(resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));

                users.add(user);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public void addUser(User user) {

        try {
            Statement statement = connection.createStatement();
            String SQL = "INSERT INTO public.user VALUES(" + 2  + ",'" + user.getName() + "')";
            statement.executeUpdate(SQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
