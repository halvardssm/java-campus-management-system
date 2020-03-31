package nl.tudelft.oopp.group39.controllers.Admin.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.AdminPanelController;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.User;


public class UserListController extends AdminPanelController implements Initializable {

    private String lastSelectedRole;
    private String lastSelectedName;
    private List<String> roles;
    private String allRoles = "ALL ROLES";
    @FXML
    private Button backbtn;
    @FXML private TableView<User> usertable;
    @FXML private TableColumn<User, String> idCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> statusCol;
    @FXML
    private TableColumn<User, User> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<User, User> updateCol = new TableColumn<>("Update");
    @FXML
    private ComboBox roleBox;
    @FXML
    private TextField usernameField;
    @FXML
    private MenuBar navBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadUsersStandard();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);

    }

    public void loadUsersStandard() throws JsonProcessingException {
        this.lastSelectedRole = allRoles;
        this.lastSelectedName = "";
        String users = ServerCommunication.get(ServerCommunication.user);
        loadUsers(users);
    }

    public void filterUsers() throws JsonProcessingException {
        String name = usernameField.getText();
        name = name == null ? "" : name;
        this.lastSelectedName = name;
        Object roleObj = roleBox.getValue();
        String role = roleObj == null ? "" : roleObj.toString();
        this.lastSelectedRole = role.contentEquals("") ? allRoles : role;
        role = role.contentEquals(allRoles) ? "" : role;
        String users = ServerCommunication.getFilteredUsers(name, role);
        loadUsers(users);
    }

    void loadFiltering() throws JsonProcessingException {
        usernameField.clear();
        usernameField.setText(lastSelectedName);
        roleBox.getItems().clear();
        String roles = ServerCommunication.getUserRoles();
        ArrayNode body = (ArrayNode) mapper.readTree(roles).get("body");
        roles = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(roles, String[].class);
        List<String> roleList = new ArrayList<>();
        roleList.add(allRoles);
        roleList.addAll(Arrays.asList(list));
        this.roles = roleList;
        ObservableList<String> data = FXCollections.observableArrayList(roleList);
        roleBox.setItems(data);
        roleBox.getSelectionModel().select(this.lastSelectedRole);
    }
    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadUsers(String users) throws JsonProcessingException {
        loadFiltering();
        usertable.setVisible(true);
        usertable.getItems().clear();
        usertable.getColumns().clear();
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        User[] list = mapper.readValue(users, User[].class);
        ObservableList<User> data = FXCollections.observableArrayList(list);
        idCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> new TableCell<User, User>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (user == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    event -> {
                        try {
                            deleteUser(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> new TableCell<User, User>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (user == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            updateUser(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        usertable.setItems(data);
        usertable.getColumns().addAll(idCol, emailCol, statusCol, deleteCol, updateCol);
    }


    public void createUser() throws IOException {
        switchFunc("/Admin/User/UserCreate.fxml");
    }

    public void deleteUser(User user) throws IOException {
        String id = user.getUsername();
        ServerCommunication.removeUser(id);
        createAlert("removed: " + user.getUsername());
        loadUsersStandard();
    }

    public void updateUser(User user) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/User/UserEdit.fxml");
        UserEditController controller = loader.getController();
        controller.initData(user);
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        switchFunc("/Admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}
