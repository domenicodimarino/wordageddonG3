package wordageddon.model;


//utente dell'applicazione
public class Utente {
    private String username;
    private String passwordHash;
    private RuoloUtente ruolo;

    public Utente(String username, String passwordHash, RuoloUtente ruolo) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public RuoloUtente getRuolo() {
        return ruolo;
    }
    
    public boolean isAdmin(){
        return ruolo == RuoloUtente.ADMIN;
    }
}
