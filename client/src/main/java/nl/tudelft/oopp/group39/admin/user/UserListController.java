package nl.tudelft.oopp.group39.admin.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

@SuppressWarnings("unchecked")
public class UserListController extends AdminPanelController {

    private ObjectMapper mapper = new ObjectMapper();
    private String lastSelectedRole;
    private String lastSelectedName;
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
    private ComboBox<String> roleBox;
    @FXML
    private TextField usernameField;
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit() {
        try {
            loadUsersStandard();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }
    /**
     * Loads users when no filtering is enabled.
     * @throws JsonProcessingException when there is a processing exception
     */

    public void loadUsersStandard() throws JsonProcessingException {
        this.lastSelectedRole = allRoles;
        this.lastSelectedName = "";
        String users = ServerCommunication.get(ServerCommunication.user);
        loadUsers(users);
    }
    /**
     * First filters through users based on criteria and then calls loadUsers.
     * @throws JsonProcessingException when there is a processing exception
     */

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
        ObservableList<String> data = FXCollections.observableArrayList(roleList);
        roleBox.setItems(data);
        roleBox.getSelectionModel().select(this.lastSelectedRole);
    }
    /**
     * Display users and data into tableView named userTable.
     */

    void loadUsers(String users) throws JsonProcessingException {
        loadFiltering();
        usertable.setVisible(true);
        usertable.getItems().clear();
        usertable.getColumns().clear();
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);

        idCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        User[] list = mapper.readValue(users, User[].class);
        updateCol.setCellFactory(param -> returnCell("Update"));
        ObservableList<User> data = FXCollections.observableArrayList(list);
        usertable.setItems(data);
        usertable.getColumns().addAll(idCol, emailCol, statusCol, deleteCol, updateCol);
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<User, User> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

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
                            switch (button) {
                                case "Update":
                                    editUser(user);
                                    break;
                                case "Delete":
                                    deleteUser(user);
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        };
    }

    /**
     * Switches scene to the createUser one.
     */
    public void createUser() throws IOException {
        FXMLLoader loader = switchFunc("/admin/user/UserCreate.fxml");
        UserCreateController controller = loader.getController();
        controller.customInit();
    }
    /**
     * Deletes selected user.
     */

    public void deleteUser(User user) throws IOException {
        String id = user.getUsername();
        ServerCommunication.removeUser(id);
        loadUsersStandard();
    }
    /**
     * Sends user to the user edit page.
     */

    public void editUser(User user) throws IOException {
        FXMLLoader loader = switchFunc("/admin/user/UserEdit.fxml");
        UserEditController controller = loader.getController();
        controller.initData(user);
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}
