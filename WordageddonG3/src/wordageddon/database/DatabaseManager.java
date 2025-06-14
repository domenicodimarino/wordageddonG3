package wordageddon.database;

import wordageddon.util.PathUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestisce la connessione e l'inizializzazione del database SQLite per l'applicazione Wordageddon.
 * Fornisce metodi per la creazione delle tabelle principali e per la gestione della connessione.
 */
public class DatabaseManager {
    
    private static final String URL = "jdbc:sqlite:" + PathUtils.getDataFilePath("wordageddon.db");
   
    private static Connection connection;

    /**
     * Inizializza il database e crea le tabelle principali se non esistono già.
     * Le tabelle includono: utente, sessione e punteggio.
     *
     * @throws SQLException se si verifica un errore SQL durante la creazione delle tabelle.
     */
    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createUtente = "CREATE TABLE IF NOT EXISTS utente (" +
                                  "username TEXT PRIMARY KEY," +
                                  "password TEXT NOT NULL," +
                                  "ruolo TEXT NOT NULL DEFAULT 'USER'"+
                                  ");";
            stmt.execute(createUtente);

            String createSessione = "CREATE TABLE IF NOT EXISTS sessione (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "data_inizio TEXT," +
                "data_fine TEXT," +
                "punteggio_totale INTEGER," +
                "tempo_residuo INTEGER,"+
                "stato_gioco_json TEXT," +
                "stato TEXT," +
                "difficolta TEXT," +
                "lingua TEXT," +
                "FOREIGN KEY(username) REFERENCES utente(username)" +
                ");";
            stmt.execute(createSessione);

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

    /**
     * Restituisce una connessione al database SQLite.
     * Se non esiste una connessione attiva, ne crea una nuova.
     *
     * @return la connessione al database.
     * @throws SQLException se si verifica un errore nella connessione.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    /**
     * Chiude la connessione al database se è aperta.
     *
     * @throws SQLException se si verifica un errore durante la chiusura della connessione.
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}