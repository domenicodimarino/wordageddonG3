package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wordageddon.database.PunteggioDAOSQL;
import wordageddon.model.Difficolta;
import wordageddon.model.Punteggio;
import wordageddon.util.SceneUtils;

public class ClassificaController implements Initializable {

    @FXML
    private TableView<LeaderboardRow> leaderboardTable;
    @FXML
    private TableColumn<LeaderboardRow, Integer> placeColumn;
    @FXML
    private TableColumn<LeaderboardRow, String> userColumn;
    @FXML
    private TableColumn<LeaderboardRow, Integer> correctColumn;
    @FXML
    private TableColumn<LeaderboardRow, Integer> difficultyColumn;
    @FXML
    private TableColumn<LeaderboardRow, Integer> scoreColumn;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;
    @FXML
    private Button menuBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Collega le colonne con i nomi dei getter di LeaderboardRow
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("posizione"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        correctColumn.setCellValueFactory(new PropertyValueFactory<>("risposteCorrette"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("punteggio"));

        // Carica i dati dal DB
        ObservableList<LeaderboardRow> data = FXCollections.observableArrayList();
        try {
            PunteggioDAOSQL dao = new PunteggioDAOSQL();
            List<Punteggio> punteggi = dao.elencaTutti();
            int pos = 1;
            for (Punteggio p : punteggi) {
                
                data.add(new LeaderboardRow(
                    pos++,
                    p.getUsername(),
                    p.getRisposteCorrette(),
                    Difficolta.valueOf(p.getDifficoltaEnum().toString()),
                    p.getValore()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        leaderboardTable.setItems(data);
        
        menuBtn.setOnAction(e -> goToMenu());
    }
    private void goToMenu() {
        SceneUtils.switchScene(menuBtn, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
}