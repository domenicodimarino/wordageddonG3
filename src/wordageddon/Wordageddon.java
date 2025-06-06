package wordageddon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Wordageddon extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Resources/fxml/login.fxml"));
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Wordageddon");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Resources/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        
    }
}
