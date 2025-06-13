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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wordageddon.config.AppConfig;
import wordageddon.database.SessioneDAOSQL;
import wordageddon.model.Difficolta;
import wordageddon.model.Domanda;
import wordageddon.model.Lingua;
import wordageddon.service.GeneratoreDomande;
import wordageddon.service.SessionManager;
import wordageddon.util.DialogUtils;
import wordageddon.util.SceneUtils;

/**
 * Controller della schermata di lettura testo di Wordageddon.
 * Gestisce la visualizzazione dei documenti, la gestione del timer di lettura,
 * la generazione del quiz e la navigazione tra i documenti.
 */
public class LetturaTestoController implements Initializable {

    @FXML private Label documentLabel;
    @FXML private Label timer;
    @FXML private Button quitGameBtn;
    @FXML private Button nextBtn;
    @FXML private TextArea documentTextArea;
    @FXML private ScrollPane scrollDocument;
    @FXML private Label tempoRimastoLabel;

    private Sessione sessione;
    private GameDifficultyConfig config;
    private List<Document> documenti; 
    private int indiceCorrente = 0;
    private int tempoResiduo;
    private Timeline timeline;
    private Set<String> vocabolarioGlobale;

    /**
     * Inizializza il controller e imposta i listener sui pulsanti.
     *
     * @param url URL location usata per risolvere i percorsi relativi all'oggetto root (può essere null).
     * @param rb  ResourceBundle usato per localizzare l'interfaccia utente (può essere null).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quitGameBtn.setOnAction(e -> goToMenu());
        nextBtn.setOnAction(e -> {
            if (timeline != null) timeline.stop();
            vaiAvanti();
        });
    }

    /**
     * Imposta la sessione e la configurazione di difficoltà.
     * Carica i documenti, mostra il primo documento e avvia il timer.
     *
     * @param sessione la sessione corrente
     * @param config configurazione di difficoltà del gioco
     */
    public void impostaSessione(Sessione sessione, GameDifficultyConfig config) {
        this.sessione = sessione;
        this.config = config;
        Difficolta diff = sessione.getDifficolta();
        this.documenti = caricaDocumenti(config);
        this.indiceCorrente = 0;
        mostraDocumento(indiceCorrente);
        startTimer();
    }

