package nl.tudelft.oopp.group39.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;
import java.util.regex.Pattern;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class LoginController {
    @FXML
    private TextField usernameField ;

    @FXML
    private PasswordField passwordField ;

    @FXML
    private Button signupbtn;

    @FXML
    private void login() {
        String user = usernameField.getText();
        String password = passwordField.getText();
        if(!checkEmpty(user, password)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Log in");
            alert.setHeaderText(null);
            alert.setContentText(ServerCommunication.userLogin(user, password));
            alert.showAndWait();
        }
        System.out.println(user + password);
    }

    public boolean checkEmpty(String user, String pwd){
        if(user.isEmpty()  | pwd.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return true;
        }
        else {
            return false;
        }
    }

    @FXML
    private void switchSignup(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) signupbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }


}
