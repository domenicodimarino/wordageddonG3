package wordageddon;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import wordageddon.database.PunteggioDAOSQL;
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Domanda;
import wordageddon.model.RispostaUtente;
import wordageddon.model.Sessione;
import wordageddon.model.Punteggio;
import wordageddon.util.DialogUtils;
import wordageddon.util.SceneUtils;

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
    @FXML private Button interrompiBtn;
    @FXML private Label tempoRimastoLabel;


    private List<Domanda> domande;
    private int domandaCorrente = 0;
    private int punteggio = 0;

    private Timeline timeline;
    private int tempoQuizResiduo = 60;

    private List<RispostaUtente> risposteUtente = new ArrayList<>();
    private Sessione sessione;
    private boolean staLampeggiando = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nextBtn.setDisable(true);
        optionsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            nextBtn.setDisable(newToggle == null);
        });

        nextBtn.setOnAction(e -> {
            controllaRisposta();
            optionsGroup.selectToggle(null);
            domandaCorrente++;
            if (domandaCorrente < domande.size()) {
                mostraDomandaCorrente();
            } else {
                if (timeline != null) timeline.stop();
                mostraRisultato();
            }
        });
    }

    public void impostaSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    public void impostaDomande(List<Domanda> domande) {
        this.domande = domande;
        startTimerGlobale();
    }

    public void setDomandaCorrente(int index) {
        this.domandaCorrente = index;
    }

    public int getDomandaCorrente() {
        return domandaCorrente;
    }

    public void setRisposteUtente(List<RispostaUtente> risposte) {
        for (RispostaUtente nuova : risposte) {
            boolean giàEsiste = this.risposteUtente.stream()
                .anyMatch(r -> r.getDomandaIndex() == nuova.getDomandaIndex());
            if (!giàEsiste) {
                this.risposteUtente.add(nuova);
                if (nuova.isEsatto()) {
                    punteggio++;
                }
            }
        }       
    }

    public List<RispostaUtente> getRisposteUtente() {
        return risposteUtente;
    }

    public void setTempoResiduo(int secondi) {
        this.tempoQuizResiduo = secondi;
    }

    public void setWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(e -> salvaSessioneInterrotta());
    }

    void mostraDomandaCorrente() {
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
        if (selected == null) return;

        String rispostaUtente = selected.getText();
        Domanda domanda = domande.get(domandaCorrente);
        String rispostaCorretta = domanda.getRispostaCorretta();
        boolean esatta = rispostaUtente.equals(rispostaCorretta);
        if (esatta) punteggio++;

        risposteUtente.add(new RispostaUtente(
            domandaCorrente,
            domanda.getTesto(),
            rispostaUtente,
            rispostaCorretta,
            esatta
        ));
    }

    private void startTimerGlobale() {
    if (timeline != null) {
        timeline.stop();
        timeline = null;
    }
    aggiornaTimerLabel();
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        tempoQuizResiduo--;
        aggiornaTimerLabel();

        // 👇 Lampeggio sotto i 10 secondi (una sola volta)
        if (tempoQuizResiduo <= 10 && !staLampeggiando) {
            staLampeggiando = true;
            Timeline blink = new Timeline(
                new KeyFrame(Duration.seconds(0), evt -> timer.setStyle("-fx-text-fill: red;")),
                new KeyFrame(Duration.seconds(0.5), evt -> timer.setStyle("-fx-text-fill: darkred;"))
            );
            blink.setCycleCount(Timeline.INDEFINITE);
            blink.play();
        }

        if (tempoQuizResiduo <= 0) {
            timeline.stop();
            mostraRisultato();
        }
    }));
    timeline.setCycleCount(tempoQuizResiduo);
    timeline.play();
    }
    
    private void aggiornaTimerLabel() {
    int min = tempoQuizResiduo / 60;
    int sec = tempoQuizResiduo % 60;
    timer.setText(String.format("%02d:%02d", min, sec));

    // Solo se non sta lampeggiando (così non sovrascrivi lo stile impostato dal blink)
    if (!staLampeggiando) {
        if (tempoQuizResiduo <= 10) {
            timer.setStyle("-fx-text-fill: red;");
        } else if (tempoQuizResiduo <= 30) {
            timer.setStyle("-fx-text-fill: orange;");
        } else {
            timer.setStyle("-fx-text-fill: green;");
        }
    }
}


    private void mostraRisultato() {
        nextBtn.setDisable(true);

        sessione.setStato("finita");
        sessione.setDataFine(java.time.LocalDateTime.now().toString());
        sessione.setTempoResiduo(tempoQuizResiduo);

        int risposteCorrette = (int) risposteUtente.stream().filter(RispostaUtente::isEsatto).count();
        int difficoltaInt = getDifficoltaAsInt(sessione.getDifficolta().toString());
        Punteggio punteggioObj = new Punteggio(sessione.getUsername(), risposteCorrette, tempoQuizResiduo, difficoltaInt);
        sessione.setPunteggioTotale(punteggioObj.getValore());

        try {
            new PunteggioDAOSQL().inserisci(punteggioObj);
            new SessioneDAOSQL().updateSessione(sessione);
        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(QuizController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ResultsController resultsController = SceneUtils.switchScene(
                nextBtn,
                "/wordageddon/Resources/fxml/Results.fxml",
                "/wordageddon/Resources/css/style.css"
            );

            if (resultsController != null) {
                resultsController.setResults(
                    risposteUtente,
                    sessione,
                    tempoQuizResiduo,
                    punteggioObj
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDifficoltaAsInt(String difficolta) {
        if (difficolta == null) return 1;
            switch (difficolta.toUpperCase()) {
                case "FACILE":
                    return 1;
                case "MEDIO":
                    return 2;
                case "DIFFICILE":
                    return 3;
                default:
                    return 1;
        }
    }

    private void salvaSessioneInterrotta() {
        if (timeline != null) {
            timeline.stop();
        }
        try {
            StringBuilder json = new StringBuilder();
            json.append("{");

            json.append("\"domande\":[");
            for (int i = 0; i < domande.size(); i++) {
                Domanda d = domande.get(i);
                json.append("{");
                json.append("\"testo\":\"").append(escape(d.getTesto())).append("\",");
                json.append("\"rispostaCorretta\":\"").append(escape(d.getRispostaCorretta())).append("\",");
                json.append("\"tipo\":\"").append(escape(d.getTipo())).append("\",");
                json.append("\"opzioni\":[");
                List<String> ops = d.getOpzioni();
                for (int j = 0; j < ops.size(); j++) {
                    json.append("\"").append(escape(ops.get(j))).append("\"");
                    if (j < ops.size() - 1) json.append(",");
                }
                json.append("]");
                json.append("}");
                if (i < domande.size() - 1) json.append(",");
            }
            json.append("],");

            json.append("\"risposteUtente\":[");
            for (int i = 0; i < risposteUtente.size(); i++) {
                RispostaUtente ru = risposteUtente.get(i);
                json.append("{");
                json.append("\"domandaIndex\":").append(ru.getDomandaIndex()).append(",");
                json.append("\"sceltaUtente\":\"").append(escape(ru.getSceltaUtente())).append("\",");
                json.append("\"testoDomanda\":\"").append(escape(ru.getTestoDomanda())).append("\",");
                json.append("\"rispostaCorretta\":\"").append(escape(ru.getRispostaCorretta())).append("\",");
                json.append("\"esatto\":").append(ru.isEsatto());
                json.append("}");
                if (i < risposteUtente.size() - 1) json.append(",");
            }
            json.append("],");

            json.append("\"domandaCorrente\":").append(domandaCorrente);
            json.append("}");

            sessione.setStato("in_corso");
            sessione.setTempoResiduo(tempoQuizResiduo);
            sessione.setStatoGiocoJson(json.toString());

            new SessioneDAOSQL().updateSessione(sessione);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }    

    @FXML
    private void onInterrompiClicked(ActionEvent event) {
        if (timeline != null) timeline.stop();
        salvaSessioneInterrotta();
        SceneUtils.switchScene(interrompiBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}
