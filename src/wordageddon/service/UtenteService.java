package wordageddon.service;

import wordageddon.model.Utente;
import wordageddon.database.UtenteDAO;
import wordageddon.model.RuoloUtente;

public class UtenteService {
    private UtenteDAO utenteDAO;

    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    
    public boolean registra(String username, String passwordChiara) throws Exception {
        if (utenteDAO.cercaPerUsername(username) != null) {
            return false; 
        }
        String hash = PasswordUtils.hash(passwordChiara);
        Utente nuovo = new Utente(username, hash, RuoloUtente.USER);
        utenteDAO.inserisci(nuovo);
        return true;
    }

   
    public boolean creaAdmin(String username, String passwordChiara) throws Exception {
        if (utenteDAO.cercaPerUsername(username) != null) {
            return false; 
        }
        String hash = PasswordUtils.hash(passwordChiara);
        Utente nuovo = new Utente(username, hash, RuoloUtente.ADMIN);
        utenteDAO.inserisci(nuovo);
        return true;
    }

   
    public boolean login(String username, String passwordChiara) throws Exception {
        Utente user = utenteDAO.cercaPerUsername(username);
        if (user == null) return false;
        String hash = PasswordUtils.hash(passwordChiara);
        return hash.equals(user.getPasswordHash());
    }
    public Utente getUtente(String username) throws Exception {
        return utenteDAO.cercaPerUsername(username);
    }
}