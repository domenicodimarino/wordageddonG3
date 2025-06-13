package wordageddon;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import wordageddon.database.UtenteDAOSQL;
import wordageddon.model.StatoGioco;
import wordageddon.model.Utente;
import wordageddon.service.SessionManager;
import wordageddon.service.UtenteService;
import wordageddon.util.DialogUtils;
import wordageddon.util.SceneUtils;

/**
 * Controller per la gestione della schermata di login e registrazione di Wordageddon.
 * Gestisce la logica di autenticazione, registrazione e navigazione tra le relative schermate.
 */
public class LoginController implements Initializable {

    @FXML
    private Label labelSignInUp;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button submitButton;
    @FXML
    private Button changeSignButton;
    @FXML
    private Hyperlink privacyLink;
    @FXML
    private Hyperlink infoLink;

    private boolean isLoginMode = true;
    private UtenteService utenteService;

    /**
     * Inizializza il controller: imposta i listener dei pulsanti e dei link, 
     * e inizializza il servizio utente.
     *
     * @param url URL location utilizzata per risolvere i percorsi relativi all'oggetto root (può essere null).
     * @param rb  ResourceBundle utilizzato per localizzare l'interfaccia utente (può essere null).
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeSignButton.setOnAction(e -> toggleMode());
        submitButton.setOnAction(e -> handleSubmit());
        privacyLink.setOnAction(e -> apriPdf("privacy_info/PrivacyG3.pdf"));
        infoLink.setOnAction(e -> apriPdf("privacy_info/InfoG3.pdf"));
        utenteService = new UtenteService(new UtenteDAOSQL());
    }

    /**
     * Cambia la modalità tra login e registrazione, aggiornando la GUI di conseguenza.
     */
    private void toggleMode() {
        if (isLoginMode) {            
            labelSignInUp.setText("PAGINA DI REGISTRAZIONE");
            submitButton.setText("Registrati");
            changeSignButton.setText("Torna al login");
            confirmPasswordLabel.setVisible(true);
            confirmPasswordLabel.setManaged(true);
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            isLoginMode = false;
        } else {
            labelSignInUp.setText("PAGINA DI LOGIN");
            submitButton.setText("Login");
            changeSignButton.setText("Vai alla schermata di registrazione");
            confirmPasswordLabel.setVisible(false);
            confirmPasswordLabel.setManaged(false);
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            isLoginMode = true;
        }
    }

    /**
     * Gestisce l'invio del form sia in modalità login che registrazione.
     * Effettua controlli sui campi e mostra avvisi in caso di errore.
     * Se il login è riuscito si passa alla schermata principale; se la registrazione va a buon fine si torna alla schermata di login.
     */
    private void handleSubmit() {
        String username = userField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            DialogUtils.showAlert(AlertType.ERROR, "Dati mancanti", "Username e password obbligatori", "Inserisci username e password.");
            return;
        }

        if (isLoginMode) {
            try {
                if (utenteService.login(username, password)) {
                    Utente utente = utenteService.getUtente(username);
                    SessionManager.setUtente(utente);
                    DialogUtils.showAlert(AlertType.INFORMATION, "Login riuscito", null, "Benvenuto " + username + "!");
                    userField.clear(); passwordField.clear(); confirmPasswordField.clear();
                    goToMainScreen();
                } else {
                    DialogUtils.showAlert(AlertType.ERROR, "Login fallito", "Credenziali non valide", "Assicurati di aver inserito correttamente username e password.");
                }
            } catch (Exception ex) {
                DialogUtils.showAlert(AlertType.ERROR, "Errore", "Errore di sistema", "Impossibile contattare il database.");
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String confirmPassword = confirmPasswordField.getText();
            if (!password.equals(confirmPassword)) {
                DialogUtils.showAlert(AlertType.ERROR, "Registrazione fallita", "Password diverse", "Devi inserire la stessa password in entrambi i campi dedicati!");
                return;
            }
            try {
                if (utenteService.registra(username, password)) {
                    DialogUtils.showAlert(AlertType.INFORMATION, "Registrazione riuscita", null, "Registrazione completata! Ora puoi effettuare il login.");
                    toggleMode();
                    userField.clear(); passwordField.clear(); confirmPasswordField.clear();
                } else {
                    DialogUtils.showAlert(AlertType.ERROR, "Registrazione fallita", "Username già in uso", "Questo username è già presente nel database. Effettua il login.");
                }
            } catch (Exception ex) {
                DialogUtils.showAlert(AlertType.ERROR, "Errore", "Errore di sistema", "Impossibile contattare il database.");
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Passa alla schermata principale se il login ha avuto successo.
     */
    private void goToMainScreen() {
        SceneUtils.switchScene(submitButton, "/wordageddon/Resources/fxml/Wordageddon.fxml", "/wordageddon/Resources/css/style.css");
    }
    
    /**
     * Apre un file PDF informativo (privacy o info) tramite il visualizzatore PDF del sistema.
     *
     * @param relativePath percorso relativo del PDF da aprire.
     */
    private void apriPdf(String relativePath) {
        try {
            // Path assoluto relativo alla working directory
            File file = new File(relativePath);
            if (!file.exists()) {
                DialogUtils.showAlert(AlertType.ERROR, "ERRORE", null, "File non trovato: " + file.getAbsolutePath());
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                DialogUtils.showAlert(AlertType.ERROR, "ERRORE", null, "Il sistema non supporta l'apertura automatica dei PDF.");
            }
        } catch (IOException ex) {
            DialogUtils.showAlert(AlertType.ERROR, "ERRORE", null, "Errore nell'apertura del PDF: " + ex.getMessage());
        }
    }
}