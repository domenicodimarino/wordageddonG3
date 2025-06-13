package wordageddon.model;

/**
 * Rappresenta una sessione di gioco di un utente.
 * Contiene informazioni sull'utente, sulle tempistiche, sul punteggio, sullo stato e sulle impostazioni di difficoltà e lingua della sessione.
 */
public class Sessione {

    /**
     * Identificativo univoco della sessione.
     */
    private int id;

    /**
     * Username dell'utente che ha avviato la sessione.
     */
    private String username;

    /**
     * Data e ora di inizio della sessione (formato stringa).
     */
    private String dataInizio;

    /**
     * Data e ora di fine della sessione (formato stringa).
     */
    private String dataFine;

    /**
     * Punteggio totale ottenuto nella sessione.
     */
    private int punteggioTotale;

    /**
     * Tempo residuo rimasto alla fine della sessione.
     */
    private int tempoResiduo;

    /**
     * Stato del gioco serializzato in formato JSON.
     */
    private String statoGiocoJson;

    /**
     * Stato corrente della sessione (es. "in corso", "finita").
     */
    private String stato;

    /**
     * Difficoltà della sessione.
     */
    private Difficolta difficolta;

    /**
     * Lingua della sessione.
     */
    private Lingua lingua;

    /**
     * Costruttore completo della classe Sessione.
     *
     * @param id               identificativo della sessione
     * @param username         username dell'utente
     * @param dataInizio       data e ora di inizio
     * @param dataFine         data e ora di fine
     * @param punteggioTotale  punteggio totale della sessione
     * @param tempoResiduo     tempo residuo alla fine della sessione
     * @param statoGiocoJson   stato del gioco in formato JSON
     * @param stato            stato corrente della sessione
     * @param difficolta       difficoltà della sessione
     * @param lingua           lingua della sessione
     */
    public Sessione(int id, String username, String dataInizio, String dataFine, int punteggioTotale, int tempoResiduo, String statoGiocoJson, String stato, Difficolta difficolta, Lingua lingua) {
        this.id = id;
        this.username = username;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.punteggioTotale = punteggioTotale;
        this.tempoResiduo = tempoResiduo;
        this.statoGiocoJson = statoGiocoJson;
        this.stato = stato;
        this.difficolta = difficolta;
        this.lingua = lingua;
    }

    /**
     * Costruttore senza id della sessione (id impostato a -1).
     *
     * @param username         username dell'utente
     * @param dataInizio       data e ora di inizio
     * @param dataFine         data e ora di fine
     * @param punteggioTotale  punteggio totale della sessione
     * @param tempoResiduo     tempo residuo alla fine della sessione
     * @param statoGiocoJson   stato del gioco in formato JSON
     * @param stato            stato corrente della sessione
     * @param difficolta       difficoltà della sessione
     * @param lingua           lingua della sessione
     */
    public Sessione(String username, String dataInizio, String dataFine, int punteggioTotale, int tempoResiduo, String statoGiocoJson, String stato, Difficolta difficolta, Lingua lingua) {
        this(-1, username, dataInizio, dataFine, punteggioTotale, tempoResiduo, statoGiocoJson, stato, difficolta, lingua);
    }

    /**
     * Restituisce l'id della sessione.
     *
     * @return id della sessione
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'id della sessione.
     *
     * @param id nuovo id della sessione
     */
    public void setId(int id) {
        this.id = id;
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
     * @param username nuovo username dell'utente
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce la data e ora di inizio della sessione.
     *
     * @return data di inizio sessione
     */
    public String getDataInizio() {
        return dataInizio;
    }

    /**
     * Imposta la data e ora di inizio della sessione.
     *
     * @param dataInizio nuova data di inizio sessione
     */
    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * Restituisce la data e ora di fine della sessione.
     *
     * @return data di fine sessione
     */
    public String getDataFine() {
        return dataFine;
    }

    /**
     * Imposta la data e ora di fine della sessione.
     *
     * @param dataFine nuova data di fine sessione
     */
    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * Restituisce il punteggio totale ottenuto nella sessione.
     *
     * @return punteggio totale
     */
    public int getPunteggioTotale() {
        return punteggioTotale;
    }

    /**
     * Imposta il punteggio totale ottenuto nella sessione.
     *
     * @param punteggioTotale nuovo punteggio totale
     */
    public void setPunteggioTotale(int punteggioTotale) {
        this.punteggioTotale = punteggioTotale;
    }

    /**
     * Restituisce il tempo residuo rimasto alla fine della sessione.
     *
     * @return tempo residuo
     */
    public int getTempoResiduo() {
        return tempoResiduo;
    }

    /**
     * Imposta il tempo residuo rimasto alla fine della sessione.
     *
     * @param tempoResiduo nuovo tempo residuo
     */
    public void setTempoResiduo(int tempoResiduo) {
        this.tempoResiduo = tempoResiduo;
    }

    /**
     * Restituisce lo stato del gioco in formato JSON.
     *
     * @return stato del gioco JSON
     */
    public String getStatoGiocoJson() {
        return statoGiocoJson;
    }

    /**
     * Imposta lo stato del gioco in formato JSON.
     *
     * @param statoGiocoJson nuovo stato del gioco JSON
     */
    public void setStatoGiocoJson(String statoGiocoJson) {
        this.statoGiocoJson = statoGiocoJson;
    }

    /**
     * Restituisce lo stato corrente della sessione.
     * Se la sessione ha una data di fine, restituisce "finita".
     *
     * @return stato della sessione
     */
    public String getStato() {
        if (dataFine != null && !dataFine.isEmpty()) {
            return "finita";
        }
        return stato;
    }

    /**
     * Imposta lo stato corrente della sessione.
     *
     * @param stato nuovo stato della sessione
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la difficoltà della sessione.
     *
     * @return difficoltà della sessione
     */
    public Difficolta getDifficolta() {
        return difficolta;
    }

    /**
     * Imposta la difficoltà della sessione.
     *
     * @param difficolta nuova difficoltà della sessione
     */
    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * Restituisce la lingua della sessione.
     *
     * @return lingua della sessione
     */
    public Lingua getLingua() {
        return lingua;
    }

    /**
     * Imposta la lingua della sessione.
     *
     * @param lingua nuova lingua della sessione
     */
    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }
}