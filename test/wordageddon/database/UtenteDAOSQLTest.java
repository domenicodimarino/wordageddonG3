package wordageddon.database;

import org.junit.*;
import static org.junit.Assert.*;
import wordageddon.Utente;

import java.util.List;

public class UtenteDAOSQLTest {
    private UtenteDAOSQL dao;

    @Before
    public void setUp() throws Exception {
        DatabaseManager.initializeDatabase();
        dao = new UtenteDAOSQL();
        dao.cancellaTutti();
    }

    @Test
    public void testInserisciAndCercaPerUsername() throws Exception {
        Utente u = new Utente("pippo", "hashPippo");
        dao.inserisci(u);

        Utente trovato = dao.cercaPerUsername("pippo");
        assertNotNull(trovato);
        assertEquals("pippo", trovato.getUsername());
        assertEquals("hashPippo", trovato.getPasswordHash());
    }

    @Test
    public void testElencaTutti() throws Exception {
        Utente u1 = new Utente("pippo", "hashPippo");
        Utente u2 = new Utente("pluto", "hashPluto");
        dao.inserisci(u1);
        dao.inserisci(u2);

        List<Utente> utenti = dao.elencaTutti();
        assertEquals(2, utenti.size());
        // Puoi fare anche il controllo sul contenuto
        assertEquals("pippo", utenti.get(0).getUsername());
        assertEquals("pluto", utenti.get(1).getUsername());
    }

    @Test
    public void testCancellaTutti() throws Exception {
        Utente u = new Utente("pippo", "hashPippo");
        dao.inserisci(u);
        assertFalse(dao.elencaTutti().isEmpty());

        dao.cancellaTutti();
        assertTrue(dao.elencaTutti().isEmpty());
    }

    @Test
    public void testCercaPerUsernameNotFound() throws Exception {
        Utente trovato = dao.cercaPerUsername("non_esiste");
        assertNull(trovato);
    }
}