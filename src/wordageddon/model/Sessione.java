package wordageddon.model;

public class Sessione {
    private int id;
    private String username;
    private String dataInizio;
    private String dataFine;
    private int punteggioTotale;
    private int tempoResiduo; // secondi
    private String statoGiocoJson; // tutto il resto serializzato
    private String stato; // "in_corso" oppure "finita"
    private Difficolta difficolta;

    public Sessione(int id, String username, String dataInizio, String dataFine, int punteggioTotale, int tempoResiduo, String statoGiocoJson, String stato, Difficolta difficolta) {
        this.id = id;
        this.username = username;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.punteggioTotale = punteggioTotale;
        this.tempoResiduo = tempoResiduo;
        this.statoGiocoJson = statoGiocoJson;
        this.stato = stato;
        this.difficolta = difficolta;
    }

    // Costruttore senza id (per inserimento)
    public Sessione(String username, String dataInizio, String dataFine, int punteggioTotale, int tempoResiduo, String statoGiocoJson, String stato, Difficolta difficolta) {
        this(-1, username, dataInizio, dataFine, punteggioTotale, tempoResiduo, statoGiocoJson, stato, difficolta);
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

    public Difficolta getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }
    
}