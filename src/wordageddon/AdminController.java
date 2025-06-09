package wordageddon;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import wordageddon.config.AppConfig;
import wordageddon.util.SceneUtils;

public class AdminController implements Initializable {

    @FXML
    private Button documentFolderButton;
    @FXML
    private Button stopITButton;
    @FXML
    private Button stopENButton;
    @FXML
    private Button menuBtn;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;

    private File selectedDocumentFolder;
    private File selectedStopwordsIT;
    private File selectedStopwordsEN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        documentFolderButton.setOnAction(e -> chooseDocumentFolder());
        stopITButton.setOnAction(e -> chooseStopwordsIT());
        stopENButton.setOnAction(e -> chooseStopwordsEN());
        // Gli altri handler (menuBtn, privacyLink, infoLink) puoi aggiungerli qui se vuoi
        menuBtn.setOnAction(e -> goToMenu());
    }

    private void chooseDocumentFolder() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Scegli la cartella dei documenti");
        File folder = dc.showDialog(getStage());
        if (folder != null) {
            selectedDocumentFolder = folder;
            documentFolderButton.setText("Cartella: " + folder.getName());
            
            AppConfig.setDocumentiBasePath(folder.getPath());
        }
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

    // Utility per ottenere lo Stage corrente
    private Stage getStage() {
        return (Stage) documentFolderButton.getScene().getWindow();
    }
    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}