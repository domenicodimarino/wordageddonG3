package wordageddon;

public class Sessione {
    private int id;
    private String username;
    private String dataInizio;
    private String dataFine;
    private int punteggioTotale;

    public Sessione(int id, String username, String dataInizio, String dataFine, int punteggioTotale) {
        this.id = id;
        this.username = username;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.punteggioTotale = punteggioTotale;
    }

    // Costruttore senza id (per inserimento)
    public Sessione(String username, String dataInizio, String dataFine, int punteggioTotale) {
        this(-1, username, dataInizio, dataFine, punteggioTotale);
    }

    // Getter e setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDataInizio() { return dataInizio; }
    public void setDataInizio(String dataInizio) { this.dataInizio = dataInizio; }

    public String getDataFine() { return dataFine; }
    public void setDataFine(String dataFine) { this.dataFine = dataFine; }

    public int getPunteggioTotale() { return punteggioTotale; }
    public void setPunteggioTotale(int punteggioTotale) { this.punteggioTotale = punteggioTotale; }
}