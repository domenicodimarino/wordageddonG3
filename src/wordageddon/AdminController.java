package wordageddon;

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
import wordageddon.util.SceneUtils;

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

   
    public static class DocumentRow {
        private final String nome;
        private final String lingua;
        public DocumentRow(String nome, String lingua) {
            this.nome = nome;
            this.lingua = lingua;
        }
        public String getNome() { return nome; }
        public String getLingua() { return lingua; }
    }

    private File selectedStopwordsIT;
    private File selectedStopwordsEN;
    private ObservableList<DocumentRow> documentRows = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopITButton.setOnAction(e -> chooseStopwordsIT());
        stopENButton.setOnAction(e -> chooseStopwordsEN());
        menuBtn.setOnAction(e -> goToMenu());

        
        docNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        docLangColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLingua()));
        documentTable.setItems(documentRows);

        uploadDocButton.setOnAction(e -> uploadDocument());

        
        refreshDocumentTable();
    }

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

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private Stage getStage() {
        
        return (Stage) menuBtn.getScene().getWindow();
    }
    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}