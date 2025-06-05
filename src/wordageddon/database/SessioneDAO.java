package wordageddon.database;

import wordageddon.Sessione;
import java.util.List;

public interface SessioneDAO {
    void insertSessione(Sessione s) throws Exception;
    List<Sessione> elencaTutte() throws Exception;
    void cancellaTutte() throws Exception;
    Sessione getSessioneById(int id) throws Exception;
}