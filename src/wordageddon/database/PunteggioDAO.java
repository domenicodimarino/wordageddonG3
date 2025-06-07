/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon.database;

import java.sql.SQLException;
import java.util.List;
import wordageddon.model.Punteggio;

/**
 *
 * @author Gruppo 3
 */
public interface PunteggioDAO {
    public void inserisci(Punteggio p) throws SQLException;
    public List<Punteggio> elencaTutti() throws SQLException;
    public void cancellaTutti() throws SQLException;
}
