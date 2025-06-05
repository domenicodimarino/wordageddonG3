package wordageddon.service;

import wordageddon.PasswordUtils;
import wordageddon.Utente;
import wordageddon.database.UtenteDAO;

public class UtenteService {
    private UtenteDAO utenteDAO;

    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    // REGISTRAZIONE
    public boolean registra(String username, String passwordChiara) throws Exception {
        // Controlla se esiste già
        if (utenteDAO.cercaPerUsername(username) != null) {
            return false; // Username già in uso
        }
        String hash = PasswordUtils.hash(passwordChiara);
        Utente nuovo = new Utente(username, hash);
        utenteDAO.inserisci(nuovo);
        return true;
    }

    // LOGIN
    public boolean login(String username, String passwordChiara) throws Exception {
        Utente user = utenteDAO.cercaPerUsername(username);
        if (user == null) return false;
        String hash = PasswordUtils.hash(passwordChiara);
        return hash.equals(user.getPasswordHash());
    }
}