package wordageddon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import wordageddon.model.Domanda;

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
    private int tempoTotale = 120; // 2 minuti in secondi (puoi parametrizzarlo)
    private Timeline timeline;

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
        if (selected != null) {
            String rispostaUtente = selected.getText();
            String rispostaCorretta = domande.get(domandaCorrente).getRispostaCorretta();
            if (rispostaUtente.equals(rispostaCorretta)) {
                punteggio++;
            }
        }
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz completato!");
        alert.setHeaderText(null);
        alert.setContentText("Hai totalizzato " + punteggio + " punti su " + domande.size());
        alert.showAndWait();
        // Qui puoi aggiungere codice per tornare al menu o altro
    }
}