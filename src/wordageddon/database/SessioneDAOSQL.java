package wordageddon.database;

import wordageddon.model.Sessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wordageddon.model.Difficolta;
import wordageddon.model.Lingua;

/**
 * Implementazione dell'interfaccia {@link SessioneDAO} che gestisce le operazioni CRUD
 * sulle sessioni di gioco nel database tramite SQL.
 */
public class SessioneDAOSQL implements SessioneDAO {

    /**
     * Inserisce una nuova sessione nel database.
     *
     * @param s la sessione da inserire
     * @throws Exception se si verifica un errore durante l'inserimento
     */
    @Override
    public void insertSessione(Sessione s) throws Exception {
        String sql = "INSERT INTO sessione (username, data_inizio, data_fine, punteggio_totale, tempo_residuo, stato_gioco_json, stato, difficolta, lingua) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, s.getUsername());
            ps.setString(2, s.getDataInizio());
            ps.setString(3, s.getDataFine());
            ps.setInt(4, s.getPunteggioTotale());
            ps.setInt(5, s.getTempoResiduo());
            ps.setString(6, s.getStatoGiocoJson());
            ps.setString(7, s.getStato());
            ps.setString(8, s.getDifficolta().toString());
            ps.setString(9, s.getLingua().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }

    /**
     * Restituisce la lista di tutte le sessioni presenti nel database, ordinate per data di inizio crescente.
     *
     * @return lista di tutte le sessioni
     * @throws Exception se si verifica un errore durante il recupero dei dati
     */
    @Override
    public List<Sessione> elencaTutte() throws Exception {
        List<Sessione> tutte = new ArrayList<>();
        String sql = "SELECT * FROM sessione ORDER BY data_inizio ASC";
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                tutte.add(new Sessione(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("data_inizio"),
                    rs.getString("data_fine"),
                    rs.getInt("punteggio_totale"),
                    rs.getInt("tempo_residuo"),
                    rs.getString("stato_gioco_json"),
                    rs.getString("stato"),
                    Difficolta.valueOf(rs.getString("difficolta")),
                    Lingua.valueOf(rs.getString("lingua"))
                ));
            }
        }
        return tutte;
    }

    /**
     * Cancella tutte le sessioni dal database.
     *
     * @throws Exception se si verifica un errore durante la cancellazione
     */
    @Override
    public void cancellaTutte() throws Exception {
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement()
        ) {
            st.executeUpdate("DELETE FROM sessione;");
        }
    }

    /**
     * Recupera una sessione dal database tramite il suo identificativo.
     *
     * @param id identificativo della sessione
     * @return la sessione corrispondente all'id, o null se non trovata
     * @throws Exception se si verifica un errore durante la ricerca
     */
    @Override
    public Sessione getSessioneById(int id) throws Exception {
        String sql = "SELECT * FROM sessione WHERE id = ?";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sessione(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("data_inizio"),
                        rs.getString("data_fine"),
                        rs.getInt("punteggio_totale"),
                        rs.getInt("tempo_residuo"),
                        rs.getString("stato_gioco_json"),
                        rs.getString("stato"),
                        Difficolta.valueOf(rs.getString("difficolta")),
                        Lingua.valueOf(rs.getString("lingua"))
                    );
                }
            }
        }
        return null;
    }

    /**
     * Elimina una sessione dal database tramite il suo identificativo.
     *
     * @param id identificativo della sessione da eliminare
     * @throws Exception se si verifica un errore durante l'eliminazione
     */
    @Override
    public void deleteSessioneById(int id) throws Exception {
        String sql = "DELETE FROM sessione WHERE id = ?";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Aggiorna i dati di una sessione esistente nel database.
     *
     * @param s la sessione aggiornata
     * @throws Exception se si verifica un errore durante l'aggiornamento
     */
    @Override
    public void updateSessione(Sessione s) throws Exception {
        String sql = "UPDATE sessione SET data_fine=?, punteggio_totale=?, tempo_residuo=?, stato=?, stato_gioco_json=? WHERE id=?";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, s.getDataFine());
            ps.setInt(2, s.getPunteggioTotale());
            ps.setInt(3, s.getTempoResiduo());
            ps.setString(4, s.getStato());
            ps.setString(5, s.getStatoGiocoJson());
            ps.setInt(6, s.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Imposta lo stato "finita" per tutte le sessioni che hanno una data di fine ma non sono già segnate come finite.
     *
     * @throws Exception se si verifica un errore durante l'aggiornamento degli stati
     */
    @Override
    public void correzioneStatoFinito() throws Exception {
        String sql = "UPDATE sessione SET stato = 'finita' WHERE data_fine IS NOT NULL AND stato <> 'finita'";
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement()
        ) {
            st.executeUpdate(sql);
        }
    }
}