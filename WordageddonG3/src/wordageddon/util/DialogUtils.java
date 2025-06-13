package wordageddon.util;

import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Classe di utilità per la gestione e visualizzazione di finestre di dialogo (Alert) in JavaFX.
 * Fornisce metodi statici per mostrare alert informativi, personalizzati e finestre di conferma.
 *
 * @author Gruppo 3
 */
public class DialogUtils {

    /**
     * Mostra una finestra di dialogo (Alert) di tipo specificato con titolo, intestazione e contenuto personalizzati.
     *
     * @param type    il tipo di alert da visualizzare (es. INFORMATION, WARNING, ERROR, ecc.)
     * @param title   il titolo della finestra di dialogo
     * @param header  il testo dell'intestazione dell'alert
     * @param content il messaggio da mostrare nel contenuto dell'alert
     */
    public static void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di dialogo (Alert) personalizzata con bottoni specificati.
     *
     * @param type     il tipo di alert da visualizzare
     * @param title    il titolo della finestra di dialogo
     * @param header   il testo dell'intestazione dell'alert
     * @param content  il messaggio da mostrare nel contenuto dell'alert
     * @param buttons  i bottoni da visualizzare nell'alert
     * @return un Optional contenente il ButtonType selezionato dall'utente, o vuoto se la finestra viene chiusa senza selezione
     */
    public static Optional<ButtonType> showCustomAlert(Alert.AlertType type, String title, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        return alert.showAndWait();
    }

    /**
     * Mostra una finestra di conferma con due opzioni ("Sì" e "No") e restituisce true se l'utente seleziona "Sì".
     *
     * @param titolo        il titolo della finestra di dialogo
     * @param intestazione  il testo dell'intestazione dell'alert
     * @param messaggio     il messaggio da mostrare nel contenuto dell'alert
     * @return true se l'utente seleziona "Sì", false altrimenti
     */
    public static boolean mostraConferma(String titolo, String intestazione, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(intestazione);
        alert.setContentText(messaggio);

        ButtonType yes = new ButtonType("Sì");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yes;
    }
}