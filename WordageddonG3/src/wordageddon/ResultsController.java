package wordageddon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import wordageddon.model.RispostaUtente;
import wordageddon.model.Sessione;
import wordageddon.model.Punteggio;
import wordageddon.util.SceneUtils;

/**
 * Controller della schermata dei risultati del quiz di Wordageddon.
 * Gestisce la visualizzazione dei risultati, delle risposte date, punteggio e statistiche.
 */
public class ResultsController implements Initializable {

    @FXML private TableView<RispostaUtente> resultsTable;
    @FXML private TableColumn<RispostaUtente, String> questionColumn;
    @FXML private TableColumn<RispostaUtente, String> givenAnswerColumn;
    @FXML private TableColumn<RispostaUtente, String> correctAnswerColumn;
    @FXML private TableColumn<RispostaUtente, Boolean> scoreColumn;
    @FXML private Label numberCorrectAnswersLabel;
    @FXML private Label timerLabel;
    @FXML private Label difficultyLabel;
    @FXML private Label scoreLabel;
    @FXML private Button menuBtn;
    @FXML private Button leaderboardBtn;

    /**
     * Inizializza il controller, impostando i listener sui pulsanti di navigazione.
     *
     * @param url URL location utilizzata per risolvere i percorsi relativi all'oggetto root (può essere null).
     * @param rb  ResourceBundle utilizzato per localizzare l'interfaccia utente (può essere null).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBtn.setOnAction(e -> goToMenu());
        leaderboardBtn.setOnAction(e -> goToLeaderboard());
    }

    /**
     * Popola la schermata dei risultati con le risposte dell'utente, la sessione e le statistiche di gioco.
     *
     * @param risposteUtente lista delle risposte dell'utente
     * @param sessione dati della sessione quiz
     * @param tempoQuizResiduo secondi rimasti alla fine del quiz
     * @param punteggioObj oggetto Punteggio calcolato
     */
    public void setResults(List<RispostaUtente> risposteUtente, Sessione sessione, int tempoQuizResiduo, Punteggio punteggioObj) {
        ObservableList<RispostaUtente> dati = FXCollections.observableArrayList(risposteUtente);
        resultsTable.setItems(dati);

        questionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestoDomanda()));
        givenAnswerColumn.setCellValueFactory(data -> {
            String rispostaData = data.getValue().getRispostaData();
            if (rispostaData != null && rispostaData.endsWith(".txt")) {
                rispostaData = rispostaData.substring(0, rispostaData.length() - 4);
            }
            return new SimpleStringProperty(rispostaData);
        });
        correctAnswerColumn.setCellValueFactory(data -> {
            String rispostaCorretta = data.getValue().getRispostaCorretta();
            if (rispostaCorretta != null && rispostaCorretta.endsWith(".txt")) {
                rispostaCorretta = rispostaCorretta.substring(0, rispostaCorretta.length() - 4);
            }
            return new SimpleStringProperty(rispostaCorretta);
        });

        scoreColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isEsatto()));

        scoreColumn.setCellFactory(col -> new TableCell<RispostaUtente, Boolean>() {
            @Override
            protected void updateItem(Boolean correct, boolean empty) {
                super.updateItem(correct, empty);
                if (empty || correct == null) {
                    setText(null);
                } else {
                    setText(correct ? "CORRETTA" : "ERRATA");
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        long corrette = risposteUtente.stream().filter(RispostaUtente::isEsatto).count();
        numberCorrectAnswersLabel.setText(String.valueOf(corrette));
        scoreLabel.setText(String.valueOf(punteggioObj.getValore()));
        difficultyLabel.setText(sessione.getDifficolta().toString());
        timerLabel.setText(formatSeconds(tempoQuizResiduo));
    }

    /**
     * Converte un valore in secondi in una stringa formattata MM:SS.
     *
     * @param seconds il numero di secondi
     * @return la stringa formattata in minuti e secondi
     */
    private String formatSeconds(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Torna al menu principale dell'applicazione.
     */
    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }

    /**
     * Passa alla schermata della classifica.
     */
    private void goToLeaderboard() {
        SceneUtils.switchScene(leaderboardBtn, "/wordageddon/Resources/fxml/Classifica.fxml", "/wordageddon/Resources/css/style.css");
    }
}