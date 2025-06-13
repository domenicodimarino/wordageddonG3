package wordageddon;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import wordageddon.database.DatabaseManager;
import wordageddon.database.UtenteDAO;
import wordageddon.database.UtenteDAOSQL;
import wordageddon.model.RuoloUtente;
import wordageddon.model.Utente;
import wordageddon.service.UtenteService;

/**
 * Classe principale dell'applicazione Wordageddon.
 * Si occupa dell'inizializzazione del database, della verifica dell'utente admin
 * e dell'avvio della GUI tramite JavaFX.
 */
public class Wordageddon extends Application {

    /**
     * Metodo di avvio dell'applicazione JavaFX.
     * Carica la schermata di login tramite FXML, imposta la scena e configura la finestra principale.
     *
     * @param stage Lo stage principale fornito da JavaFX.
     * @throws Exception Se si verifica un errore durante il caricamento delle risorse o della scena.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Resources/fxml/login.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/login.css").toExternalForm());
        stage.setTitle("Wordageddon");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/wordageddon_icon.png")));
        stage.setScene(scene);
        stage.setResizable(false); 
        stage.show();
    }

    /**
     * Metodo main dell'applicazione.
     * Inizializza il driver JDBC, il database e verifica l'esistenza dell'utente admin prima di avviare l'applicazione JavaFX.
     *
     * @param args Argomenti passati da linea di comando.
     */
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            DatabaseManager.initializeDatabase();
            ensureAdminUserExists();
        } catch (SQLException ex) {
            Logger.getLogger(Wordageddon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Wordageddon.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
    }

    /**
     * Verifica che l'utente admin esista nel database.
     * Se non esiste, crea un nuovo utente admin con username e password predefiniti ("admin", "admin").
     *
     * @throws Exception Se si verifica un errore durante l'accesso al database o nella creazione dell'utente.
     */
    public static void ensureAdminUserExists() throws Exception {
        UtenteDAO dao = new UtenteDAOSQL();
        UtenteService utenteservice = new UtenteService(dao);
        Utente admin = dao.cercaPerUsername("admin");
        if (admin == null) {
            utenteservice.creaAdmin("admin", "admin");
        }
    }
}