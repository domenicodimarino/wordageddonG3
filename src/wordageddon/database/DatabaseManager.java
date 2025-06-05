/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author domenicodimarino
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:wordageddon.db";
    private static Connection connection;
    
    public static void initializeDatabase() throws SQLException {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        // Esempio tabella Utente
        String createUtente = "CREATE TABLE IF NOT EXISTS utente (" +
                              "username TEXT PRIMARY KEY," +
                              "password TEXT NOT NULL" +
                              ");";
        stmt.execute(createUtente);

        // Altre tabelle...
    }
}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
