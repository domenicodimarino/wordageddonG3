package wordageddon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import wordageddon.database.PunteggioDAOSQL;
import wordageddon.model.Punteggio;
import wordageddon.util.SceneUtils;
import wordageddon.service.SessionManager;
import wordageddon.model.Utente;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.cell.PropertyValueFactory;

public class StoricoController implements Initializable {

    @FXML
    private Label titolo;
    @FXML
    private TableView<Punteggio> resultsTable;
    @FXML
    private TableColumn<Punteggio, Integer> correctanswers;
    @FXML
    private TableColumn<Punteggio, Integer> temporesiduo;
    @FXML
    private TableColumn<Punteggio, String> difficoltascelta;
    @FXML
    private TableColumn<Punteggio, Integer> Totpunteggio;
    @FXML
    private TableColumn<Punteggio, String> data;
    @FXML
    private Label bestscore;
    @FXML
    private Label mediapunti;
    @FXML
    private Button menu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup colonne
        correctanswers.setCellValueFactory(new PropertyValueFactory<>("risposteCorrette"));
        temporesiduo.setCellValueFactory(new PropertyValueFactory<>("tempoResiduo"));
        Totpunteggio.setCellValueFactory(new PropertyValueFactory<>("valore"));

        difficoltascelta.setCellValueFactory(cellData -> {
            int diff = cellData.getValue().getDifficolta();
            String difficoltaString;
            switch (diff) {
                case 1: difficoltaString = "Facile"; break;
                case 2: difficoltaString = "Medio"; break;
                case 3: difficoltaString = "Difficile"; break;
                default: difficoltaString = "Sconosciuta"; break;
            }
            return new javafx.beans.property.SimpleStringProperty(difficoltaString);
        });

        data.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getData().toString()
            )
        );

        // --- SOLO punteggi dell'utente loggato ---
        ObservableList<Punteggio> punteggiList = FXCollections.observableArrayList();
        try {
            Utente utenteLoggato = SessionManager.getUtente();
            if (utenteLoggato != null) {
                String username = utenteLoggato.getUsername();
                PunteggioDAOSQL dao = new PunteggioDAOSQL();
                List<Punteggio> tuttiPunteggi = dao.elencaTutti();
                // Filtro per utente corrente
                List<Punteggio> punteggiUtente = tuttiPunteggi.stream()
                        .filter(p -> p.getUsername().equals(username))
                        .collect(Collectors.toList());
                punteggiList.addAll(punteggiUtente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultsTable.setItems(punteggiList);

        // Migliore e media
        if (!punteggiList.isEmpty()) {
            int max = punteggiList.stream().mapToInt(Punteggio::getValore).max().orElse(0);
            double media = punteggiList.stream().mapToInt(Punteggio::getValore).average().orElse(0.0);
            bestscore.setText(String.valueOf(max));
            mediapunti.setText(String.format("%.2f", media));
        } else {
            bestscore.setText("—");
            mediapunti.setText("—");
        }

        // Bottone menu
        menu.setOnAction(event -> {
        SceneUtils.switchScene(menu, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
        });
    }
}