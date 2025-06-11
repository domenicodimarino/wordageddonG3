package wordageddon.model;

import java.util.List;

public class StatoGioco {
    private List<Domanda> domande;
    private List<RispostaUtente> risposteUtente;
    private int domandaCorrente;

    public StatoGioco(List<Domanda> domande, List<RispostaUtente> risposteUtente, int domandaCorrente) {
        this.domande = domande;
        this.risposteUtente = risposteUtente;
        this.domandaCorrente = domandaCorrente;
    }

    public List<Domanda> getDomande() { return domande; }
    public List<RispostaUtente> getRisposteUtente() { return risposteUtente; }
    public int getDomandaCorrente() { return domandaCorrente; }
}