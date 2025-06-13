package wordageddon.model;

import java.util.List;
/**
 *
 * @author Gruppo 3
 * Rappresenta una domanda a risposta multipla.
 */
public class Domanda {
    private final String testo;
    private final List<String> opzioni;
    private final String rispostaCorretta;
    private final String tipo; 

    public Domanda(String testo, List<String> opzioni, String rispostaCorretta, String tipo) {
        this.testo = testo;
        this.opzioni = opzioni;
        this.rispostaCorretta = rispostaCorretta;
        this.tipo = tipo;
    }

    public String getTesto() { return testo; }
    public List<String> getOpzioni() { return opzioni; }
    public String getRispostaCorretta() { return rispostaCorretta; }
    public String getTipo() { return tipo; }
}