package wordageddon;

import java.io.BufferedReader;
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
import wordageddon.util.WordStats;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wordageddon.config.AppConfig;
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Difficolta;
import wordageddon.model.Domanda;
import wordageddon.service.GeneratoreDomande;
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
    private Set<String> vocabolarioGlobale;

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
        String cartellaBase = AppConfig.getDocumentiBasePath();
        File folder = new File(cartellaBase);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return Collections.emptyList();

        List<File> fileList = Arrays.stream(files)
            .filter(f -> contaParole(f) <= config.getLunghezzaTesto())
            .collect(Collectors.toList());

        Collections.shuffle(fileList);
        List<File> selezionati = fileList.stream()
            .limit(config.getNumDocumenti())
            .collect(Collectors.toList());

        Set<String> stopwords = caricaStopwords(AppConfig.getStopwordsPath());
        Set<String> vocabolarioGlobale = new HashSet<>();
        List<Document> result = new ArrayList<>();

        for (File f : selezionati) {
            WordStats stats = processaFile(f, stopwords);
            vocabolarioGlobale.addAll(stats.parole);
            result.add(new Document(f.getAbsolutePath(), f.getName(), stats.frequenze));
        }

        this.vocabolarioGlobale = vocabolarioGlobale;
        return result;
    }

    private int contaParole(File file) {
        try {
            String contenuto = new String(Files.readAllBytes(file.toPath()), java.nio.charset.StandardCharsets.UTF_8);
            String[] parole = contenuto.trim().split("\\s+");
            if (parole.length == 1 && parole[0].isEmpty()) return 0;
            return parole.length;
        } catch (IOException e) {
            return Integer.MAX_VALUE;
        }
    }

    private WordStats processaFile(File file, Set<String> stopwords) {
        WordStats stats = new WordStats();
        try (Scanner s = new Scanner(new BufferedReader(new FileReader(file)))) {
            s.useDelimiter("\\s+|\\,|\\-|\\:|\\;|\\?|\\!|\\+|\\-|\\_|\\.|\\—|\\–|\\'|\\’");
            while (s.hasNext()) {
                String parola = s.next().toLowerCase();
                if (stopwords.contains(parola) || parola.isEmpty()) continue;
                stats.parole.add(parola);
                stats.frequenze.put(parola, stats.frequenze.getOrDefault(parola, 0) + 1);
            }
        } catch (IOException ex) { }
        return stats;
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
        ProgressBar progressBar = new ProgressBar(0);
        Label progressLabel = new Label("Sto preparando le domande del quiz...");
        VBox box = new VBox(15, progressLabel, progressBar);
        box.setAlignment(javafx.geometry.Pos.CENTER);
        box.setPadding(new javafx.geometry.Insets(20));
        Scene scene = new Scene(box, 400, 120);

        Stage progressStage = new Stage();
        progressStage.setTitle("Preparazione quiz");
        progressStage.setScene(scene);
        progressStage.setResizable(false);
        progressStage.initOwner(nextBtn.getScene().getWindow());
        progressStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        Task<List<Domanda>> task = new Task<List<Domanda>>() {
            @Override
            protected List<Domanda> call() throws Exception {
                int numDomande = 5;
                List<Domanda> domande = new ArrayList<>();
                Set<String> testiDomande = new HashSet<>();
                GeneratoreDomande generatore = new GeneratoreDomande(documenti, vocabolarioGlobale);
                int tentativi = 0;
                int maxTentativi = numDomande * 15;
                while (domande.size() < numDomande && tentativi < maxTentativi) {
                    if (isCancelled()) return null;
                    Domanda domanda = generatore.generaDomandaRandom();
                    tentativi++;
                    if (domanda == null || domanda.getTesto() == null || testiDomande.contains(domanda.getTesto())) continue;
                    domande.add(domanda);
                    testiDomande.add(domanda.getTesto());
                    updateProgress(domande.size(), numDomande);
                    updateMessage("Domanda " + domande.size() + " di " + numDomande + "...");
                }
                if (domande.size() < numDomande) {
                    throw new Exception("Impossibile generare abbastanza domande! Aggiungi più documenti o usa testi più lunghi.");
                }
                return domande;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressLabel.textProperty().bind(task.messageProperty());

        progressStage.setOnCloseRequest(event -> {
            task.cancel();
            event.consume();
        });

        task.setOnSucceeded(ev -> {
            progressStage.close();
            List<Domanda> domandeQuiz = task.getValue();
            if (domandeQuiz == null || domandeQuiz.isEmpty()) {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "Impossibile generare le domande.", " Riprova o scegli altri documenti.", null);
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Quiz.fxml"));
                Parent root = loader.load();

                QuizController quizController = loader.getController();
                quizController.impostaSessione(sessione);
                quizController.impostaDomande(domandeQuiz);

                Stage stage = (Stage) nextBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        task.setOnFailed(ev -> {
            progressStage.close();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Errore durante la preparazione del quiz: ", task.getException().toString(), null);
        });

        progressStage.show();
        new Thread(task).start();
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

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/style.css").toExternalForm());

                Stage stage = (Stage) quitGameBtn.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Set<String> caricaStopwords(String stopwordsPath) {
        Set<String> stopwords = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(stopwordsPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopwords.add(line.trim().toLowerCase());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stopwords;
    }
}