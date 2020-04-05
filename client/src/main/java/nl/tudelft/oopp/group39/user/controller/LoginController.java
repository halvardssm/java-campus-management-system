package nl.tudelft.oopp.group39.user.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;
//import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class LoginController extends AbstractSceneController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errormsg;

    /**
     * Logs the user in.
     */
    public void login() throws IOException {
        String user = usernameField.getText();
        String password = passwordField.getText();
        if (!checkEmpty(user, password)) {
            if (ServerCommunication.userLogin(user, password).equals("Logged in")) {
                changeUserBox();
                goToBuildingScene();
            } else {
                createAlert("Wrong username or password");
            }
        }
        System.out.println(loggedIn);
    }

    /**
     * Check if username and password fields aren't empty.
     *
     * @param user username String
     * @param pwd  password String
     */
    public boolean checkEmpty(String user, String pwd) {
        if (user.isEmpty() || pwd.isEmpty()) {
            errormsg.setText("Please fill in all the fields");
            return true;
        } else {
            return false;
        }
    }
}
