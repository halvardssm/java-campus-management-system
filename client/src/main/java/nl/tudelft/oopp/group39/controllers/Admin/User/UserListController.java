package nl.tudelft.oopp.group39.controllers.Admin.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.User;


public class UserListController extends MainAdminController implements Initializable {

    private String allRoles = "All Roles";
    @FXML
    private Button backbtn;
    @FXML private TableView<User> usertable;
    @FXML private TableColumn<User, String> idCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> statusCol;
    @FXML
    private TableColumn<User, User> userDelCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<User, User> userUpCol = new TableColumn<>("Update");
//    @FXML
//    private ComboBox<String> roleBox;
    @FXML
    private TextField usernameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadUsersStandard();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    void loadUsersStandard() throws JsonProcessingException {
        String users = ServerCommunication.get(ServerCommunication.user);
        loadUsers(users);
    }

    public void filterUsers() throws JsonProcessingException {
        String name = usernameField.getText();
        name = name == null ? "" : name;
//        String role = roleBox.getValue();
//        role = role == null ? "" : role;
//        role = role.contentEquals("") ? role : "";
        String users = ServerCommunication.getFilteredUsers(name);
        System.out.println(users);
        loadUsers(users);
    }

    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }

    void loadFiltering(User[] list) {
        List<String> nList = new ArrayList<>();
        for(User user : list) {
            nList.add(user.getRole());
        }
        nList.add(allRoles);
        ObservableList<String> data = FXCollections.observableArrayList(nList);
//        roleBox.setItems(data);
//        roleBox.setPromptText(allRoles);
    }
    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadUsers(String users) throws JsonProcessingException {
        usertable.setVisible(true);
        usertable.getItems().clear();
        usertable.getColumns().clear();
        System.out.println(users);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        System.out.println(users);
        User[] list = mapper.readValue(users, User[].class);
        loadFiltering(list);
        ObservableList<User> data = FXCollections.observableArrayList(list);
        idCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        userDelCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        userDelCol.setCellFactory(param -> new TableCell<User, User>() {
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
                            deleteUserView(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        userUpCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        userUpCol.setCellFactory(param -> new TableCell<User, User>() {
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
                            updateUserView(user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        usertable.setItems(data);
        usertable.getColumns().addAll(idCol, emailCol, statusCol, userDelCol, userUpCol);
    }


    public void adminAddBuilding() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/Building/BuildingCreate.fxml"));
        currentstage.setScene(new Scene(root, 900, 650));
    }

    public void deleteUserView(User user) throws IOException {
        String id = user.getUsername();
        ServerCommunication.removeUser(id);
        createAlert("removed: " + user.getUsername());
        loadUsersStandard();
    }

    public void updateUserView(User user) throws IOException {
//        Stage currentstage = (Stage) backbtn.getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminUpdateUser.fxml"));
//        Parent root = loader.load();
//        AdminUpdateUserController controller = loader.getController();
//        controller.initData(user);
//        currentstage.setScene(new Scene(root, 900, 650));
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 900, 650));
    }

}
