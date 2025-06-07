/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import wordageddon.model.Utente;
import wordageddon.util.DialogUtils;

/**
 * FXML Controller class
 *
 * @author Gruppo 3
 */
public class WordageddonController implements Initializable {

    @FXML
    private ImageView logoImage;
    @FXML
    private Label appNameLabel;
    @FXML
    private Button storicoBtn;
    @FXML
    private ComboBox<?> linguaCombo;
    @FXML
    private Button leaderboardBtn;
    @FXML
    private MenuButton userMenu;
    @FXML
    private MenuItem impostazioniMenu;
    @FXML
    private MenuItem logoutMenu;
    @FXML
    private StackPane mainStackPane;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;
    
    private Utente utente;
    @FXML
    private Button playBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoutMenu.setOnAction(e -> logout());
        playBtn.setOnAction(e -> startGame());
    }
    private void logout(){
        ButtonType yes = new ButtonType("Sì");
        ButtonType no = new ButtonType("No");
        Optional<ButtonType> result = DialogUtils.showCustomAlert(
            Alert.AlertType.CONFIRMATION,
            "Conferma logout",
            "Sei sicuro?",
            "Vuoi effettuare il logout?",
            yes, no
        );

        if (result.isPresent() && result.get() == yes) {
            goToLogin();
        }
    }
    private void goToLogin(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
        // Aggiorna la GUI se serve!
    }
    private void startGame(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/ChooseDifficulty.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
