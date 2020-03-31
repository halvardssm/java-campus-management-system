package nl.tudelft.oopp.group39.controllers.Admin.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.Room.RoomListController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;

public class UserCreateController extends RoomListController implements Initializable {

    private List<User> users;
    @FXML
    private Button backbtn;
    @FXML
    private ComboBox<String> roleBox;
    @FXML
    private TextField usernameField;
    @FXML
    private List<String> roles;
    @FXML
    private TextArea userMessage;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordConfirmField;
    @FXML
    private MenuBar navBar;


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }

    public void initData() throws JsonProcessingException {
        userMessage.setText("");
        String roles = ServerCommunication.getUserRoles();
        ArrayNode body = (ArrayNode) mapper.readTree(roles).get("body");
        roles = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(roles, String[].class);
        String users = ServerCommunication.get(ServerCommunication.user);
        ArrayNode bodyU = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(bodyU);
        User[] listU = mapper.readValue(users, User[].class);
        this.users = Arrays.asList(listU);

        this.roles = Arrays.asList(list);
        ObservableList<String> data = FXCollections.observableArrayList(list);
        roleBox.setItems(data);
        roleBox.setPromptText("Select a role:");
        usernameField.setPromptText("Username");
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/User/UserList.fxml", currentstage);
    }

    @FXML
    public void createUser() throws IOException {
        userMessage.setText("");
        String email = usernameField.getText();
        String password = passwordField.getText();
        String passwordConfirmation = passwordConfirmField.getText();
        String roleObj = roleBox.getValue();
        boolean roleNull = roleObj == null;
        verifyInputs(email, roleObj, roleNull, password, passwordConfirmation);
    }

    public void verifyInputs(String name, String roleObj, boolean roleNull, String password, String passwordConfirmation) throws IOException {
        if (name == null || name.contentEquals("")) {
            userMessage.setStyle("-fx-text-fill: Red");
            userMessage.setText("Please select a name!\n");
            return;
        }
        if (roleNull) {
            userMessage.setStyle("-fx-text-fill: Red");
            String cString = userMessage.getText();
            userMessage.setText(cString + "Please select a role!\n");
            return;
        }
        for (User user : this.users) {
            if(user.getUsername().contentEquals(name)) {
                String cString = userMessage.getText();
                userMessage.setStyle("-fx-text-fill: Red");
                userMessage.setText(cString + "This username was already taken!\n");
                return;
            }
        }
        if (password == null || passwordConfirmation == null || password.contentEquals("") || passwordConfirmation.contentEquals("")) {
            userMessage.setStyle("-fx-text-fill: Red");
            String cString = userMessage.getText();
            userMessage.setText(cString + "Please input a password and a confirmation password!\n");
            return;
        }
        if (password.length() < 8 || passwordConfirmation.length() < 8) {
            userMessage.setStyle("-fx-text-fill: Red");
            String cString = userMessage.getText();
            userMessage.setText(cString + "Please input a password and a confirmation password of 8 or more characters!\n");
            return;
        }
        if (!password.contentEquals(passwordConfirmation)) {
            userMessage.setStyle("-fx-text-fill: Red");
            String cString = userMessage.getText();
            userMessage.setText(cString + "The password and the password confirmation must match!\n");
            return;
        }
        String role = roleObj;
        String email = "tudelft.nl";
        email = role.contentEquals("STUDENT") ? "student." + email : email;
        email = name + "@" + email;
        ServerCommunication.createUser(name, email, role, password);
        getBack();
        createAlert("Created: " + email);
    }

}
