package nl.tudelft.oopp.group39.controllers.admin.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.controllers.admin.room.RoomListController;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

public class UserCreateController extends RoomListController {

    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
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


    public void customInit() {
        try {
            initData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }
    /**
     * Initializes data needed for user.
     * @throws JsonProcessingException when there is a processing exception.
     */

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
     * Goes back to main User panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchUserView(currentStage);
    }
    /**
     * Gets the values inputted by admin to be used for creating user.
     * @throws IOException when no values have been inputted.
     */

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
    /**
     * Checks to see if input is valid.
     * @param name Name inputted
     * @param roleNull has a role been picked?
     * @param password and passwordConfirmation the password to be set for user.
     */

    public void verifyInputs(
        String name,
        String roleObj,
        boolean roleNull,
        String password,
        String passwordConfirmation) throws IOException {
        userMessage.setStyle("-fx-text-fill: Red");
        if (name == null || name.contentEquals("")) {
            userMessage.setText("Please select a name!\n");
            return;
        }
        if (roleNull) {
            String abcString = userMessage.getText();
            userMessage.setText(abcString + "Please select a role!\n");
            return;
        }
        for (User user : this.users) {
            if (user.getUsername().contentEquals(name)) {
                String abcString = userMessage.getText();
                userMessage.setText(abcString + "This username was already taken!\n");
                return;
            }
        }
        if (password == null
            || passwordConfirmation == null
            || password.contentEquals("")
            || passwordConfirmation.contentEquals("")) {
            String abcString = userMessage.getText();
            userMessage.setText(abcString + "Please input a password and confirmation password!\n");
            return;
        }
        if (password.length() < 8 || passwordConfirmation.length() < 8) {
            String abcString = userMessage.getText();
            userMessage.setText(abcString + "Password must be longer than 8 characters!\n");
            return;
        }
        if (!password.contentEquals(passwordConfirmation)) {
            String abcString = userMessage.getText();
            userMessage.setText(abcString + "The password and password confirmation must match!\n");
            return;
        }
        String email = "tudelft.nl";
        email = roleObj.contentEquals("STUDENT") ? "student." + email : email;
        email = name + "@" + email;
        ServerCommunication.createUser(name, email, roleObj, password);
        getBack();
    }

}
