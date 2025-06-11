package wordageddon.model;

public class RispostaUtente {
    private final int domandaIndex;           // Posizione della domanda nel quiz
    private final String testoDomanda;        // Testo completo della domanda
    private final String rispostaData;        // Risposta scelta dall'utente
    private final String rispostaCorretta;    // Risposta corretta
    private final boolean esatto;             // true = risposta corretta

    public RispostaUtente(int domandaIndex, String testoDomanda, String rispostaData, String rispostaCorretta, boolean esatto) {
        this.domandaIndex = domandaIndex;
        this.testoDomanda = testoDomanda;
        this.rispostaData = rispostaData;
        this.rispostaCorretta = rispostaCorretta;
        this.esatto = esatto;
    }

    public int getDomandaIndex() {
        return domandaIndex;
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

    // Puoi usare questo alias se "scelta utente" ti serve altrove con quel nome
    public String getSceltaUtente() {
        return rispostaData;
    }
}