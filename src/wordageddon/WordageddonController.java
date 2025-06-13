/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Lingua;
import wordageddon.model.RuoloUtente;
import wordageddon.model.Sessione;
import wordageddon.model.StatoGioco;
import wordageddon.model.Utente;
import wordageddon.service.SessionManager;
import wordageddon.util.DialogUtils;
import wordageddon.util.JsonParserManuale;
import wordageddon.util.SceneUtils;

/**
 * 
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
    @FXML
    private ToggleGroup quizLanguageGroup;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            new SessioneDAOSQL().correzioneStatoFinito();
        } catch (Exception ex) {
            Logger.getLogger(WordageddonController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
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
    private Lingua getLinguaSelezionata() {
        RadioMenuItem selected = (RadioMenuItem) quizLanguageGroup.getSelectedToggle();
        return Lingua.fromString(selected.getText());
    }
    private void goToLogin(){
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/login.fxml", "/wordageddon/Resources/css/login.css");
    }
    private void apriStorico() {
        SceneUtils.switchScene(storicoBtn, "/wordageddon/Resources/fxml/Storico.fxml", "/wordageddon/Resources/css/style.css");
}
    private void startGame() {
        try {
            SessioneDAOSQL dao = new SessioneDAOSQL();
            List<Sessione> sessioni = dao.elencaTutte();

            for (Sessione s : sessioni) {
                if (s.getUsername().equals(utente.getUsername()) && 
                    "in_corso".equals(s.getStato()) &&
                    s.getStatoGiocoJson() != null && !s.getStatoGiocoJson().isEmpty()) {

                    ButtonType riprendi = new ButtonType("Riprendi");
                    ButtonType nuova = new ButtonType("Nuova partita", ButtonBar.ButtonData.CANCEL_CLOSE);
                    Optional<ButtonType> scelta = DialogUtils.showCustomAlert(
                        Alert.AlertType.CONFIRMATION,
                        "Sessione interrotta trovata",
                        "Vuoi riprendere la sessione precedente?",
                        "Hai interrotto una partita. Vuoi riprendere da dove eri rimasto?",
                        riprendi, nuova
                    );

                    if (scelta.isPresent() && scelta.get() == riprendi) {
                        
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Quiz.fxml"));
                        Parent root = loader.load();
                        QuizController quiz = loader.getController();
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add("/wordageddon/Resources/css/style.css");
                        
                       
                        StatoGioco stato = JsonParserManuale.parseStatoGioco(s.getStatoGiocoJson());
                        quiz.setDomandaCorrente(stato.getDomandaCorrente());
                        quiz.setRisposteUtente(stato.getRisposteUtente());
                        quiz.impostaDomande(stato.getDomande());
                        quiz.mostraDomandaCorrente();
                        quiz.impostaSessione(s);
                        quiz.setTempoResiduo(s.getTempoResiduo());

                        Stage stage = (Stage) playBtn.getScene().getWindow();
                        quiz.setWindowCloseHandler(stage);
                        stage.setScene(scene);
                        stage.show();
                        return;
                    }else if(scelta.isPresent() && scelta.get() == nuova){
                        dao.deleteSessioneById(s.getId());
                    }

                    break;
                }
            }
            SessionManager.setLinguaScelta(getLinguaSelezionata());
            SceneUtils.switchScene(leaderboardBtn, "/wordageddon/Resources/fxml/ChooseDifficulty.fxml", "/wordageddon/Resources/css/style.css");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void goToLeaderboard() {
        SceneUtils.switchScene(leaderboardBtn, "/wordageddon/Resources/fxml/Classifica.fxml", "/wordageddon/Resources/css/style.css");
    }
    private void goToAdminPanel(){
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/AdminPanel.fxml", "/wordageddon/Resources/css/style.css");
    }
    
    
}
