package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Difficolta;
import wordageddon.model.GameConfig;
import wordageddon.model.GameDifficultyConfig;
import wordageddon.model.Lingua;
import wordageddon.model.Sessione;
import wordageddon.model.Utente;
import wordageddon.service.SessionManager;
import wordageddon.util.SceneUtils;

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

    private Utente utente;

    private SessioneDAOSQL sessioneDAO = new SessioneDAOSQL();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBtn.setOnAction(e -> goToMenu());
        easyBtn.setOnAction(e -> scegliDifficolta(Difficolta.FACILE));
        mediumBtn.setOnAction(e -> scegliDifficolta(Difficolta.MEDIO));
        hardBtn.setOnAction(e -> scegliDifficolta(Difficolta.DIFFICILE));
        utente = SessionManager.getUtente();
    }

    private void scegliDifficolta(Difficolta diff) {
        GameDifficultyConfig config = GameConfig.getConfig(diff);
        
        Lingua lingua = SessionManager.getLinguaScelta();

        // Crea una nuova sessione
        String dataInizio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Sessione nuovaSessione = new Sessione(
            utente.getUsername(),
            dataInizio,
            null, // dataFine
            0,    // punteggioTotale
            config.getTempoLettura(), // tempoResiduo (o altro valore iniziale)
            null, // statoGiocoJson (puoi serializzare lo stato più avanti)
            "in_corso",
            diff,
            lingua
        );

        try {
            sessioneDAO.insertSessione(nuovaSessione); // Dopo questa chiamata, l'ID viene impostato
            // (Opzionale) Salva la sessione corrente in SessionManager se vuoi accedervi globalmente
            SessionManager.setSessione(nuovaSessione);

            vaiALetturaTesto(diff, config, nuovaSessione);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Mostra errore all'utente se vuoi
        }
    }

    private void vaiALetturaTesto(Difficolta diff, GameDifficultyConfig config, Sessione sessione) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/LetturaTesto.fxml"));
            Parent root = loader.load();

            LetturaTestoController controller = loader.getController();
            controller.impostaSessione(sessione, config);

            // Ottieni lo stage corrente
            Stage stage = (Stage) easyBtn.getScene().getWindow();

            // Passa lo stage per impostare il gestore di chiusura finestra
            controller.setWindowCloseHandler(stage);

            // Cambia scena
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}