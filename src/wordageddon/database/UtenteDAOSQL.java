package wordageddon.database;

import wordageddon.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wordageddon.model.RuoloUtente;

public class UtenteDAOSQL implements UtenteDAO {

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