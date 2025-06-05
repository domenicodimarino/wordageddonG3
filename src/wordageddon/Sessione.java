package wordageddon;

public class Sessione {
    private int id;
    private String username;
    private String dataInizio;
    private String dataFine;
    private int punteggioTotale;
    private int livelloCorrente;
    private int tempoResiduo; // secondi
    private String statoGiocoJson; // tutto il resto serializzato
    private String stato; // "in_corso" oppure "finita"

    public Sessione(int id, String username, String dataInizio, String dataFine, int punteggioTotale, int livelloCorrente, int tempoResiduo, String statoGiocoJson, String stato) {
        this.id = id;
        this.username = username;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.punteggioTotale = punteggioTotale;
        this.livelloCorrente = livelloCorrente;
        this.tempoResiduo = tempoResiduo;
        this.statoGiocoJson = statoGiocoJson;
        this.stato = stato;
    }

    // Costruttore senza id (per inserimento)
    public Sessione(String username, String dataInizio, String dataFine, int punteggioTotale, int livelloCorrente, int tempoResiduo, String statoGiocoJson, String stato) {
        this(-1, username, dataInizio, dataFine, punteggioTotale, livelloCorrente, tempoResiduo, statoGiocoJson, stato);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public int getPunteggioTotale() {
        return punteggioTotale;
    }

    public void setPunteggioTotale(int punteggioTotale) {
        this.punteggioTotale = punteggioTotale;
    }

    public int getLivelloCorrente() {
        return livelloCorrente;
    }

    public void setLivelloCorrente(int livelloCorrente) {
        this.livelloCorrente = livelloCorrente;
    }

    public int getTempoResiduo() {
        return tempoResiduo;
    }

    public void setTempoResiduo(int tempoResiduo) {
        this.tempoResiduo = tempoResiduo;
    }

    public String getStatoGiocoJson() {
        return statoGiocoJson;
    }

    public void setStatoGiocoJson(String statoGiocoJson) {
        this.statoGiocoJson = statoGiocoJson;
    } 

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
    
}