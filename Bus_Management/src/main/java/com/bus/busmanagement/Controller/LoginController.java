package com.bus.busmanagement.Controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.bus.busmanagement.BusManagementApp;
import com.bus.busmanagement.DataAccess.AdminDAO;
import com.bus.busmanagement.Entity.Admin;
import com.bus.busmanagement.FxmlLoader;
import com.bus.busmanagement.Validation.SignUpValidation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    AnchorPane logoApp;
    @FXML
    AnchorPane signInForm;
    @FXML
    AnchorPane signUpForm;
    @FXML
    private Hyperlink create_acc;
    @FXML
    private Hyperlink login;
    @FXML
    private TextField signInUsername;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private TextField signUpUsername;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpConfirmPassword;
    @FXML
    private FontIcon showPassword;
    @FXML
    private FontIcon hidePassword;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    Button btnLogin;
    @FXML
    Button btnRegister;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private AdminDAO adminDAO = new AdminDAO();
    public void loginToDashBoard(ActionEvent event) {
        String username = signInUsername.getText();
        String password = signInPassword.getText();
        if(username.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Username or password is empty");
            alert.setContentText("Please enter your username and password");
            alert.showAndWait();
        } else {
            Admin admin = adminDAO.getByUsername(username);
            if(admin!=null){
                if(BCrypt.verifyer().verify(password.toCharArray(),admin.getPassword()).verified){
                    FXMLLoader loader = new FXMLLoader(BusManagementApp.class.getResource("Dashboard.fxml"));
                    try {
                        root = loader.load();
                        DashboardController dashboardController = loader.getController();
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        // Set the stage in the middle of the screen
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Password is incorrect");
                    alert.setContentText("Please enter your password again");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Username is incorrect");
                alert.setContentText("Please enter your username again");
                alert.showAndWait();
            }
        }
    }
    public void register(){
        String username = signUpUsername.getText();
        if(username.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setHeaderText("Username is empty");
            alert.setContentText("Please enter your username");
            alert.showAndWait();
        } else if (!SignUpValidation.isUsernameValid(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign Up Error");
            alert.setHeaderText("Username is invalid");
            alert.setContentText("Username must be from 3 to 20 characters" +
                    "\nMust not contain special characters except (.) (_) and (-)");
            alert.showAndWait();
        } else{
            String password = signUpPassword.getText();
            if(password.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Error");
                alert.setHeaderText("Password is empty");
                alert.setContentText("Please enter your password");
                alert.showAndWait();
            } else if (!SignUpValidation.isPasswordValid(password)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Error");
                alert.setHeaderText("Password is invalid");
                alert.setContentText("Password must be from 6 to 20 characters"+
                        "\n must contain at least 1 uppercase letter, " +
                        "1 lowercase letter, 1 number, and 1 special character");
                alert.showAndWait();
            }
            else {
                String confirmPassword = signUpConfirmPassword.getText();
                if(confirmPassword.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sign Up Error");
                    alert.setHeaderText("Confirm Password is empty");
                    alert.setContentText("Please enter your confirm password");
                    alert.showAndWait();
                } else if (!SignUpValidation.isConfirmPasswordValid(password,confirmPassword)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sign Up Error");
                    alert.setHeaderText("Confirm Password is invalid");
                    alert.setContentText("Confirm Password must be the same as Password");
                    alert.showAndWait();
                }
                else {
                    String passwordEncoder = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                    Admin newAccount= new Admin(1,username, passwordEncoder);
                    int rs = adminDAO.add(newAccount);
                    if(rs == -1){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Sign Up Error");
                        alert.setHeaderText("Username is already exist");
                        alert.setContentText("Please use another username");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sign Up Success");
                        alert.setHeaderText("Sign Up Success");
                        alert.setContentText("Please login to continue");
                        alert.showAndWait();
                        signUpUsername.setText("");
                        signUpPassword.setText("");
                        signUpConfirmPassword.setText("");
                    }
                }
            }
        }

    }
    public void changeForm(ActionEvent event){
        if(event.getSource() == create_acc){
            TranslateTransition translate = new TranslateTransition();
            translate.setNode(logoApp);
            translate.setDuration(Duration.millis(1000));
            translate.setByX(400);
            translate.setAutoReverse(true);
            translate.play();

            FadeTransition fade = new FadeTransition();
            fade.setNode(signUpForm);
            fade.setDuration(Duration.millis(1500));
            fade.setInterpolator(Interpolator.LINEAR);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } else if (event.getSource() == login) {
            TranslateTransition translate = new TranslateTransition();
            translate.setNode(logoApp);
            translate.setDuration(Duration.millis(1000));
            translate.setByX(-400);
            translate.setAutoReverse(true);
            translate.play();

            FadeTransition fade = new FadeTransition();
            fade.setNode(signInForm);
            fade.setDuration(Duration.millis(1500));
            fade.setInterpolator(Interpolator.LINEAR);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordLabel.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        hidePassword.setVisible(false);
        showPassword.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Show password
                passwordLabel.setVisible(true);
                confirmPasswordLabel.setVisible(true);
                passwordLabel.textProperty().bindBidirectional(signUpPassword.textProperty());
                confirmPasswordLabel.textProperty().bindBidirectional(signUpConfirmPassword.textProperty());
                showPassword.setVisible(false);
                hidePassword.setVisible(true);
            }
        });
        hidePassword.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Hide password
                passwordLabel.setVisible(false);
                confirmPasswordLabel.setVisible(false);
                showPassword.setVisible(true);
                hidePassword.setVisible(false);
            }
        });
        // LOGIN BY KEY ENTER
        signInForm.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    String username = signInUsername.getText();
                    String password = signInPassword.getText();
                    if(username.isEmpty() || password.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Error");
                        alert.setHeaderText("Username or password is empty");
                        alert.setContentText("Please enter your username and password");
                        alert.showAndWait();
                    } else {
                        Admin admin = adminDAO.getByUsername(username);
                        if(admin!=null){
                            if(BCrypt.verifyer().verify(password.toCharArray(),admin.getPassword()).verified){
                                FXMLLoader loader = new FXMLLoader(BusManagementApp.class.getResource("Dashboard.fxml"));
                                try {
                                    root = loader.load();
                                    DashboardController dashboardController = loader.getController();
                                    stage = (Stage) signInForm.getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    // Set the stage in the middle of the screen
                                    stage.centerOnScreen();
                                    stage.show();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Login Error");
                                alert.setHeaderText("Password is incorrect");
                                alert.setContentText("Please enter your password again");
                                alert.showAndWait();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login Error");
                            alert.setHeaderText("Username is incorrect");
                            alert.setContentText("Please enter your username again");
                            alert.showAndWait();
                        }
                    }
                }
            }
        });
        // SIGN UP BY KEY ENTER
        signUpForm.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String username = signUpUsername.getText();
                if(username.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sign Up Error");
                    alert.setHeaderText("Username is empty");
                    alert.setContentText("Please enter your username");
                    alert.showAndWait();
                } else if (!SignUpValidation.isUsernameValid(username)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sign Up Error");
                    alert.setHeaderText("Username is invalid");
                    alert.setContentText("Username must be from 3 to 20 characters" +
                            "\nMust not contain special characters except (.) (_) and (-)");
                    alert.showAndWait();
                } else{
                    String password = signUpPassword.getText();
                    if(password.isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Sign Up Error");
                        alert.setHeaderText("Password is empty");
                        alert.setContentText("Please enter your password");
                        alert.showAndWait();
                    } else if (!SignUpValidation.isPasswordValid(password)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Sign Up Error");
                        alert.setHeaderText("Password is invalid");
                        alert.setContentText("Password must be from 6 to 20 characters"+
                                "\n must contain at least 1 uppercase letter, " +
                                "1 lowercase letter, 1 number, and 1 special character");
                        alert.showAndWait();
                    }
                    else {
                        String confirmPassword = signUpConfirmPassword.getText();
                        if(confirmPassword.isEmpty()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Sign Up Error");
                            alert.setHeaderText("Confirm Password is empty");
                            alert.setContentText("Please enter your confirm password");
                            alert.showAndWait();
                        } else if (!SignUpValidation.isConfirmPasswordValid(password,confirmPassword)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Sign Up Error");
                            alert.setHeaderText("Confirm Password is invalid");
                            alert.setContentText("Confirm Password must be the same as Password");
                            alert.showAndWait();
                        }
                        else {
                            String passwordEncoder = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                            Admin newAccount= new Admin(1,username, passwordEncoder);
                            int rs = adminDAO.add(newAccount);
                            if(rs == -1){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Sign Up Error");
                                alert.setHeaderText("Username is already exist");
                                alert.setContentText("Please use another username");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Sign Up Success");
                                alert.setHeaderText("Sign Up Success");
                                alert.setContentText("Please login to continue");
                                alert.showAndWait();
                                signUpUsername.setText("");
                                signUpPassword.setText("");
                                signUpConfirmPassword.setText("");
                            }
                        }
                    }
                }
            }
        });

    }

}
