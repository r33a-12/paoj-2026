package com.pao.proiect.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Singleton pentru conexiunea la baza de date.
 * Citește configurația din resources/db.properties.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private String url;
    private String user;
    private String password;

    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("com/pao/proiect/elearning/resources/db.properties");

            if (input == null) {
                // Fallback: încearcă să citească relativ la clasă
                input = getClass().getResourceAsStream("/com/pao/proiect/elearning/resources/db.properties");
            }

            if (input == null) {
                throw new RuntimeException("Nu s-a gasit fisierul db.properties in classpath!");
            }

            props.load(input);
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");

            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("[DB] Conexiune stabilita cu succes la: " + url);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Eroare la conectarea la baza de date: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Reconectare dacă conexiunea a fost închisă
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("[DB] Reconectare reusita.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la reconectare: " + e.getMessage(), e);
        }
        return connection;
    }

    public void resetDatabase() {
        try {
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("com/pao/proiect/elearning/resources/schema.sql");
            if (input == null) {
                input = getClass().getResourceAsStream("/com/pao/proiect/elearning/resources/schema.sql");
            }
            if (input == null) {
                System.err.println("[DB] Nu s-a putut incarca schema.sql pentru resetare.");
                return;
            }
            String sqlScript = new String(input.readAllBytes());
            String[] queries = sqlScript.split(";");
            try (Statement stmt = getConnection().createStatement()) {
                for (String query : queries) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query);
                    }
                }
                System.out.println("[DB] Baza de date a fost resetata la starea initiala (schema.sql).");
            }
        } catch (Exception e) {
            System.err.println("[DB] Eroare la resetarea bazei de date: " + e.getMessage());
        }
    }
}
