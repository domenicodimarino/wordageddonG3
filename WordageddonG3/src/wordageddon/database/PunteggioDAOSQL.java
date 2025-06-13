package wordageddon.database;

import wordageddon.model.Punteggio;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione SQL dell'interfaccia {@link PunteggioDAO} per la gestione dei punteggi nel database.
 * Fornisce metodi per inserire, elencare e cancellare i punteggi nella tabella 'punteggio'.
 */
public class PunteggioDAOSQL implements PunteggioDAO {
   
    private Connection conn;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Costruttore che apre una connessione al database tramite {@link DatabaseManager}.
     *
     * @throws SQLException se si verifica un errore di connessione al database
     */
    public PunteggioDAOSQL() throws SQLException {
        conn = DatabaseManager.getConnection();
    }

    /**
     * Inserisce un nuovo punteggio nella tabella 'punteggio'.
     *
     * @param p l'oggetto {@link Punteggio} da inserire
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
    @Override
    public void inserisci(Punteggio p) throws SQLException {
        String sql = "INSERT INTO punteggio (username, valore, risposteCorrette, tempoResiduo, difficolta, data) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            ps.setInt(2, p.getValore());
            ps.setInt(3, p.getRisposteCorrette());
            ps.setInt(4, p.getTempoResiduo());
            ps.setInt(5, p.getDifficolta());
            ps.setString(6, p.getData().format(FORMATTER));
            ps.executeUpdate();
        }
    }

    /**
     * Restituisce una lista di tutti i punteggi presenti nel database,
     * ordinati per valore decrescente.
     *
     * @return lista di {@link Punteggio}
     * @throws SQLException se si verifica un errore durante la lettura dei dati
     */
    @Override
    public List<Punteggio> elencaTutti() throws SQLException {
        List<Punteggio> lista = new ArrayList<>();
        String sql = "SELECT username, risposteCorrette, tempoResiduo, difficolta, data FROM punteggio ORDER BY valore DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String username = rs.getString("username");
                int risposteCorrette = rs.getInt("risposteCorrette");
                int tempoResiduo = rs.getInt("tempoResiduo");
                int difficolta = rs.getInt("difficolta");
                String dataStr = rs.getString("data");
                LocalDateTime data = LocalDateTime.parse(dataStr, FORMATTER);
                Punteggio p = new Punteggio(username, risposteCorrette, tempoResiduo, difficolta, data);
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Cancella tutti i record dalla tabella 'punteggio'.
     *
     * @throws SQLException se si verifica un errore durante la cancellazione
     */
    @Override
    public void cancellaTutti() throws SQLException {
        String sql = "DELETE FROM punteggio";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        }
    }
}