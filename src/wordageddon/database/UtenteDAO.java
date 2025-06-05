package wordageddon.database;

import wordageddon.model.Utente;
import java.util.List;

public interface UtenteDAO {
    void inserisci(Utente u) throws Exception;
    Utente cercaPerUsername(String username) throws Exception;
    List<Utente> elencaTutti() throws Exception;
    void cancellaTutti() throws Exception;
}