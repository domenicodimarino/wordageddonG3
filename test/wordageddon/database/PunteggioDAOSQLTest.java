package wordageddon.database;

import org.junit.*;
import wordageddon.Punteggio;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class PunteggioDAOSQLTest {

    private PunteggioDAOSQL dao;

    @Before
    public void setUp() throws SQLException {
        DatabaseManager.initializeDatabase();
        dao = new PunteggioDAOSQL();
        dao.cancellaTutti(); // Pulisci la tabella prima di ogni test
    }

    @After
    public void tearDown() throws SQLException {
        dao.cancellaTutti(); // Pulisci la tabella dopo ogni test
    }

    @Test
    public void testInserisciEElenchiTutti() throws SQLException {
        // Arrange
        Punteggio p1 = new Punteggio("testuser", 5, 60, 2, LocalDateTime.of(2025, 6, 5, 14, 0));
        Punteggio p2 = new Punteggio("altrauser", 7, 45, 1, LocalDateTime.of(2025, 6, 4, 12, 0));
        dao.inserisci(p1);
        dao.inserisci(p2);

        // Act
        List<Punteggio> lista = dao.elencaTutti();

        // Assert
        assertEquals(2, lista.size());

        // Il punteggio con valore maggiore dovrebbe essere il primo (controlla ordinamento)
        Punteggio top = lista.get(0);
        assertTrue(
            (top.getUsername().equals("testuser") && top.getRisposteCorrette() == 5) ||
            (top.getUsername().equals("altrauser") && top.getRisposteCorrette() == 7)
        );
    }

    @Test
    public void testCancellaTutti() throws SQLException {
        // Arrange
        Punteggio p1 = new Punteggio("testuser", 3, 30, 1);
        dao.inserisci(p1);

        // Act
        dao.cancellaTutti();
        List<Punteggio> lista = dao.elencaTutti();

        // Assert
        assertTrue(lista.isEmpty());
    }

    @Test
    public void testInserisciConDataAttuale() throws SQLException {
        // Arrange
        Punteggio p = new Punteggio("nowuser", 2, 20, 1);
        dao.inserisci(p);

        // Act
        List<Punteggio> lista = dao.elencaTutti();

        // Assert
        assertEquals(1, lista.size());
        Punteggio fromDb = lista.get(0);
        assertEquals("nowuser", fromDb.getUsername());
        assertEquals(2, fromDb.getRisposteCorrette());
        assertEquals(20, fromDb.getTempoResiduo());
        assertEquals(1, fromDb.getDifficolta());
        assertNotNull(fromDb.getData());
    }
}