    /**
     * Carica i documenti disponibili secondo la configurazione specificata.
     *
     * @param config configurazione di difficoltà del gioco
     * @return lista di documenti caricati e filtrati
     */
    private List<Document> caricaDocumenti(GameDifficultyConfig config) {
        String cartellaBase = AppConfig.getDocumentiBasePath();
        Lingua lingua = sessione.getLingua();
        File folder = new File(cartellaBase, lingua.getFolderName());
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return Collections.emptyList();

        List<File> fileList = Arrays.stream(files)
            .filter(f -> contaParole(f) <= config.getLunghezzaTesto())
            .collect(Collectors.toList());

        Collections.shuffle(fileList);
        List<File> selezionati = fileList.stream()
            .limit(config.getNumDocumenti())
            .collect(Collectors.toList());

        Set<String> stopwords;
        switch (lingua) {
            case ITALIANO:
                stopwords = caricaStopwords(AppConfig.getStopwordsPathIT());
                break;
            case INGLESE:
                stopwords = caricaStopwords(AppConfig.getStopwordsPathEN());
                break;
            default:
                stopwords = Collections.emptySet();
        }
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

    /**
     * Conta il numero di parole in un file.
     *
     * @param file file di testo da analizzare
     * @return numero di parole, oppure Integer.MAX_VALUE in caso di errore
     */
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

    /**
     * Processa un file di testo, estraendo le parole e le loro frequenze,
     * escludendo le stopwords specificate.
     *
     * @param file file di testo da processare
     * @param stopwords set di stopwords da escludere
     * @return oggetto WordStats con parole e frequenze
     */
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

    /**
     * Mostra il documento corrente nella view e aggiorna il titolo e il testo.
     *
     * @param indice indice del documento da mostrare
     */
    private void mostraDocumento(int indice) {
        Document doc = documenti.get(indice);
        String title = doc.getTitle();
        if (title.endsWith(".txt")) {
            title = title.substring(0, title.length() - 4);
        }
        documentLabel.setText("Documento " + (indice + 1) + " di " + documenti.size() + " (" + doc.getTitleWithoutExtension() + ")");
        documentTextArea.setText(leggiTestoDaFile(doc.getPath()));
        tempoResiduo = config.getTempoLettura();
        aggiornaTimerLabel();
        if (indice == documenti.size() - 1) {
            nextBtn.setText("Vai al quiz");
        } else {
            nextBtn.setText("Vai al prossimo documento");
        }
    }

    /**
     * Legge il testo da un file dato il suo percorso.
     *
     * @param path percorso del file di testo
     * @return il testo contenuto nel file
     */
    private String leggiTestoDaFile(String path) {
        try {
            return new String(Files.readAllBytes(new File(path).toPath()));
        } catch (IOException e) {
            return "Errore nel caricamento del documento.";
        }
    }

    /**
     * Avvia il timer per la lettura del documento corrente.
     * Allo scadere del tempo passa automaticamente al prossimo documento o al quiz.
     */
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

    /**
     * Aggiorna la label del timer e il suo colore in base al tempo residuo.
     */
    private void aggiornaTimerLabel() {
        int min = tempoResiduo / 60;
        int sec = tempoResiduo % 60;
        timer.setText(String.format("%02d:%02d", min, sec));

        if (tempoResiduo <= 10) {
            timer.setStyle("-fx-text-fill: red;");
        } else if (tempoResiduo <= 30) {
            timer.setStyle("-fx-text-fill: orange;");
        } else {
            timer.setStyle("-fx-text-fill: green;");
        }
    }

    /**
     * Passa al prossimo documento oppure avvia il quiz se sono finiti i documenti.
     */
    private void vaiAvanti() {
        if (indiceCorrente < documenti.size() - 1) {
            indiceCorrente++;
            mostraDocumento(indiceCorrente);
            startTimer();
        } else {
            vaiAlQuiz();
        }
    }

    /**
     * Avvia la preparazione e la generazione delle domande del quiz a partire dai documenti letti.
     * Mostra una finestra di caricamento/progresso durante l'operazione.
     */
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
            StringBuilder json = new StringBuilder();
            json.append("{\"domande\":[");
            for (int i = 0; i < domandeQuiz.size(); i++) {
                Domanda d = domandeQuiz.get(i);
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
                if (i < domandeQuiz.size() - 1) json.append(",");
            }
            json.append("],\"risposteUtente\":[],\"domandaCorrente\":0}");
            if (domandeQuiz == null || domandeQuiz.isEmpty()) {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "Impossibile generare le domande.", " Riprova o scegli altri documenti.", null);
                return;
            }
            
            QuizController quizController =  SceneUtils.switchScene(nextBtn, "/wordageddon/Resources/fxml/Quiz.fxml", "/wordageddon/Resources/css/style.css");
            quizController.impostaSessione(sessione);
            quizController.impostaDomande(domandeQuiz);
            quizController.mostraDomandaCorrente(); 
        });

        task.setOnFailed(ev -> {
            progressStage.close();
            DialogUtils.showAlert(Alert.AlertType.ERROR, "Errore durante la preparazione del quiz: ", task.getException().toString(), null);
        });

        progressStage.show();
        new Thread(task).start();
    }

    /**
     * Chiede conferma all'utente e termina la partita tornando al menu principale,
     * eliminando la sessione corrente.
     */
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
            SessioneDAOSQL sessioneDAO = new SessioneDAOSQL();
            try {
                sessioneDAO.deleteSessioneById(sessione.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SessionManager.setSessione(null);
            SceneUtils.switchScene(quitGameBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
        }
    }

    /**
     * Carica le stopwords da un file su disco.
     *
     * @param stopwordsPath percorso al file delle stopwords
     * @return insieme delle stopwords caricate
     */
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
    
    /**
     * Imposta il comportamento alla chiusura della finestra durante la lettura.
     * @param stage stage della finestra corrente
     */
    public void setWindowCloseHandler(Stage stage) {
        stage.setOnCloseRequest(e -> {
            System.out.println("CHIUSURA DURANTE LETTURA TESTO");
        });
    }

    /**
     * Effettua l'escape di caratteri speciali per la serializzazione JSON.
     * @param s stringa da convertire
     * @return stringa "escapata" per JSON
     */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}