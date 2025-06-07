/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gruppo 3
 */
public class ChooseDifficultyController implements Initializable {

    @FXML
    private Label labelSignInUp;
    @FXML
    private Button easyBtn;
    @FXML
    private Button mediumBtn;
    @FXML
    private Button hardBtn;
    @FXML
    private Button menuBtn;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBtn.setOnAction(e -> goToMenu());
    }
    private void goToMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Wordageddon.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
