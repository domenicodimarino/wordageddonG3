package wordageddon.database;

import wordageddon.model.Sessione;
import java.util.List;

/**
 * Interfaccia DAO (Data Access Object) per la gestione delle sessioni di gioco nel database di Wordageddon.
 * Fornisce i metodi principali per inserire, recuperare, aggiornare ed eliminare le sessioni.
 */
public interface SessioneDAO {
    /**
     * Inserisce una nuova sessione nel database.
     *
     * @param s la sessione da inserire
     * @throws Exception se si verifica un errore durante l'inserimento
     */
    void insertSessione(Sessione s) throws Exception;

    /**
     * Restituisce una lista di tutte le sessioni presenti nel database.
     *
     * @return lista di sessioni
     * @throws Exception se si verifica un errore durante la lettura dei dati
     */
    List<Sessione> elencaTutte() throws Exception;

    /**
     * Cancella tutte le sessioni dal database.
     *
     * @throws Exception se si verifica un errore durante la cancellazione
     */
    void cancellaTutte() throws Exception;

    /**
     * Recupera una sessione dal database tramite il suo identificativo.
     *
     * @param id identificativo della sessione
     * @return la sessione corrispondente all'id, o null se non trovata
     * @throws Exception se si verifica un errore durante la ricerca
     */
    Sessione getSessioneById(int id) throws Exception;

    /**
     * Elimina una sessione dal database tramite il suo identificativo.
     *
     * @param id identificativo della sessione da eliminare
     * @throws Exception se si verifica un errore durante l'eliminazione
     */
    void deleteSessioneById(int id) throws Exception;

    /**
     * Aggiorna i dati di una sessione esistente nel database.
     *
     * @param s la sessione aggiornata
     * @throws Exception se si verifica un errore durante l'aggiornamento
     */
    void updateSessione(Sessione s) throws Exception;

    /**
     * Corregge eventuali stati delle sessioni impostandoli a "finito" dove necessario.
     *
     * @throws Exception se si verifica un errore durante la correzione degli stati
     */
    void correzioneStatoFinito() throws Exception;
}