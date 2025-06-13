package wordageddon.database;

import wordageddon.model.Utente;
import java.util.List;

/**
 * Interfaccia DAO (Data Access Object) per la gestione degli utenti nel database di Wordageddon.
 * Fornisce i metodi principali per inserire, cercare, elencare ed eliminare utenti.
 */
public interface UtenteDAO {
    /**
     * Inserisce un nuovo utente nel database.
     *
     * @param u l'utente da inserire
     * @throws Exception se si verifica un errore durante l'inserimento
     */
    void inserisci(Utente u) throws Exception;

    /**
     * Cerca e restituisce un utente in base al suo username.
     *
     * @param username lo username da cercare
     * @return l'utente corrispondente allo username, o null se non trovato
     * @throws Exception se si verifica un errore durante la ricerca
     */
    Utente cercaPerUsername(String username) throws Exception;

    /**
     * Restituisce una lista di tutti gli utenti presenti nel database.
     *
     * @return lista di utenti
     * @throws Exception se si verifica un errore durante la lettura dei dati
     */
    List<Utente> elencaTutti() throws Exception;

    /**
     * Cancella tutti gli utenti dal database.
     *
     * @throws Exception se si verifica un errore durante la cancellazione
     */
    void cancellaTutti() throws Exception;
}