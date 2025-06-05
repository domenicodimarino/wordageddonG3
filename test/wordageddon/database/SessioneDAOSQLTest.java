package wordageddon.database;

import org.junit.*; // JUnit 4
import static org.junit.Assert.*;
import wordageddon.model.Sessione;

import java.util.List;

public class SessioneDAOSQLTest {
    private SessioneDAOSQL dao;

    @Before
    public void setUp() throws Exception {
        DatabaseManager.initializeDatabase();
        dao = new SessioneDAOSQL();
        dao.cancellaTutte();
    }

    @Test
    public void testInsertSessioneAndGetById() throws Exception {
        Sessione s = new Sessione(
                "pippo", "2025-06-05T13:00", null, 123,
                2, 60, "{}", "in_corso"
        );
        dao.insertSessione(s);
        assertTrue(s.getId() > 0);

        Sessione sFound = dao.getSessioneById(s.getId());
        assertNotNull(sFound);
        assertEquals("pippo", sFound.getUsername());
        assertEquals(123, sFound.getPunteggioTotale());
        assertEquals("in_corso", sFound.getStato());
    }

    @Test
    public void testElencaTutte() throws Exception {
        Sessione s1 = new Sessione("pippo", "2025-06-05T13:00", null, 100, 1, 80, "{}", "in_corso");
        Sessione s2 = new Sessione("pluto", "2025-06-05T14:00", null, 150, 2, 70, "{}", "finita");
        dao.insertSessione(s1);
        dao.insertSessione(s2);

        List<Sessione> tutte = dao.elencaTutte();
        assertEquals(2, tutte.size());
        assertEquals("pippo", tutte.get(0).getUsername());
        assertEquals("pluto", tutte.get(1).getUsername());
    }

    @Test
    public void testCancellaTutte() throws Exception {
        Sessione s = new Sessione("pippo", "2025-06-05T13:00", null, 100, 1, 80, "{}", "in_corso");
        dao.insertSessione(s);
        assertFalse(dao.elencaTutte().isEmpty());

        dao.cancellaTutte();
        assertTrue(dao.elencaTutte().isEmpty());
    }

    @Test
    public void testGetSessioneByIdNotFound() throws Exception {
        Sessione result = dao.getSessioneById(99999); // ID sicuramente inesistente
        assertNull(result);
    }
}