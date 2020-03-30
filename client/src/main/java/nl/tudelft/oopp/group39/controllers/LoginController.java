package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.MainSceneController;

public class LoginController extends MainSceneController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errormsg;

    /**
     * Logs the user in.
     */
    public void login() throws IOException {
        String user = usernameField.getText();
        String password = passwordField.getText();
        if (!checkEmpty(user, password)) {
            if (ServerCommunication.userLogin(user, password).equals("Logged in")) {
                changeTopBtn();
                goToBuildingScene();
            }
        }
        System.out.println(loggedIn);
        System.out.println(MainSceneController.user.getBookings());
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
