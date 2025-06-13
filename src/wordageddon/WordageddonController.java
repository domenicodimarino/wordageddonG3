package wordageddon;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
 * Controller principale della schermata iniziale di Wordageddon.
 * Gestisce la logica delle interfacce utente principali, il logout, la gestione delle sessioni,
 * la navigazione tra le schermate e l'apertura di documenti PDF informativi.
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

    /**
     * Inizializza il controller e i componenti della GUI.
     * Imposta i listener per i pulsanti e i menu, verifica il ruolo dell'utente e aggiorna la visibilità del pannello admin.
     * Effettua anche una correzione dello stato delle sessioni terminate.
     *
     * @param url non utilizzato.
     * @param rb  non utilizzato.
     */
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
        privacyLink.setOnAction(e -> apriPdf("privacy_info/PrivacyG3.pdf"));
        infoLink.setOnAction(e -> apriPdf("privacy_info/InfoG3.pdf"));
    }

    /**
     * Gestisce il logout dell'utente.
     * Mostra una finestra di conferma e, in caso affermativo, effettua il logout e torna alla schermata di login.
     */
    private void logout() {
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

    /**
     * Restituisce la lingua selezionata dall'utente per il quiz.
     *
     * @return la lingua selezionata.
     */
    private Lingua getLinguaSelezionata() {
        RadioMenuItem selected = (RadioMenuItem) quizLanguageGroup.getSelectedToggle();
        return Lingua.fromString(selected.getText());
    }

    /**
     * Passa alla schermata di login.
     */
    private void goToLogin() {
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/login.fxml", "/wordageddon/Resources/css/login.css");
    }

    /**
     * Passa alla schermata dello storico delle partite.
     */
    private void apriStorico() {
        SceneUtils.switchScene(storicoBtn, "/wordageddon/Resources/fxml/Storico.fxml", "/wordageddon/Resources/css/style.css");
    }

    /**
     * Avvia una nuova partita.
     * Se esiste una sessione interrotta la propone all'utente per essere ripresa, altrimenti avvia una nuova partita.
     */
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
                    } else if (scelta.isPresent() && scelta.get() == nuova) {
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

    /**
     * Passa alla schermata della classifica.
     */
    private void goToLeaderboard() {
        SceneUtils.switchScene(leaderboardBtn, "/wordageddon/Resources/fxml/Classifica.fxml", "/wordageddon/Resources/css/style.css");
    }

    /**
     * Passa al pannello di amministrazione (solo per utenti admin).
     */
    private void goToAdminPanel() {
        SceneUtils.switchScene(userMenu, "/wordageddon/Resources/fxml/AdminPanel.fxml", "/wordageddon/Resources/css/style.css");
    }

    /**
     * Apre un file PDF informativo (privacy o info) tramite il visualizzatore PDF del sistema.
     *
     * @param relativePath percorso relativo del PDF da aprire.
     */
    private void apriPdf(String relativePath) {
        try {
            
            File file = new File(relativePath);
            if (!file.exists()) {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "File non trovato: " + file.getAbsolutePath());
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "Il sistema non supporta l'apertura automatica dei PDF.");
            }
        } catch (IOException ex) {
            DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "Errore nell'apertura del PDF: " + ex.getMessage());
        }
    }
}