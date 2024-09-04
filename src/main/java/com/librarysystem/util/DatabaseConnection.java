package com.librarysystem.util;

import com.librarysystem.exception.LibrarySystemException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "/database.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static Connection getConnection() throws LibrarySystemException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new LibrarySystemException("Failed to connect to the database", e);
        }
    }
}