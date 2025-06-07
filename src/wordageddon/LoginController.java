package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import wordageddon.database.UtenteDAOSQL;
import wordageddon.model.Utente;
import wordageddon.service.SessionManager;
import wordageddon.service.UtenteService;
import wordageddon.util.DialogUtils;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeSignButton.setOnAction(e -> toggleMode());
        submitButton.setOnAction(e -> handleSubmit());
        utenteService = new UtenteService(new UtenteDAOSQL());
    }

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
    private void goToMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/wordageddon/Resources/fxml/Wordageddon.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
 
        scene.getStylesheets().add(getClass().getResource("/wordageddon/Resources/css/style.css").toExternalForm());
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}