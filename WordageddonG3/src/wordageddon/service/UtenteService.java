package wordageddon.service;

import wordageddon.model.Utente;
import wordageddon.database.UtenteDAO;
import wordageddon.model.RuoloUtente;

/**
 * Servizio per la gestione delle operazioni relative agli utenti,
 * come registrazione, login, creazione di amministratori e recupero delle informazioni utente.
 */
public class UtenteService {

    /**
     * Oggetto DAO per l'accesso ai dati degli utenti.
     */
    private UtenteDAO utenteDAO;

    /**
     * Costruttore della classe UtenteService.
     *
     * @param utenteDAO DAO per la gestione degli utenti
     */
    public UtenteService(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Registra un nuovo utente con ruolo USER.
     *
     * @param username        username scelto dall'utente
     * @param passwordChiara  password in chiaro fornita dall'utente
     * @return true se la registrazione è andata a buon fine, false se l'username è già esistente
     * @throws Exception se si verifica un errore durante l'accesso ai dati o l'hashing della password
     */
    public boolean registra(String username, String passwordChiara) throws Exception {
        if (utenteDAO.cercaPerUsername(username) != null) {
            return false; 
        }
        String hash = PasswordUtils.hash(passwordChiara);
        Utente nuovo = new Utente(username, hash, RuoloUtente.USER);
        utenteDAO.inserisci(nuovo);
        return true;
    }

    /**
     * Crea un nuovo utente con ruolo ADMIN.
     *
     * @param username        username scelto per l'admin
     * @param passwordChiara  password in chiaro fornita per l'admin
     * @return true se la creazione è andata a buon fine, false se l'username è già esistente
     * @throws Exception se si verifica un errore durante l'accesso ai dati o l'hashing della password
     */
    public boolean creaAdmin(String username, String passwordChiara) throws Exception {
        if (utenteDAO.cercaPerUsername(username) != null) {
            return false; 
        }
        String hash = PasswordUtils.hash(passwordChiara);
        Utente nuovo = new Utente(username, hash, RuoloUtente.ADMIN);
        utenteDAO.inserisci(nuovo);
        return true;
    }

    /**
     * Esegue il login di un utente verificando username e password.
     *
     * @param username        username dell'utente
     * @param passwordChiara  password in chiaro fornita dall'utente
     * @return true se le credenziali sono corrette, false altrimenti
     * @throws Exception se si verifica un errore durante l'accesso ai dati o l'hashing della password
     */
    public boolean login(String username, String passwordChiara) throws Exception {
        Utente user = utenteDAO.cercaPerUsername(username);
        if (user == null) return false;
        String hash = PasswordUtils.hash(passwordChiara);
        return hash.equals(user.getPasswordHash());
    }

    /**
     * Recupera le informazioni di un utente dato l'username.
     *
     * @param username username dell'utente da cercare
     * @return l'oggetto Utente corrispondente, o null se non esiste
     * @throws Exception se si verifica un errore durante l'accesso ai dati
     */
    public Utente getUtente(String username) throws Exception {
        return utenteDAO.cercaPerUsername(username);
    }
}