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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;
import java.util.regex.Pattern;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class LoginController extends MainSceneController {
    @FXML
    private TextField usernameField ;

    @FXML
    private PasswordField passwordField ;

    @FXML
    private Text errormsg;


    @FXML
    private void login() throws IOException {
        String user = usernameField.getText();
        String password = passwordField.getText();
        if(!checkEmpty(user, password)){
            if(ServerCommunication.userLogin(user, password).equals("Logged in")){
                changeTopBtn();
                goToBuildingScene();
            }
        }
        System.out.println(loggedIn);
        System.out.println(user + password);
    }

    public boolean checkEmpty(String user, String pwd){
        if(user.isEmpty() || pwd.isEmpty()){
            errormsg.setText("Please fill in all the fields");
            return true;
        }
        else {
            return false;
        }
    }


}
