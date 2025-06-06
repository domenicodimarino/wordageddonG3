package wordageddon;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import wordageddon.database.UtenteDAOSQL;
import wordageddon.service.UtenteService;

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

    private void showAlert(AlertType type, String title, String header, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
}

    private void handleSubmit() {
        String username = userField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Dati mancanti", "Username e password obbligatori", "Inserisci username e password.");
            return;
        }

        if (isLoginMode) {
            try {
                if (utenteService.login(username, password)) {
                    showAlert(AlertType.INFORMATION, "Login riuscito", null, "Benvenuto " + username + "!");
                    // TODO: passa alla schermata principale
                    userField.clear(); passwordField.clear(); confirmPasswordField.clear();
                } else {
                    showAlert(AlertType.ERROR, "Login fallito", "Credenziali non valide", "Assicurati di aver inserito correttamente username e password.");
                }
            } catch (Exception ex) {
                showAlert(AlertType.ERROR, "Errore", "Errore di sistema", "Impossibile contattare il database.");
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String confirmPassword = confirmPasswordField.getText();
            if (!password.equals(confirmPassword)) {
                showAlert(AlertType.ERROR, "Registrazione fallita", "Password diverse", "Devi inserire la stessa password in entrambi i campi dedicati!");
                return;
            }
            try {
                if (utenteService.registra(username, password)) {
                    showAlert(AlertType.INFORMATION, "Registrazione riuscita", null, "Registrazione completata! Ora puoi effettuare il login.");
                    toggleMode();
                    userField.clear(); passwordField.clear(); confirmPasswordField.clear();
                } else {
                    showAlert(AlertType.ERROR, "Registrazione fallita", "Username già in uso", "Questo username è già presente nel database. Effettua il login.");
                }
            } catch (Exception ex) {
                showAlert(AlertType.ERROR, "Errore", "Errore di sistema", "Impossibile contattare il database.");
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}