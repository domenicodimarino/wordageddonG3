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


public class Wordageddon extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Resources/fxml/login.fxml"));
        Scene scene = new Scene(root, 900, 600);
        
        scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/login.css").toExternalForm());
        stage.setTitle("Wordageddon");
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            DatabaseManager.initializeDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(Wordageddon.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
        
    }
}
