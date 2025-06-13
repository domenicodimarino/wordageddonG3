package wordageddon.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

/**
 * Classe di utilità per la gestione delle scene in un'applicazione JavaFX.
 * Fornisce metodi per cambiare scena e applicare fogli di stile CSS opzionali.
 */
public class SceneUtils {

    /**
     * Cambia la scena corrente della finestra associata al nodo sorgente evento, caricando un file FXML e, opzionalmente, applicando un file CSS.
     *
     * @param <T>       il tipo del controller associato alla nuova scena
     * @param eventSource il nodo che ha generato l'evento (utile per recuperare la finestra corrente)
     * @param fxmlPath    il percorso del file FXML da caricare
     * @param cssPath     il percorso del file CSS da applicare (può essere null o vuoto se non necessario)
     * @return il controller associato al file FXML caricato, oppure null in caso di errore
     */
    public static <T> T switchScene(Node eventSource, String fxmlPath, String cssPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if (cssPath != null && !cssPath.isEmpty()) {
                URL cssUrl = SceneUtils.class.getResource(cssPath);
                if(cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                }
            }

            Stage stage = (Stage) eventSource.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}