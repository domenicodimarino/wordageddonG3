package wordageddon.model;

/**
 * Rappresenta la risposta fornita dall'utente a una specifica domanda durante una sessione di gioco.
 */
public class RispostaUtente {

    /**
     * Indice della domanda a cui si riferisce la risposta.
     */
    private final int domandaIndex;

    /**
     * Testo della domanda.
     */
    private final String testoDomanda;

    /**
     * Risposta fornita dall'utente.
     */
    private final String rispostaData;

    /**
     * Risposta corretta per la domanda.
     */
    private final String rispostaCorretta;

    /**
     * Indica se la risposta fornita dall'utente è corretta.
     */
    private final boolean esatto;

    /**
     * Costruisce una nuova istanza di {@code RispostaUtente}.
     *
     * @param domandaIndex     indice della domanda
     * @param testoDomanda     testo della domanda
     * @param rispostaData     risposta fornita dall'utente
     * @param rispostaCorretta risposta corretta
     * @param esatto           true se la risposta è corretta, false altrimenti
     */
    public RispostaUtente(int domandaIndex, String testoDomanda, String rispostaData, String rispostaCorretta, boolean esatto) {
        this.domandaIndex = domandaIndex;
        this.testoDomanda = testoDomanda;
        this.rispostaData = rispostaData;
        this.rispostaCorretta = rispostaCorretta;
        this.esatto = esatto;
    }

    /**
     * Restituisce l'indice della domanda.
     *
     * @return indice della domanda
     */
    public int getDomandaIndex() {
        return domandaIndex;
    }

    /**
     * Restituisce il testo della domanda.
     *
     * @return testo della domanda
     */
    public String getTestoDomanda() {
        return testoDomanda;
    }

    /**
     * Restituisce la risposta fornita dall'utente.
     *
     * @return risposta data dall'utente
     */
    public String getRispostaData() {
        return rispostaData;
    }

    /**
     * Restituisce la risposta corretta.
     *
     * @return risposta corretta
     */
    public String getRispostaCorretta() {
        return rispostaCorretta;
    }

    /**
     * Indica se la risposta fornita dall'utente è corretta.
     *
     * @return true se la risposta è corretta, false altrimenti
     */
    public boolean isEsatto() {
        return esatto;
    }

    /**
     * Restituisce la scelta effettuata dall'utente (alias di {@link #getRispostaData()}).
     *
     * @return scelta dell'utente
     */
    public String getSceltaUtente() {
        return rispostaData;
    }
}