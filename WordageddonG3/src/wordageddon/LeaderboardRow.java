package wordageddon;

import wordageddon.model.Difficolta;

/**
 * Rappresenta una singola riga della classifica (leaderboard) di Wordageddon.
 * Contiene informazioni sulla posizione, username, numero di risposte corrette,
 * difficoltà della partita e punteggio totale.
 */
public class LeaderboardRow {
    private final int posizione;
    private final String username;
    private final int risposteCorrette;
    private final String difficolta;
    private final int punteggio;

    /**
     * Costruisce una nuova riga della leaderboard con i dati forniti.
     *
     * @param posizione posizione in classifica
     * @param username nome utente
     * @param risposteCorrette numero di risposte corrette
     * @param difficolta livello di difficoltà della partita
     * @param punteggio punteggio totale ottenuto
     */
    public LeaderboardRow(int posizione, String username, int risposteCorrette, Difficolta difficolta, int punteggio) {
        this.posizione = posizione;
        this.username = username;
        this.risposteCorrette = risposteCorrette;
        this.difficolta = difficolta.toString();
        this.punteggio = punteggio;
    }

    /**
     * Restituisce la posizione dell'utente in classifica.
     *
     * @return posizione in classifica
     */
    public int getPosizione() {
        return posizione;
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
     * Restituisce il numero di risposte corrette.
     *
     * @return numero di risposte corrette
     */
    public int getRisposteCorrette() {
        return risposteCorrette;
    }

    /**
     * Restituisce la difficoltà della partita come stringa.
     *
     * @return difficoltà della partita
     */
    public String getDifficolta() {
        return difficolta;
    }

    /**
     * Restituisce il punteggio totale ottenuto dall'utente.
     *
     * @return punteggio totale
     */
    public int getPunteggio() {
        return punteggio;
    }
}