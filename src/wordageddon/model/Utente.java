package wordageddon.model;

/**
 * Rappresenta un utente dell'applicazione, con username, hash della password e ruolo.
 */
public class Utente {

    /**
     * Username dell'utente.
     */
    private String username;

    /**
     * Hash della password dell'utente.
     */
    private String passwordHash;

    /**
     * Ruolo dell'utente (USER o ADMIN).
     */
    private RuoloUtente ruolo;

    /**
     * Costruttore della classe Utente.
     *
     * @param username      username dell'utente
     * @param passwordHash  hash della password dell'utente
     * @param ruolo         ruolo dell'utente (USER o ADMIN)
     */
    public Utente(String username, String passwordHash, RuoloUtente ruolo) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
    }

    /**
     * Restituisce lo username dell'utente.
     *
     * @return username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta lo username dell'utente.
     *
     * @param username nuovo username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce l'hash della password dell'utente.
     *
     * @return hash della password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Imposta l'hash della password dell'utente.
     *
     * @param passwordHash nuovo hash della password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Restituisce il ruolo dell'utente.
     *
     * @return ruolo dell'utente
     */
    public RuoloUtente getRuolo() {
        return ruolo;
    }

    /**
     * Restituisce true se l'utente ha il ruolo di amministratore.
     *
     * @return true se l'utente è ADMIN, false altrimenti
     */
    public boolean isAdmin() {
        return ruolo == RuoloUtente.ADMIN;
    }
}