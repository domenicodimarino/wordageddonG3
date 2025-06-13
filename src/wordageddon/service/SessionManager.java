package wordageddon.service;

import wordageddon.model.Lingua;
import wordageddon.model.Sessione;
import wordageddon.model.Utente;


public class SessionManager {
    private static Utente utente;
    private static Sessione sessione;
    private static Lingua linguaScelta;

    public static void setSessione(Sessione s) { sessione = s; }
    public static Sessione getSessione() { return sessione; }
    public static void clearSessione() { sessione = null; }
    public static Lingua getLinguaScelta() { return linguaScelta; }
    public static void setLinguaScelta(Lingua lingua) { linguaScelta = lingua; }
    
    public static void setUtente(Utente u) {
        utente = u;
    }

    
    public static Utente getUtente() {
        return utente;
    }

    
    public static boolean isLoggedIn() {
        return utente != null;
    }

    
    public static void logout() {
        utente = null;
    }
}