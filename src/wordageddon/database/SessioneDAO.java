package wordageddon.database;

import wordageddon.model.Sessione;
import java.util.List;

public interface SessioneDAO {
    void insertSessione(Sessione s) throws Exception;
    List<Sessione> elencaTutte() throws Exception;
    void cancellaTutte() throws Exception;
    Sessione getSessioneById(int id) throws Exception;
    public void deleteSessioneById(int id) throws Exception;
    public void updateSessione(Sessione s) throws Exception;
}