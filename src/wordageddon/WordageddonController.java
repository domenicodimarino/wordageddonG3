/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import wordageddon.model.RuoloUtente;
import wordageddon.model.Utente;
import wordageddon.service.SessionManager;
import wordageddon.util.DialogUtils;
import wordageddon.util.SceneUtils;

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
    @FXML
    private MenuItem pannelloAdminMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoutMenu.setOnAction(e -> logout());
        playBtn.setOnAction(e -> startGame());
        leaderboardBtn.setOnAction(e -> goToLeaderboard());
        storicoBtn.setOnAction(e -> apriStorico());
        
        utente = SessionManager.getUtente();
       
        if (utente.getRuolo().equals(RuoloUtente.ADMIN)) {
            pannelloAdminMenu.setVisible(true);
        } else {
            pannelloAdminMenu.setVisible(false);
        }
        
        pannelloAdminMenu.setOnAction(e -> goToAdminPanel());
        
        
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
            SessionManager.logout(); 
            goToLogin();
        }
    }
    private void goToLogin(){
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/login.fxml", "/wordageddon/Resources/css/login.css");
    }
    private void apriStorico() {
        SceneUtils.switchScene(storicoBtn, "/wordageddon/Resources/fxml/Storico.fxml", "/wordageddon/Resources/css/style.css");
}
    private void startGame(){
        SceneUtils.switchScene(playBtn, "/wordageddon/Resources/fxml/ChooseDifficulty.fxml", "/wordageddon/Resources/css/style.css");
    }
    private void goToLeaderboard() {
        SceneUtils.switchScene(leaderboardBtn, "/wordageddon/Resources/fxml/Classifica.fxml", "/wordageddon/Resources/css/style.css");
    }
    private void goToAdminPanel(){
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/AdminPanel.fxml", "/wordageddon/Resources/css/style.css");
    }
    
}
