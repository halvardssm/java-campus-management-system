package nl.tudelft.oopp.group39.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;
import nl.tudelft.oopp.group39.user.model.User;

public class SignupController extends AbstractSceneController {
    @FXML private TextField emailField;
    @FXML private TextField netIdField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmpasswordField;
    @FXML private Label errormsg;

    /**
     * User signup.
     */
    public void signup() throws JsonProcessingException {
        String email = emailField.getText();
        String netID = netIdField.getText();
        String password = passwordField.getText();
        String confirmpassword = confirmpasswordField.getText();

        if (checkEmpty(email, netID, password, confirmpassword)
            && isValid(email)
            && checkPwd(password, confirmpassword)) {
            String role = getRole(email);
            User user = new User(netID, email, password, null, role);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign up");
            alert.setHeaderText(null);
            alert.setContentText(ServerCommunication.addUser(user));
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

    /**
     * Checks if signup fields aren't empty.
     *
     * @param email   email String
     * @param userID  userId String
     * @param pwd     password String
     * @param confirm confirm password String
     * @return boolean: true if fields aren't empty, false otherwise
     */
    public boolean checkEmpty(String email, String userID, String pwd, String confirm) {
        if (email.isEmpty() || userID.isEmpty() || pwd.isEmpty() || confirm.isEmpty()) {
            alertErr("Please fill in all the fields");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets the error text if there is something wrong in the sigup fields.
     *
     * @param msg String message that will be set in the error text
     */
    public void alertErr(String msg) {
        errormsg.setText(msg);
    }

    /**
     * Checks if the email is a valid tu delft email address.
     *
     * @param email email that needs to be checked.
     * @return boolean: true if email is valid, false otherwise
     */
    public boolean isValid(String email) {
        String emailRegex =
            "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!"
                + "#$%&’*+/=?`{|}~^-]+)*@(student.)?tudelft.nl$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || !pat.matcher(email).matches()) {
            alertErr("Please provide a valid tudelft email address");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if password and confirm password are the same.
     *
     * @param pwd     password String
     * @param confirm confirm password String
     * @return boolean: true if they match, false otherwise
     */
    public boolean checkPwd(String pwd, String confirm) {
        if (pwd.equals(confirm)) {
            return true;
        } else {
            errormsg.setText("Passwords must be the same");
            return false;
        }
    }

    /**
     * Gets the role of the user.
     *
     * @param email email of the user
     * @return role String
     */
    public String getRole(String email) {
        String role = email.split("@")[1];
        if (role.contains("student")) {
            return "STUDENT";
        } else {
            return "STAFF";
        }
    }
}
