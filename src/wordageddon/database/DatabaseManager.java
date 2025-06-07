package wordageddon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Gruppo 3
 */
public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:wordageddon.db";
    private static Connection connection;
    
    public static void initializeDatabase() throws SQLException {
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement()) {
        // Tabella Utente
        String createUtente = "CREATE TABLE IF NOT EXISTS utente (" +
                              "username TEXT PRIMARY KEY," +
                              "password TEXT NOT NULL," +
                              "ruolo TEXT NOT NULL DEFAULT 'USER'"+
                              ");";
        stmt.execute(createUtente);

        // Tabella Sessione
        String createSessione = "CREATE TABLE IF NOT EXISTS sessione (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "data_inizio TEXT," +
            "data_fine TEXT," +
            "punteggio_totale INTEGER," +
            "livello_corrente INTEGER,"+
            "tempo_residuo INTEGER,"+
            "stato_gioco_json TEXT," +
            "stato TEXT," +
            "FOREIGN KEY(username) REFERENCES utente(username)" +
            ");";
        stmt.execute(createSessione);

        // Tabella Punteggio
        String createPunteggio = "CREATE TABLE IF NOT EXISTS punteggio (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "valore INTEGER NOT NULL," +
            "risposteCorrette INTEGER," +
            "tempoResiduo INTEGER," +
            "difficolta INTEGER," +
            "data TEXT," +
            "FOREIGN KEY(username) REFERENCES utente(username)" +
            ");";
        stmt.execute(createPunteggio);
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