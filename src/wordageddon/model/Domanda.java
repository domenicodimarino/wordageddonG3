package wordageddon.model;

import java.util.List;

/**
 * Rappresenta una domanda a risposta multipla.
 * Contiene il testo della domanda, le opzioni di risposta, la risposta corretta e il tipo di domanda.
 * 
 * @author Gruppo 3
 */
public class Domanda {

    /**
     * Testo della domanda.
     */
    private final String testo;

    /**
     * Lista delle opzioni di risposta.
     */
    private final List<String> opzioni;

    /**
     * Risposta corretta alla domanda.
     */
    private final String rispostaCorretta;

    /**
     * Tipo della domanda.
     */
    private final String tipo; 

    /**
     * Costruttore della classe Domanda.
     * 
     * @param testo testo della domanda
     * @param opzioni lista delle opzioni di risposta
     * @param rispostaCorretta risposta corretta alla domanda
     * @param tipo tipo della domanda
     */
    public Domanda(String testo, List<String> opzioni, String rispostaCorretta, String tipo) {
        this.testo = testo;
        this.opzioni = opzioni;
        this.rispostaCorretta = rispostaCorretta;
        this.tipo = tipo;
    }

    /**
     * Restituisce il testo della domanda.
     * 
     * @return testo della domanda
     */
    public String getTesto() { return testo; }

    /**
     * Restituisce la lista delle opzioni di risposta.
     * 
     * @return lista delle opzioni di risposta
     */
    public List<String> getOpzioni() { return opzioni; }

    /**
     * Restituisce la risposta corretta alla domanda.
     * 
     * @return risposta corretta
     */
    public String getRispostaCorretta() { return rispostaCorretta; }

    /**
     * Restituisce il tipo della domanda.
     * 
     * @return tipo della domanda
     */
    public String getTipo() { return tipo; }
}