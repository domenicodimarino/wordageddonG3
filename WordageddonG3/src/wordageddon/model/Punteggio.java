package wordageddon.model;

import java.time.LocalDateTime;

/**
 * Rappresenta il punteggio ottenuto da un utente in una sessione di gioco.
 * Contiene informazioni sull'utente, il valore del punteggio, il numero di risposte corrette,
 * il tempo residuo, la difficoltà e la data in cui è stato ottenuto il punteggio.
 */
public class Punteggio {

    /**
     * Username dell'utente che ha ottenuto il punteggio.
     */
    private String username;

    /**
     * Valore totale del punteggio calcolato.
     */
    private int valore;

    /**
     * Numero di risposte corrette date dall'utente.
     */
    private int risposteCorrette;

    /**
     * Tempo residuo al termine della partita (in secondi).
     */
    private int tempoResiduo;

    /**
     * Livello di difficoltà numerico (1 = facile, 2 = medio, 3 = difficile).
     */
    private int difficolta;

    /**
     * Data e ora in cui è stato ottenuto il punteggio.
     */
    private LocalDateTime data;

    /**
     * Costruisce un oggetto Punteggio con tutti i parametri specificati,
     * calcolando il valore del punteggio in base alle risposte corrette, al tempo residuo e alla difficoltà.
     *
     * @param username          username dell'utente
     * @param risposteCorrette  numero di risposte corrette
     * @param tempoResiduo      tempo residuo (in secondi)
     * @param difficolta        livello di difficoltà (1, 2 o 3)
     * @param data              data e ora del punteggio
     */
    public Punteggio(String username, int risposteCorrette, int tempoResiduo, int difficolta, LocalDateTime data) {
        this.username = username;
        this.risposteCorrette = risposteCorrette;
        this.tempoResiduo = tempoResiduo;
        this.difficolta = difficolta;
        this.data = data;
        this.valore = calcolaPunteggio();
    }

    /**
     * Costruisce un oggetto Punteggio con la data corrente.
     * 
     * @param username          username dell'utente
     * @param risposteCorrette  numero di risposte corrette
     * @param tempoResiduo      tempo residuo (in secondi)
     * @param difficolta        livello di difficoltà (1, 2 o 3)
     */
    public Punteggio(String username, int risposteCorrette, int tempoResiduo, int difficolta) {
        this(username, risposteCorrette, tempoResiduo, difficolta, LocalDateTime.now());
    }

    /**
     * Calcola il valore del punteggio sulla base delle risposte corrette, della difficoltà e del tempo residuo.
     * 
     * @return valore del punteggio calcolato
     */
    private int calcolaPunteggio() {
        return (risposteCorrette * 30) * difficolta + (risposteCorrette > 0 ? tempoResiduo : 0);
    }

    /**
     * Restituisce l'username dell'utente.
     *
     * @return username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Imposta l'username dell'utente.
     *
     * @param username nuovo username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Restituisce il valore totale del punteggio.
     *
     * @return valore del punteggio
     */
    public int getValore() {
        return valore;
    }

    /**
     * Imposta il valore totale del punteggio.
     *
     * @param valore nuovo valore del punteggio
     */
    public void setValore(int valore) {
        this.valore = valore;
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
     * Imposta il numero di risposte corrette.
     *
     * @param risposteCorrette nuovo numero di risposte corrette
     */
    public void setRisposteCorrette(int risposteCorrette) {
        this.risposteCorrette = risposteCorrette;
    }

    /**
     * Restituisce il tempo residuo.
     *
     * @return tempo residuo in secondi
     */
    public int getTempoResiduo() {
        return tempoResiduo;
    }

    /**
     * Imposta il tempo residuo.
     *
     * @param tempoResiduo nuovo tempo residuo in secondi
     */
    public void setTempoResiduo(int tempoResiduo) {
        this.tempoResiduo = tempoResiduo;
    }

    /**
     * Restituisce il livello di difficoltà numerico.
     *
     * @return livello di difficoltà (1, 2 o 3)
     */
    public int getDifficolta() {
        return difficolta;
    }

    /**
     * Imposta il livello di difficoltà numerico.
     *
     * @param difficolta nuovo livello di difficoltà (1, 2 o 3)
     */
    public void setDifficolta(int difficolta) {
        this.difficolta = difficolta;
    }

    /**
     * Restituisce la data e l'ora in cui è stato ottenuto il punteggio.
     *
     * @return data e ora del punteggio
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * Imposta la data e l'ora del punteggio.
     *
     * @param data nuova data e ora del punteggio
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * Restituisce la difficoltà come enumerazione {@link Difficolta} in base al valore numerico.
     *
     * @return difficoltà come {@link Difficolta}, o null se il valore non è valido
     */
    public Difficolta getDifficoltaEnum() {
        switch (difficolta) {
            case 1: return Difficolta.FACILE;
            case 2: return Difficolta.MEDIO;
            case 3: return Difficolta.DIFFICILE;
            default: return null;
        }
    }
}