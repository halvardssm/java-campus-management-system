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
import java.io.IOException;
import java.util.regex.Pattern;

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
        isValid(email);
        checkPwd(password, confirmpassword);
        System.out.println(email + user + password + confirmpassword);
    }

    public boolean checkEmpty(String email, String userID, String pwd, String confirm){
        if(email.isEmpty() | userID.isEmpty()  | pwd.isEmpty() | confirm.isEmpty()){
            alertErr("Please fill in all the fields");
            return true;
        }
        else {
            return false;
        }
    }

    public void alertErr(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public boolean isInt(String str){
        try{
            int id = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe){
            alertErr("Please provide a valid user ID");
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

    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || pat.matcher(email).matches() == false){
            alertErr("Please provide a valid email address");
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
}
