package wordageddon;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class WordageddonController implements Initializable {
    
    @FXML
    private TextField queryField;
    @FXML
    private Label queryLabel;
    @FXML
    private Label docsCountField;
    @FXML
    private Label wordsCountField;
    @FXML
    private TableView<Document> documentTable;
    @FXML
    private TableColumn<Document, String> fileNameCln;
    @FXML
    private TableColumn<Document, BigDecimal> indexCln;
    @FXML
    private Label directoryLabel;
    @FXML
    private ListView<String> vocabularyLV;
    @FXML
    private Button sendButton;
    @FXML
    private Button clearButton;
    
    private Set<String> vocabulary;
    private List<Document> docs;
    private Set<String> stopWords;
    
    private ObservableList<String> obsVocabulary;
    private ObservableList<Document> obsDocs;
    
    private File folder;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        vocabulary = new LinkedHashSet<>();
        docs = new ArrayList<>();
        stopWords = new HashSet<>();
        obsVocabulary = FXCollections.observableArrayList();
        obsDocs = FXCollections.observableArrayList();
        
        fileNameCln.setCellValueFactory(new PropertyValueFactory("title"));
        indexCln.setCellValueFactory(new PropertyValueFactory("score"));
        
        documentTable.setItems(obsDocs);
        vocabularyLV.setItems(obsVocabulary);
        
        initStopWords();
        initBindings();
        initEventHandlers();
        
    }
    
    /**
     * Triggered by the button "Scegli cartella".
     * The user choose a folder.
     * @param event
     * @throws IOException 
     */
    @FXML
    private void chooseFolder(ActionEvent event) throws IOException {
        
        DirectoryChooser dc = new DirectoryChooser();
        folder = dc.showDialog(queryField.getParent().getScene().getWindow());

        if(folder != null) {
            
            clearVariables();
        
            String directoryPath = folder.getAbsolutePath();
            directoryLabel.setText("Directory: " + directoryPath);

            List<File> files = Stream.of(folder.listFiles()).collect(Collectors.toList());

            createVocabulary(directoryPath, files);
        }
        
    }
    
    /**
     * Called by chooseFolder().
     * Create the vocabulary starting the VocabularyService.
     * @param directoryPath the path of the directory chosen by the user
     * @param files the list of the .txt files
     */
    private void createVocabulary(String directoryPath, List<File> files) {
        
        VocabularyService vs = new VocabularyService(directoryPath, stopWords);
        vs.start();
        vs.setOnSucceeded(e -> {
            
            vocabulary.addAll(vs.getValue());
            files.forEach(f -> {
                try {
                    docs.add(new Document(f.getAbsolutePath(), f.getName(), vocabulary));
                } catch (IOException ex) { }
            });
            obsVocabulary.addAll(vocabulary);
            obsVocabulary.sort(String::compareTo);
            obsDocs.addAll(docs);
            docsCountField.setText("Numero di documenti: " + docs.size());
        });
    }
    
    /**
     * Triggered by the button "Invia".
     * @param event 
     */
    @FXML
    private void sendQueryByBtn(ActionEvent event) {
        sendQuery();
    }
    
    /**
     * Triggered by pushing Enter key.
     * @param event 
     */
    @FXML
    private void sendQueryByEnter(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER) && !queryField.getText().isEmpty() && !obsVocabulary.isEmpty()) {
            sendQuery();
        }
    }
    
    /**
     * Calculate the index of relevance of the files starting the QueryService and
     * show the documents in the table sorted by their score in descending order.
     */
    private void sendQuery(){
        String query = queryField.getText();
        queryLabel.setText("Query: " + query);
        
        QueryService qs = new QueryService(query.toLowerCase().split("\\s+"), vocabulary);
        qs.start();
        qs.setOnSucceeded(e -> {
            
        });
    }
    
    /**
     * Triggered by the button "Scegli stop-word".
     * Rielaborate the vocabulary based on the chosen stop-word file.
     * @param event 
     */
    @FXML
    private void chooseStopWords(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        File f = fc.showOpenDialog(queryField.getParent().getScene().getWindow());
        
        if(f != null) {
            
            stopWords.clear();
            try(BufferedReader br = new BufferedReader(new FileReader(f))) {
                String stopWord;
                while ((stopWord = br.readLine()) != null)
                    stopWords.add(stopWord);
            } catch (IOException ex) { }
            
            clearVariables();
            
            List<File> files = Stream.of(folder.listFiles()).collect(Collectors.toList());
            createVocabulary(folder.getAbsolutePath(), files);
        }
    }
    
    /**
     * Triggered by the button "X".
     * Clear the field of input query.
     * @param event 
     */
    @FXML
    private void clearQueryField(ActionEvent event) {
        queryField.clear();
    }
    
    /**
     * Called by initialize() method.
     * Automatically choose a default stop-word file.
     */
    private void initStopWords() {
        
        try(BufferedReader br = new BufferedReader(new FileReader("stopwords-it.txt"))) {
            String stopWord;
            while ((stopWord = br.readLine()) != null)
                stopWords.add(stopWord);
        } catch (IOException ex) { }
    }
    
    /**
     * Called by initialize() method.
     * Disable button if query field is empty.
     */
    private void initBindings() {
        
        // SendButton Binding
        sendButton.disableProperty().bind(Bindings.isEmpty(queryField.textProperty()).or(Bindings.isEmpty(obsVocabulary)));
        // ClearButton Binding
        clearButton.disableProperty().bind(Bindings.equal("", queryField.textProperty()));
    }
    
    /**
     * Called by initialize() method.
     * Open the selected file on the table by clicking twice.
     */
    private void initEventHandlers() {
        
        // DocumentTable EventHandler
        documentTable.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && !documentTable.getSelectionModel().isEmpty()) {
                wordsCountField.setText(" | Numero di parole: " + documentTable.getSelectionModel().getSelectedItem().getWordsCount());
                if(event.getClickCount() == 2) {
                    try {
                        Desktop.getDesktop().open(new File(documentTable.getSelectionModel().getSelectedItem().getPath()));
                    } catch (IOException ex) { }
                }
            }
        });
    }
    
    /**
     * Reset the vocabulary and the list of document.
     */
    private void clearVariables() {
        vocabulary.clear();
        docs.clear();
        obsVocabulary.clear();
        obsDocs.clear();
    }
    
}
