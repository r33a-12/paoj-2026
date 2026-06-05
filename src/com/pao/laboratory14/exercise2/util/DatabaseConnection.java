package com.pao.laboratory14.exercise2.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton care gestioneaza conexiunea la baza de date.
 * Citeste configuratia din db.properties de pe classpath.
 *
 * Configurare IntelliJ: marcheaza 'exercise2/resources/' ca Resources Root.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                // Fallback direct din fișier dacă folderul nu e marcat ca Resources Root
                try (InputStream fs = new java.io.FileInputStream("src/com/pao/laboratory14/exercise2/resources/db.properties")) {
                    props.load(fs);
                } catch (Exception e) {
                    throw new IOException("Nu am putut gasi db.properties nici pe classpath, nici in folderul src/com/pao/laboratory14/exercise2/resources/.", e);
                }
            }
        }
        String url      = props.getProperty("db.url");
        String user     = props.getProperty("db.user", "");
        String password = props.getProperty("db.password", "");
        
        // Asigură-te că directorul părinte există, pentru a preveni erori dacă 'output' lipsește
        if (url != null && url.startsWith("jdbc:sqlite:")) {
            String path = url.substring("jdbc:sqlite:".length());
            java.io.File dbFile = new java.io.File(path);
            if (dbFile.getParentFile() != null && !dbFile.getParentFile().exists()) {
                dbFile.getParentFile().mkdirs();
            }
        }
        
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public static DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

