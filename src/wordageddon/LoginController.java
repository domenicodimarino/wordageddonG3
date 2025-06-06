package wordageddon;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeSignButton.setOnAction(e -> toggleMode());
        submitButton.setOnAction(e -> handleSubmit());
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
        if (isLoginMode) {
            // Logica login
        } else {
            // Logica registrazione: controlla che password e conferma siano uguali!
        }
    }
}