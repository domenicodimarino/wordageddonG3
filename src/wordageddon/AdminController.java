package wordageddon;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wordageddon.config.AppConfig;
import wordageddon.util.DialogUtils;
import wordageddon.util.SceneUtils;

/**
 * Controller per la gestione delle funzioni amministrative dell'applicazione Wordageddon.
 * Permette la gestione di stopwords, documenti in italiano/inglese, visualizzazione e caricamento di nuovi documenti,
 * e l'accesso ai link informativi e privacy.
 */
public class AdminController implements Initializable {

    @FXML
    private Button stopITButton;
    @FXML
    private Button stopENButton;
    @FXML
    private Button menuBtn;
    @FXML
    private Button uploadDocButton;
    @FXML
    private ToggleGroup docLanguage;
    @FXML
    private TableView<DocumentRow> documentTable;
    @FXML
    private TableColumn<DocumentRow, String> docNameColumn;
    @FXML
    private TableColumn<DocumentRow, String> docLangColumn;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;

    /**
     * Classe interna che rappresenta una riga nella tabella dei documenti caricati.
     */
    public static class DocumentRow {
        private final String nome;
        private final String lingua;

        /**
         * Costruisce una riga documento.
         * @param nome nome del documento
         * @param lingua lingua del documento ("Italiano" o "Inglese")
         */
        public DocumentRow(String nome, String lingua) {
            this.nome = nome;
            this.lingua = lingua;
        }
        /**
         * Restituisce il nome del documento.
         * @return nome del documento
         */
        public String getNome() { return nome; }
        /**
         * Restituisce la lingua del documento.
         * @return lingua del documento
         */
        public String getLingua() { return lingua; }
    }

    private File selectedStopwordsIT;
    private File selectedStopwordsEN;
    private ObservableList<DocumentRow> documentRows = FXCollections.observableArrayList();

    /**
     * Inizializza il controller, imposta i listener sui pulsanti, popola la tabella dei documenti e aggiorna la vista.
     *
     * @param url URL location utilizzata per risolvere i percorsi relativi all'oggetto root (può essere null).
     * @param rb  ResourceBundle utilizzato per localizzare l'interfaccia utente (può essere null).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopITButton.setOnAction(e -> chooseStopwordsIT());
        stopENButton.setOnAction(e -> chooseStopwordsEN());
        menuBtn.setOnAction(e -> goToMenu());

        docNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        docLangColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLingua()));
        documentTable.setItems(documentRows);

        uploadDocButton.setOnAction(e -> uploadDocument());
        privacyLink.setOnAction(e -> apriPdf("privacy_info/PrivacyG3.pdf"));
        infoLink.setOnAction(e -> apriPdf("privacy_info/InfoG3.pdf"));

        refreshDocumentTable();
    }

    /**
     * Permette di selezionare il file delle stopwords per l'italiano.
     * Aggiorna l'etichetta del pulsante con il nome del file selezionato.
     */
    private void chooseStopwordsIT() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Scegli file stopwords italiano");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(getStage());
        if (file != null) {
            selectedStopwordsIT = file;
            stopITButton.setText("IT: " + file.getName());
        }
    }

    /**
     * Permette di selezionare il file delle stopwords per l'inglese.
     * Aggiorna l'etichetta del pulsante con il nome del file selezionato.
     */
    private void chooseStopwordsEN() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Scegli file stopwords inglese");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(getStage());
        if (file != null) {
            selectedStopwordsEN = file;
            stopENButton.setText("EN: " + file.getName());
        }
    }

    /**
     * Permette il caricamento di un nuovo documento di testo in italiano o inglese.
     * Richiede la selezione della lingua e copia il file nella cartella appropriata.
     * Aggiorna la tabella dei documenti dopo il caricamento.
     */
    private void uploadDocument() {
        Toggle selectedToggle = docLanguage.getSelectedToggle();
        if (selectedToggle == null) {
            showAlert("Seleziona una lingua (Italiano/Inglese) prima di caricare un documento.");
            return;
        }
        String lang = ((RadioButton) selectedToggle).getText();
        String langFolder = lang.equalsIgnoreCase("Italiano") ? "ita" : "eng";

        FileChooser fc = new FileChooser();
        fc.setTitle("Scegli documento di testo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showOpenDialog(getStage());
        if (file == null) return;

        String basePath = AppConfig.getDocumentiBasePath();
        if (basePath == null || basePath.isEmpty()) {
            showAlert("Seleziona prima la cartella base dei documenti!");
            return;
        }

        File destDir = new File(basePath, langFolder);
        if (!destDir.exists()) destDir.mkdirs();

        File destFile = new File(destDir, file.getName());
        if (destFile.exists()) {
            showAlert("Esiste già un documento con questo nome in " + lang + "!");
            return;
        }

        try {
            Files.copy(file.toPath(), destFile.toPath());
            documentRows.add(new DocumentRow(file.getName(), lang));
            showAlert("Documento caricato con successo!");
        } catch (IOException ex) {
            showAlert("Errore durante il caricamento del documento: " + ex.getMessage());
        }
    }

    /**
     * Aggiorna la tabella dei documenti leggendo i file presenti nelle cartelle
     * di italiano e inglese della directory dei documenti.
     */
    private void refreshDocumentTable() {
        documentRows.clear();
        String basePath = AppConfig.getDocumentiBasePath();
        if (basePath == null || basePath.isEmpty()) return;

        File itaDir = new File(basePath, "ita");
        File engDir = new File(basePath, "eng");
        if (itaDir.exists() && itaDir.isDirectory()) {
            for (File file : itaDir.listFiles((dir, name) -> name.endsWith(".txt"))) {
                documentRows.add(new DocumentRow(file.getName(), "Italiano"));
            }
        }
        if (engDir.exists() && engDir.isDirectory()) {
            for (File file : engDir.listFiles((dir, name) -> name.endsWith(".txt"))) {
                documentRows.add(new DocumentRow(file.getName(), "Inglese"));
            }
        }
    }

    /**
     * Mostra una finestra di dialogo informativa con il messaggio passato.
     * @param msg messaggio da visualizzare
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Restituisce lo stage corrente dell'applicazione.
     * @return stage corrente
     */
    private Stage getStage() {
        return (Stage) menuBtn.getScene().getWindow();
    }

    /**
     * Torna al menu principale dell'applicazione.
     */
    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }

    /**
     * Apre un file PDF informativo (privacy o info) tramite il visualizzatore PDF del sistema.
     *
     * @param relativePath percorso relativo del PDF da aprire.
     */
    private void apriPdf(String relativePath) {
        try {
            
            File file = new File(relativePath);
            if (!file.exists()) {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "File non trovato: " + file.getAbsolutePath());
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "Il sistema non supporta l'apertura automatica dei PDF.");
            }
        } catch (IOException ex) {
            DialogUtils.showAlert(Alert.AlertType.ERROR, "ERRORE", null, "Errore nell'apertura del PDF: " + ex.getMessage());
        }
    }
}