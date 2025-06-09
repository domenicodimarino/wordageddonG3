package wordageddon.database;

import wordageddon.model.Sessione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wordageddon.model.Difficolta;

public class SessioneDAOSQL implements SessioneDAO {

    @Override
    public void insertSessione(Sessione s) throws Exception {
        String sql = "INSERT INTO sessione (username, data_inizio, data_fine, punteggio_totale, tempo_residuo, stato_gioco_json, stato, difficolta) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }

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
                    Difficolta.valueOf(rs.getString("difficolta"))
                ));
            }
        }
        return tutte;
    }

    @Override
    public void cancellaTutte() throws Exception {
        try (
            Connection c = DatabaseManager.getConnection();
            Statement st = c.createStatement()
        ) {
            st.executeUpdate("DELETE FROM sessione;");
        }
    }

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
                    Difficolta.valueOf(rs.getString("difficolta"))
                    );
                }
            }
        }
        return null;
    }
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
    @Override
    public void updateSessione(Sessione s) throws Exception {
        String sql = "UPDATE sessione SET data_fine=?, punteggio_totale=?, tempo_residuo=?, stato=? WHERE id=?";
        try (
            Connection c = DatabaseManager.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)
        ) {
            ps.setString(1, s.getDataFine());
            ps.setInt(2, s.getPunteggioTotale());
            ps.setInt(3, s.getTempoResiduo());
            ps.setString(4, s.getStato());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        }
    }
}