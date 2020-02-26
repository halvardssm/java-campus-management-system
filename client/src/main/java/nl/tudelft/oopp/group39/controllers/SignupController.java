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

import javax.swing.*;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class SignupController {
    @FXML
    private TextField emailField ;

    @FXML
    private TextField userIDField ;

    @FXML
    private PasswordField passwordField ;

    @FXML
    private PasswordField confirmpasswordField ;

    @FXML
    private Button loginbtn;

    @FXML
    private void signup() {
        String email = emailField.getText();
        String user = userIDField.getText();
        String password = passwordField.getText();
        String confirmpassword = confirmpasswordField.getText();
        checkEmpty(email, user, password, confirmpassword);
        isInt(user);
        System.out.println(email + user + password + confirmpassword);
    }

    public boolean checkEmpty(String email, String userID, String pwd, String confirm){
        if(email.isEmpty() | userID.isEmpty()  | pwd.isEmpty() | confirm.isEmpty()){
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

    public boolean isInt(String str){
        try{
            int id = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in a valid user ID");
            alert.showAndWait();
            return false;
        }
        return true;
    }


    @FXML
    private void switchLogin(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) loginbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }
}
