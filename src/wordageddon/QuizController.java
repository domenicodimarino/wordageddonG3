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

/**
 * Controller del quiz per l'applicazione Wordageddon.
 * Gestisce la logica della somministrazione delle domande, la gestione del timer,
 * il salvataggio delle risposte, la gestione della sessione e la visualizzazione dei risultati.
 */
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

    /**
     * Inizializza il controller, imposta i listener sui bottoni e sul gruppo di opzioni.
     * Disabilita il bottone "Avanti" finché non viene selezionata una risposta.
     *
     * @param url URL location utilizzata per risolvere i percorsi relativi all'oggetto root (può essere null).
     * @param rb  ResourceBundle utilizzato per localizzare l'interfaccia utente (può essere null).
     */
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

    /**
     * Imposta la sessione corrente.
     * @param sessione la sessione del quiz.
     */
    public void impostaSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    /**
     * Imposta la lista delle domande e avvia il timer globale.
     * @param domande lista delle domande del quiz.
     */
    public void impostaDomande(List<Domanda> domande) {
        this.domande = domande;
        startTimerGlobale();
    }

    /**
     * Imposta l'indice della domanda corrente.
     * @param index indice della domanda corrente.
     */
    public void setDomandaCorrente(int index) {
        this.domandaCorrente = index;
    }

    /**
     * Restituisce l'indice della domanda corrente.
     * @return indice della domanda corrente.
     */
    public int getDomandaCorrente() {
        return domandaCorrente;
    }

    /**
     * Imposta la lista delle risposte utente recuperate da una sessione interrotta.
     * Aggiunge solo le nuove risposte mancanti.
     * @param risposte lista delle risposte da impostare.
     */
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

    /**
     * Restituisce la lista delle risposte date dall'utente.
     * @return lista delle risposte utente.
     */
    public List<RispostaUtente> getRisposteUtente() {
        return risposteUtente;
    }

    /**
     * Imposta il tempo residuo del quiz in secondi.
     * @param secondi tempo residuo in secondi.
     */
    public void setTempoResiduo(int secondi) {
        this.tempoQuizResiduo = secondi;
    }

    /**
     * Imposta il comportamento alla chiusura della finestra per salvare la sessione interrotta.
     * @param stage lo stage della finestra corrente.
     */
    public void setWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(e -> salvaSessioneInterrotta());
    }

    /**
     * Mostra la domanda corrente e aggiorna le opzioni disponibili.
     * Aggiorna i testi e abilita/disabilita le opzioni a seconda della domanda.
     */
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

    /**
     * Controlla se la risposta selezionata dall'utente è corretta e aggiorna il punteggio.
     * Salva la risposta nella lista delle risposte utente.
     */
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

    /**
     * Avvia il timer globale del quiz e aggiorna la visualizzazione del tempo.
     * Cambia il colore del timer in base al tempo rimanente e gestisce la fine del tempo.
     */
    private void startTimerGlobale() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
        aggiornaTimerLabel();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoQuizResiduo--;
            aggiornaTimerLabel();

            // Lampeggio rosso negli ultimi 10 secondi
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

    /**
     * Aggiorna il testo del timer e il colore in base al tempo residuo.
     */
    private void aggiornaTimerLabel() {
        int min = tempoQuizResiduo / 60;
        int sec = tempoQuizResiduo % 60;
        timer.setText(String.format("%02d:%02d", min, sec));

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

    /**
     * Mostra il risultato finale del quiz, salva punteggio e sessione, e passa i dati al controller dei risultati.
     */
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

    /**
     * Ritorna la difficoltà come valore intero.
     * @param difficolta stringa della difficoltà ("FACILE", "MEDIO", "DIFFICILE").
     * @return valore intero della difficoltà (1=Facile, 2=Medio, 3=Difficile).
     */
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

    /**
     * Salva lo stato attuale della sessione in formato JSON per permettere la ripresa successiva.
     * Si attiva in caso di interruzione del quiz.
     */
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

    /**
     * Effettua l'escape di caratteri speciali per la serializzazione JSON.
     * @param s stringa da convertire.
     * @return stringa "escapata" per JSON.
     */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    /**
     * Gestisce il click sul pulsante "Interrompi".
     * Salva la sessione corrente e torna al menu principale.
     *
     * @param event l'evento di click del pulsante.
     */
    @FXML
    private void onInterrompiClicked(ActionEvent event) {
        if (timeline != null) timeline.stop();
        salvaSessioneInterrotta();
        SceneUtils.switchScene(interrompiBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}