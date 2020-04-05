package nl.tudelft.oopp.group39.admin.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

public class UserEditController extends UserListController {

    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private User user;
    @FXML
    private Button backbtn;
    @FXML
    private ComboBox<String> roleBox;
    @FXML
    private TextField emailField;


    /**
     * Initializes the data of a User and makes it usable.
     * @param user Object user
     * @throws JsonProcessingException when there is a processing exception.
     */

    public void initData(User user) throws JsonProcessingException {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        this.user = user;
        String roles = ServerCommunication.getUserRoles();
        ArrayNode body = (ArrayNode) mapper.readTree(roles).get("body");
        roles = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(roles, String[].class);
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
        switchUserView(currentStage);
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
    }

}
