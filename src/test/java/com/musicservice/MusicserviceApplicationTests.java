package com.musicservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MusicserviceApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Test
//    public void shouldGetJdbcConnection() throws SQLException {
//        try(
//            Connection connection = getNewConnection()) {
//            assertTrue(connection.isValid(1));
//            assertFalse(connection.isClosed());
//        }
//    }
//
//    private Connection getNewConnection() throws SQLException {
//        String url = "jdbc:postgresql://localhost:5432/users";
//        String user = "postgres";
//        String passwd = "rv6kn84l";
//        return DriverManager.getConnection(url, user, passwd);
//    }
}
