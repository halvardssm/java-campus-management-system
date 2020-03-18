package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class SignupController extends MainSceneController {
    @FXML
    private TextField emailField;

    @FXML
    private TextField netIDField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmpasswordField;

    @FXML
    private Label errormsg;

    @FXML
    private void signup() {
        String email = emailField.getText();
        String netID = netIDField.getText();
        String password = passwordField.getText();
        String confirmpassword = confirmpasswordField.getText();
        String role = getRole(email);
        if (checkEmpty(email, netID, password, confirmpassword) && isValid(email) && checkPwd(password, confirmpassword)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign up");
            alert.setHeaderText(null);
            alert.setContentText(ServerCommunication.addUser(netID, email, password, role));
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Go to log in");
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
                try {
                    goToLoginScene();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            alert.showAndWait();

        }

        System.out.println(email + netID + password + confirmpassword);
    }

    public boolean checkEmpty(String email, String userID, String pwd, String confirm){
        if(email.isEmpty() | userID.isEmpty()  | pwd.isEmpty() | confirm.isEmpty()){
            alertErr("Please fill in all the fields");
            return false;
        }
        else {
            return true;
        }
    }

    public void alertErr(String msg){
        errormsg.setText(msg);
    }

    public boolean isValid(String email) {
        String emailRegex = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@(student.)?tudelft.nl$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || pat.matcher(email).matches() == false){
            alertErr("Please provide a valid tudelft email address");
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkPwd(String pwd, String confirm){
        if(!pwd.equals(confirm)){
            alertErr("Passwords must be the same");
            return false;
        }
        else{
            return true;
        }
    }

    public String getRole(String email){
        if(email.contains("student")){
            return "student";
        }
        else{
            return "staff";
        }
    }
}
