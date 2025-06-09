package wordageddon.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class SceneUtils {

    /**
     * Cambia la scena corrente.
     * @param eventSource il Node di partenza (ad esempio il bottone che scatena l'azione)
     * @param fxmlPath path all'FXML (es: "/wordageddon/Resources/fxml/Wordageddon.fxml")
     * @param cssPath path al CSS (può essere null se non vuoi caricare un CSS aggiuntivo)
     * @return il Controller caricato, se serve
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