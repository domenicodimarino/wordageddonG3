package wordageddon.database;

import wordageddon.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wordageddon.model.RuoloUtente;

/**
 * Implementazione dell'interfaccia {@link UtenteDAO} che gestisce le operazioni CRUD
 * sugli utenti nel database tramite SQL.
 */
public class UtenteDAOSQL implements UtenteDAO {

    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param u l'utente da inserire
     * @throws Exception se si verifica un errore durante l'inserimento
     */
    @Override
    public void inserisci(Utente u) throws Exception {
        String sql = "INSERT INTO utente (username, password, ruolo) VALUES (?, ?, ?)";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRuolo().toString());
            ps.executeUpdate();
        }
    }

    /**
     * Cerca e restituisce un utente dal database in base allo username.
     *
     * @param username lo username da cercare
     * @return l'utente corrispondente allo username, o null se non trovato
     * @throws Exception se si verifica un errore durante la ricerca
     */
    @Override
    public Utente cercaPerUsername(String username) throws Exception {
        String sql = "SELECT * FROM utente WHERE username = ?";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utente(
                        rs.getString("username"),
                        rs.getString("password"),
                        RuoloUtente.valueOf(rs.getString("ruolo"))
                    );
                }
            }
        }
        return null;
    }

    /**
     * Restituisce una lista di tutti gli utenti presenti nel database, ordinati per username crescente.
     *
     * @return lista di utenti
     * @throws Exception se si verifica un errore durante il recupero dei dati
     */
    @Override
    public List<Utente> elencaTutti() throws Exception {
        List<Utente> lista = new ArrayList<>();
        String sql = "SELECT * FROM utente ORDER BY username ASC";
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                lista.add(new Utente(
                    rs.getString("username"),
                    rs.getString("password"),
                    RuoloUtente.valueOf(rs.getString("ruolo"))
                ));
            }
        }
        return lista;
    }

    /**
     * Cancella tutti gli utenti dal database.
     *
     * @throws Exception se si verifica un errore durante la cancellazione
     */
    @Override
    public void cancellaTutti() throws Exception {
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement()
        ) {
            st.executeUpdate("DELETE FROM utente;");
        }
    }
}