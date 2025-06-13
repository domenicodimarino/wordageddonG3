package wordageddon.service;

import wordageddon.model.Lingua;
import wordageddon.model.Sessione;
import wordageddon.model.Utente;

/**
 * Classe di utilità per la gestione della sessione utente nell'applicazione.
 * Permette di salvare e recuperare le informazioni relative all'utente autenticato,
 * alla sessione di gioco attiva e alla lingua scelta.
 */
public class SessionManager {

    /**
     * Utente attualmente autenticato nell'applicazione.
     */
    private static Utente utente;

    /**
     * Sessione di gioco attiva.
     */
    private static Sessione sessione;

    /**
     * Lingua scelta dall'utente per la sessione corrente.
     */
    private static Lingua linguaScelta;

    /**
     * Imposta la sessione di gioco attiva.
     *
     * @param s la sessione da impostare come attiva
     */
    public static void setSessione(Sessione s) {
        sessione = s;
    }

    /**
     * Restituisce la sessione di gioco attiva.
     *
     * @return la sessione attiva, o null se non esiste
     */
    public static Sessione getSessione() {
        return sessione;
    }

    /**
     * Cancella la sessione di gioco attiva.
     */
    public static void clearSessione() {
        sessione = null;
    }

    /**
     * Restituisce la lingua attualmente scelta dall'utente.
     *
     * @return la lingua scelta, o null se non impostata
     */
    public static Lingua getLinguaScelta() {
        return linguaScelta;
    }

    /**
     * Imposta la lingua scelta dall'utente.
     *
     * @param lingua la lingua da impostare come scelta
     */
    public static void setLinguaScelta(Lingua lingua) {
        linguaScelta = lingua;
    }

    /**
     * Imposta l'utente attualmente autenticato nell'applicazione.
     *
     * @param u l'utente da impostare come autenticato
     */
    public static void setUtente(Utente u) {
        utente = u;
    }

    /**
     * Restituisce l'utente attualmente autenticato.
     *
     * @return l'utente autenticato, o null se nessun utente è autenticato
     */
    public static Utente getUtente() {
        return utente;
    }

    /**
     * Verifica se esiste un utente autenticato.
     *
     * @return true se un utente è autenticato, false altrimenti
     */
    public static boolean isLoggedIn() {
        return utente != null;
    }

    /**
     * Effettua il logout dell'utente corrente, rimuovendo le informazioni dell'utente autenticato.
     */
    public static void logout() {
        utente = null;
    }
}