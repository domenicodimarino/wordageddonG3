package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import wordageddon.model.RispostaUtente;
import wordageddon.model.Sessione;
import wordageddon.model.Punteggio;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBtn.setOnAction(e -> goToMenu());
        leaderboardBtn.setOnAction(e -> goToLeaderboard());
    }

    /**
     * @param risposteUtente lista delle risposte utente
     * @param sessione dati della sessione quiz
     * @param tempoQuizResiduo secondi rimasti alla fine del quiz
     * @param punteggioObj oggetto Punteggio calcolato
     */
    public void setResults(List<RispostaUtente> risposteUtente, Sessione sessione, int tempoQuizResiduo, Punteggio punteggioObj) {
        ObservableList<RispostaUtente> dati = FXCollections.observableArrayList(risposteUtente);
        resultsTable.setItems(dati);

        questionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestoDomanda()));
        givenAnswerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRispostaData()));
        correctAnswerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRispostaCorretta()));
        scoreColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isEsatto()));

        scoreColumn.setCellFactory(col -> new TableCell<RispostaUtente, Boolean>() {
            @Override
            protected void updateItem(Boolean correct, boolean empty) {
                super.updateItem(correct, empty);
                if (empty || correct == null) {
                    setText(null);
                } else {
                    setText(correct ? "✔️" : "❌");
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

    private String formatSeconds(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void goToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Wordageddon.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
       
            scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/style.css").toExternalForm());
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void goToLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Classifica.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
       
            scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/style.css").toExternalForm());
            Stage stage = (Stage) leaderboardBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}