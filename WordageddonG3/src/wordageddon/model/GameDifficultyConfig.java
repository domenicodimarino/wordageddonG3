package wordageddon.model;

/**
 * Rappresenta la configurazione dei parametri di gioco per un determinato livello di difficoltà.
 */
public class GameDifficultyConfig {

    /**
     * Numero di documenti previsti per la difficoltà.
     */
    private int numDocumenti;

    /**
     * Lunghezza del testo da utilizzare per la difficoltà.
     */
    private int lunghezzaTesto;

    /**
     * Tempo di lettura assegnato (in secondi) per la difficoltà.
     */
    private int tempoLettura; 

    /**
     * Costruisce una configurazione di difficoltà con i parametri specificati.
     *
     * @param numDocumenti   numero di documenti
     * @param lunghezzaTesto lunghezza del testo
     * @param tempoLettura   tempo di lettura assegnato (in secondi)
     */
    public GameDifficultyConfig(int numDocumenti, int lunghezzaTesto, int tempoLettura) {
        this.numDocumenti = numDocumenti;
        this.lunghezzaTesto = lunghezzaTesto;
        this.tempoLettura = tempoLettura;
    }

    /**
     * Restituisce il numero di documenti previsti per la difficoltà.
     *
     * @return numero di documenti
     */
    public int getNumDocumenti() {
        return numDocumenti;
    }

    /**
     * Restituisce la lunghezza del testo prevista per la difficoltà.
     *
     * @return lunghezza del testo
     */
    public int getLunghezzaTesto() {
        return lunghezzaTesto;
    }

    /**
     * Restituisce il tempo di lettura assegnato (in secondi) per la difficoltà.
     *
     * @return tempo di lettura in secondi
     */
    public int getTempoLettura() {
        return tempoLettura;
    }
}