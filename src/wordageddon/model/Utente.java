package wordageddon.model;


//utente dell'applicazione
public class Utente {
    private String username;
    private String passwordHash;

    public Utente(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getter e Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}