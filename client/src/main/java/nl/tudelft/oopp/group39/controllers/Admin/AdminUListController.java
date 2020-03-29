package nl.tudelft.oopp.group39.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.User;


public class AdminUListController extends MainSceneController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadUsers();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadUsers() throws JsonProcessingException {
        usertable.setVisible(true);
        usertable.getItems().clear();
        usertable.getColumns().clear();
        String users = ServerCommunication.get(ServerCommunication.user);
        System.out.println(users);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        System.out.println(users);
        User[] list = mapper.readValue(users, User[].class);
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
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminAddBuilding.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    public void deleteUserView(User user) throws IOException {
        String id = user.getUsername();
        ServerCommunication.removeUser(id);
        createAlert("removed: " + user.getUsername());
        loadUsers();
    }

    public void updateUserView(User user) throws IOException {
//        Stage currentstage = (Stage) backbtn.getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminUpdateUser.fxml"));
//        Parent root = loader.load();
//        AdminUpdateUserController controller = loader.getController();
//        controller.initData(user);
//        currentstage.setScene(new Scene(root, 700, 600));
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

}
