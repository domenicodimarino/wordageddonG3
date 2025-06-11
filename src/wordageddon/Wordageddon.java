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


public class Wordageddon extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Resources/fxml/login.fxml"));
        Scene scene = new Scene(root, 900, 600);
        
        scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/login.css").toExternalForm());
        stage.setTitle("Wordageddon");
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/WordageddonG3.jpeg")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            DatabaseManager.initializeDatabase();
            ensureAdminUserExists();
        } catch (SQLException ex) {
            Logger.getLogger(Wordageddon.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Wordageddon.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
        
    }
    // Da chiamare una sola volta all'avvio, al login o all'apertura dell'app principale
    public static void ensureAdminUserExists() throws Exception {
        UtenteDAO dao = new UtenteDAOSQL();
        UtenteService utenteservice = new UtenteService(dao);
        Utente admin = dao.cercaPerUsername("admin");
        if (admin == null) {        
            utenteservice.creaAdmin("admin","admin");
        }
    }
}
