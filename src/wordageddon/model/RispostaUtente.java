package wordageddon.model;

public class RispostaUtente {
    private final int domandaIndex;          
    private final String testoDomanda;       
    private final String rispostaData;        
    private final String rispostaCorretta;   
    private final boolean esatto;            

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

        public String getSceltaUtente() {
        return rispostaData;
    }
}