package wordageddon.model;

import java.util.List;

/**
 * Rappresenta lo stato corrente del gioco, includendo la lista delle domande,
 * le risposte fornite dall'utente e l'indice della domanda attualmente in corso.
 */
public class StatoGioco {

    /**
     * Lista delle domande del gioco.
     */
    private List<Domanda> domande;

    /**
     * Lista delle risposte fornite dall'utente durante la sessione di gioco.
     */
    private List<RispostaUtente> risposteUtente;

    /**
     * Indice della domanda attualmente in corso.
     */
    private int domandaCorrente;

    /**
     * Costruttore della classe StatoGioco.
     *
     * @param domande           lista delle domande del gioco
     * @param risposteUtente    lista delle risposte dell'utente
     * @param domandaCorrente   indice della domanda corrente
     */
    public StatoGioco(List<Domanda> domande, List<RispostaUtente> risposteUtente, int domandaCorrente) {
        this.domande = domande;
        this.risposteUtente = risposteUtente;
        this.domandaCorrente = domandaCorrente;
    }

    /**
     * Restituisce la lista delle domande del gioco.
     *
     * @return lista delle domande
     */
    public List<Domanda> getDomande() { return domande; }

    /**
     * Restituisce la lista delle risposte fornite dall'utente.
     *
     * @return lista delle risposte dell'utente
     */
    public List<RispostaUtente> getRisposteUtente() { return risposteUtente; }

    /**
     * Restituisce l'indice della domanda attualmente in corso.
     *
     * @return indice della domanda corrente
     */
    public int getDomandaCorrente() { return domandaCorrente; }
}