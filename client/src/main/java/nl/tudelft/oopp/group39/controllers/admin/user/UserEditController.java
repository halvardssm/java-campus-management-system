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
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.admin.room.RoomListController;
import nl.tudelft.oopp.group39.user.model.User;

public class UserEditController extends RoomListController implements Initializable {

    private ObjectMapper mapper = new ObjectMapper();
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
    private MenuBar navBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }
    /**
     * Initializes the data of a User and makes it usable.
     * @param user Object user
     * @throws JsonProcessingException when there is a processing exception.
     */

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
     * Goes back to main User panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/admin/user/UserList.fxml", currentstage);
    }
    /**
     * Edits user values and sends them to database.
     */

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
//        createAlert("Updated: " + user.getEmail());
    }

}
