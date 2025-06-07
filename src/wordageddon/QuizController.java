package wordageddon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import wordageddon.model.Domanda;
import wordageddon.model.RispostaUtente;
import wordageddon.model.Sessione;
import wordageddon.model.Punteggio;
import wordageddon.util.DialogUtils;

public class QuizController implements Initializable {

    @FXML private Label questionNumberLabel;
    @FXML private Label timer;
    @FXML private Button nextBtn;
    @FXML private RadioButton option1Btn;
    @FXML private ToggleGroup optionsGroup;
    @FXML private RadioButton option2Btn;
    @FXML private RadioButton option3Btn;
    @FXML private RadioButton option4Btn;
    @FXML private Label questionLabel;

    private List<Domanda> domande;
    private int domandaCorrente = 0;
    private int punteggio = 0;

    // TIMER
    private int tempoTotale = 60; // 1 minuto in secondi (puoi parametrizzarlo)
    private Timeline timeline;

    private List<RispostaUtente> risposteUtente = new ArrayList<>();
    private Sessione sessione;
    private int tempoQuizResiduo = 0;

    // Metodo per ricevere la sessione dal controller precedente
    public void impostaSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nextBtn.setDisable(true);
        optionsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            nextBtn.setDisable(newToggle == null);
        });
        nextBtn.setOnAction(e -> {
            controllaRisposta();
            domandaCorrente++;
            if (domandaCorrente < domande.size()) {
                mostraDomandaCorrente();
            } else {
                if (timeline != null) timeline.stop();
                mostraRisultato();
            }
        });
    }

    public void impostaDomande(List<Domanda> domande) {
        this.domande = domande;
        this.domandaCorrente = 0;
        mostraDomandaCorrente();
        startTimerGlobale();
    }

    private void mostraDomandaCorrente() {
        if (domande == null || domande.isEmpty() || domandaCorrente >= domande.size()) return;
        Domanda d = domande.get(domandaCorrente);
        questionNumberLabel.setText("Domanda " + (domandaCorrente + 1) + " di " + domande.size());
        questionLabel.setText(d.getTesto());
        List<String> opzioni = d.getOpzioni();
        option1Btn.setText(opzioni.size() > 0 ? opzioni.get(0) : "");
        option2Btn.setText(opzioni.size() > 1 ? opzioni.get(1) : "");
        option3Btn.setText(opzioni.size() > 2 ? opzioni.get(2) : "");
        option4Btn.setText(opzioni.size() > 3 ? opzioni.get(3) : "");
        option1Btn.setDisable(opzioni.size() < 1);
        option2Btn.setDisable(opzioni.size() < 2);
        option3Btn.setDisable(opzioni.size() < 3);
        option4Btn.setDisable(opzioni.size() < 4);
        optionsGroup.selectToggle(null);
        nextBtn.setDisable(true);
    }

    private void controllaRisposta() {
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        String rispostaUtente = selected != null ? selected.getText() : "";
        Domanda domanda = domande.get(domandaCorrente);
        String rispostaCorretta = domanda.getRispostaCorretta();
        boolean esatta = rispostaUtente.equals(rispostaCorretta);
        if (esatta) punteggio++;
        risposteUtente.add(new RispostaUtente(
            domanda.getTesto(),
            rispostaUtente,
            rispostaCorretta,
            esatta
        ));
    }

    private void startTimerGlobale() {
        aggiornaTimerLabel();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoTotale--;
            aggiornaTimerLabel();
            if (tempoTotale <= 0) {
                timeline.stop();
                mostraRisultato(); // tempo scaduto, mostra subito risultato!
            }
        }));
        timeline.setCycleCount(tempoTotale);
        timeline.play();
    }

    private void aggiornaTimerLabel() {
        int min = tempoTotale / 60;
        int sec = tempoTotale % 60;
        timer.setText(String.format("%02d:%02d", min, sec));
    }

    private void mostraRisultato() {
        nextBtn.setDisable(true);
        DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Quiz completato!", null, "Hai totalizzato " + punteggio + " punti su " + domande.size());

        tempoQuizResiduo = tempoTotale; // Salva il tempo rimasto correttamente

        // Calcolo punteggio usando la classe Punteggio!
        int risposteCorrette = (int) risposteUtente.stream().filter(RispostaUtente::isEsatto).count();
        int difficoltaInt = getDifficoltaAsInt(sessione.getDifficolta().toString());
        String username = sessione.getUsername();

        Punteggio punteggioObj = new Punteggio(username, risposteCorrette, tempoQuizResiduo, difficoltaInt);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Results.fxml"));
            Parent root = loader.load();
            ResultsController resultsController = loader.getController();

            resultsController.setResults(
                risposteUtente,
                sessione,
                tempoQuizResiduo,
                punteggioObj
            );

            Stage stage = (Stage) nextBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility per convertire la difficoltà in intero (adatta alla tua enum)
    private int getDifficoltaAsInt(String difficolta) {
        switch (difficolta.toUpperCase()) {
            case "FACILE": return 1;
            case "MEDIO": return 2;
            case "DIFFICILE": return 3;
            default: return 1;
        }
    }
}