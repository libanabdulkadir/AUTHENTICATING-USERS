package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/userdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";



    public static boolean registerUser(String username, String email, String phone, String password) {
        String query = "INSERT INTO users (username, email, phone, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, password);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

// for the UserApp
//CREATE DATABASE userdb;
//USE userdb;
//
//CREATE TABLE users (
//        id INT AUTO_INCREMENT PRIMARY KEY,
//        username VARCHAR(50) UNIQUE NOT NULL,
//email VARCHAR(100) UNIQUE NOT NULL,
//phone VARCHAR(20) NOT NULL,
//password VARCHAR(255) NOT NULL  -- Consider using hashed passwords
//);




