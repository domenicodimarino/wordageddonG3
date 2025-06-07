package wordageddon.model;

public class RispostaUtente {
    private final String testoDomanda;
    private final String rispostaData;
    private final String rispostaCorretta;
    private final boolean esatto; // true = risposta corretta

    public RispostaUtente(String testoDomanda, String rispostaData, String rispostaCorretta, boolean esatto) {
        this.testoDomanda = testoDomanda;
        this.rispostaData = rispostaData;
        this.rispostaCorretta = rispostaCorretta;
        this.esatto = esatto;
    }

    public String getTestoDomanda() {
        return testoDomanda;
    }
    public String getRispostaData() {
        return rispostaData;
    }
    public String getRispostaCorretta() {
        return rispostaCorretta;
    }
    public boolean isEsatto() {
        return esatto;
    }
}