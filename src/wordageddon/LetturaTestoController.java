package wordageddon;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.Duration;
import wordageddon.model.GameDifficultyConfig;
import wordageddon.model.Sessione;
import wordageddon.model.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import wordageddon.config.AppConfig;
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Difficolta;
import wordageddon.service.SessionManager;
import wordageddon.util.DialogUtils;

public class LetturaTestoController implements Initializable {

    @FXML private Label documentLabel;
    @FXML private Label timer;
    @FXML private Button quitGameBtn;
    @FXML private Button nextBtn;
    @FXML private TextArea documentTextArea;

    private Sessione sessione;
    private GameDifficultyConfig config;
    private List<Document> documenti; // Lista dei documenti da visualizzare
    private int indiceCorrente = 0;
    private int tempoResiduo;
    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quitGameBtn.setOnAction(e -> goToMenu());
        nextBtn.setOnAction(e -> {
            if (timeline != null) timeline.stop();
            vaiAvanti();
        });
    }

    public void impostaSessione(Sessione sessione, GameDifficultyConfig config) {
        this.sessione = sessione;
        this.config = config;
        Difficolta diff = sessione.getDifficolta();
        this.documenti = caricaDocumenti(config);
        this.indiceCorrente = 0;
        mostraDocumento(indiceCorrente);
        startTimer();
    }

    private List<Document> caricaDocumenti(GameDifficultyConfig config) {
        String cartellaBase = AppConfig.getDocumentiBasePath(); // ad esempio "DocumentFolder"
        File folder = new File(cartellaBase);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return Collections.emptyList();

        // Filtra i file in base al numero di parole
        List<File> fileList = Arrays.stream(files)
            .filter(f -> contaParole(f) <= config.getLunghezzaTesto())
            .collect(Collectors.toList());

        // Mescola la lista per la casualità
        Collections.shuffle(fileList);

        // Prendi solo il numero richiesto
        List<File> selezionati = fileList.stream()
            .limit(config.getNumDocumenti())
            .collect(Collectors.toList());

        List<Document> result = new ArrayList<>();
        for (File f : selezionati) {
            try {
                result.add(new Document(f.getAbsolutePath(), f.getName(), Collections.emptySet()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private int contaParole(File file) {
        try {
            String contenuto = new String(Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
            // Split su uno o più spazi (o newline)
            String[] parole = contenuto.trim().split("\\s+");
            // Evita di contare 1 parola se il file è vuoto
            if (parole.length == 1 && parole[0].isEmpty()) return 0;
            return parole.length;
        } catch (IOException e) {
            return Integer.MAX_VALUE; // Così i file che danno errore non vengono scelti
        }
    }
    private void mostraDocumento(int indice) {
        Document doc = documenti.get(indice);
        documentLabel.setText("Documento " + (indice + 1) + " di " + documenti.size() + " (" + doc.getTitle() + ")");
        documentTextArea.setText(leggiTestoDaFile(doc.getPath()));
        tempoResiduo = config.getTempoLettura();
        aggiornaTimerLabel();
        if (indice == documenti.size() - 1) {
            nextBtn.setText("Vai al quiz");
        } else {
            nextBtn.setText("Vai al prossimo documento");
        }
    }

    private String leggiTestoDaFile(String path) {
        try {
            return new String(Files.readAllBytes(new File(path).toPath()));
        } catch (IOException e) {
            return "Errore nel caricamento del documento.";
        }
    }

    private void startTimer() {
        aggiornaTimerLabel();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempoResiduo--;
            aggiornaTimerLabel();
            if (tempoResiduo <= 0) {
                timeline.stop();
                vaiAvanti();
            }
        }));
        timeline.setCycleCount(tempoResiduo);
        timeline.play();
    }

    private void aggiornaTimerLabel() {
        int min = tempoResiduo / 60;
        int sec = tempoResiduo % 60;
        timer.setText(String.format("%02d:%02d", min, sec));
    }

    private void vaiAvanti() {
        if (indiceCorrente < documenti.size() - 1) {
            indiceCorrente++;
            mostraDocumento(indiceCorrente);
            startTimer();
        } else {
            vaiAlQuiz();
        }
    }

    private void vaiAlQuiz() {
        // TODO: Carica la schermata del quiz, passa sessione e config se necessario
    }

    private void goToMenu() {
        
        ButtonType yes = new ButtonType("Sì");
        ButtonType no = new ButtonType("No");
        Optional<ButtonType> result = DialogUtils.showCustomAlert(
            Alert.AlertType.CONFIRMATION,
            "Termina partita",
            "Sei sicuro?",
            "Vuoi terminare la partita?",
            yes, no
        );
        
        if (result.isPresent() && result.get() == yes) {
            try {
                
                SessioneDAOSQL sessioneDAO = new SessioneDAOSQL();
                try {
                    sessioneDAO.deleteSessioneById(sessione.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                SessionManager.setSessione(null);
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Wordageddon.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) quitGameBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}