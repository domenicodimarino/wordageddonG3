/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author ferdi
 */
public class QuizController implements Initializable {

    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label timer;
    @FXML
    private Button nextBtn;
    @FXML
    private RadioButton option1Btn;
    @FXML
    private ToggleGroup optionsGroup;
    @FXML
    private RadioButton option2Btn;
    @FXML
    private RadioButton option3Btn;
    @FXML
    private RadioButton option4Btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
