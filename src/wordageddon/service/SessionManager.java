package wordageddon.service;

import wordageddon.model.Utente;

/**
 * Classe di utilità per gestire la sessione utente corrente.
 * Offre metodi statici per impostare, recuperare e resettare l'utente loggato.
 */
public class SessionManager {
    private static Utente utente;

    /**
     * Imposta l'utente attualmente loggato.
     * @param u utente autenticato
     */
    public static void setUtente(Utente u) {
        utente = u;
    }

    /**
     * Restituisce l'utente attualmente loggato, o null se non autenticato.
     * @return l'utente loggato oppure null
     */
    public static Utente getUtente() {
        return utente;
    }

    /**
     * Verifica se un utente è autenticato.
     * @return true se loggato, false altrimenti
     */
    public static boolean isLoggedIn() {
        return utente != null;
    }

    /**
     * Esegue il logout dell'utente corrente.
     */
    public static void logout() {
        utente = null;
    }
}