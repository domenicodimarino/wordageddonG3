package wordageddon.database;

import java.sql.SQLException;
import java.util.List;
import wordageddon.model.Punteggio;

/**
 * Interfaccia DAO (Data Access Object) per la gestione delle operazioni
 * CRUD sui punteggi nel database di Wordageddon.
 *
 * @author Gruppo 3
 */
public interface PunteggioDAO {
    /**
     * Inserisce un nuovo punteggio nel database.
     *
     * @param p oggetto Punteggio da inserire
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
    public void inserisci(Punteggio p) throws SQLException;

    /**
     * Elenca tutti i punteggi presenti nel database.
     *
     * @return lista di oggetti Punteggio
     * @throws SQLException se si verifica un errore nella lettura dei dati
     */
    public List<Punteggio> elencaTutti() throws SQLException;

    /**
     * Cancella tutti i punteggi dal database.
     *
     * @throws SQLException se si verifica un errore durante la cancellazione
     */
    public void cancellaTutti() throws SQLException;
}