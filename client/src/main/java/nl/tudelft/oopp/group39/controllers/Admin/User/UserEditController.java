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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.Room.RoomListController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;

public class UserEditController extends RoomListController implements Initializable {

    private User user;
    @FXML
    private Button backbtn;
    @FXML
    private ComboBox roleBox;
    @FXML
    private TextField emailField;
    @FXML
    private List<String> roles;
    @FXML
    private TextArea dateMessage;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(User user) throws JsonProcessingException {
        this.user = user;
        String roles = ServerCommunication.getUserRoles();
        ArrayNode body = (ArrayNode) mapper.readTree(roles).get("body");
        roles = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(roles, String[].class);
        this.roles = Arrays.asList(list);
        ObservableList<String> data = FXCollections.observableArrayList(list);
        roleBox.setItems(data);
        roleBox.setPromptText(user.getRole());
        emailField.setPromptText(user.getEmail());
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/User/UserList.fxml", currentstage);
    }

    public void editUser() throws IOException {
        String name = emailField.getText();
        name = name.contentEquals("") ? user.getUsername() : name;
        Object roleObj = roleBox.getValue();
        String role = roleObj == null ? user.getRole() : roleObj.toString();
        String email = "tudelft.nl";
        email = role.contentEquals("STUDENT") ? "student." + email : email;
        email = name + "@" + email;
        ServerCommunication.updateUserAdmin(user.getUsername(), email, role);
        getBack();
        createAlert("Updated: " + user.getEmail());
    }

}
