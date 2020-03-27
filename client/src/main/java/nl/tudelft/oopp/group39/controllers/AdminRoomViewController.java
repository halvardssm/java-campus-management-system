package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Room;

public class AdminRoomViewController extends MainSceneController implements Initializable {
    @FXML
    private Button backbtn;
    @FXML
    private TextField updateRoomField;
    @FXML
    private TextField roomFacilitiesField;
    @FXML
    private TextField roomDescriptionField;
    @FXML
    private TextField roomBuildingIdField;
    @FXML
    private TextField roomOnlyStaffField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private TableView<Room> roomTable = new TableView<>();
    @FXML
    private TableColumn<Room, String> roomIdCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Room, String> roomBuildingIdCol = new TableColumn<>("Building ID");
    @FXML
    private TableColumn<Room, String> roomCapacityCol = new TableColumn<>("Capacity");
    @FXML
    private TableColumn<Room, String> roomFacilityCol = new TableColumn<>("Facilities");
    @FXML
    private TableColumn<Room, String> roomStaffCol = new TableColumn<>("Only staff");
    @FXML
    private TableColumn<Room, String> roomNameCol = new TableColumn<>("Name");


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadBuildings();
    }

    /**
     * Display rooms and data in tableView buildingTable. -- Likely doesn't work yet. Need to add nameCol.
     */
    void loadBuildings() {
        roomTable.setVisible(true);
        roomTable.getItems().clear();
        createAlert(ServerCommunication.get(ServerCommunication.building));


        roomBuildingIdCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomCapacityCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        roomFacilityCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        roomStaffCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        roomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }


    /**
     * Adds a new Room with auto-generated ID.
     */
    public void addRoom() throws IOException {
        String buildingId = roomBuildingIdField.getText();
        String roomCapacity = roomCapacityField.getText();
        String roomDescription = roomDescriptionField.getText();
        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));

    }

    /**
     * Updates room based on ID specified.
     */

    public void updateRoom() throws IOException {
        String buildingId = roomBuildingIdField.getText();
        String roomCap = roomCapacityField.getText();
        String roomDesc = roomDescriptionField.getText();
        String roomID = updateRoomField.getText();
        roomID = roomID.contentEquals("") ? "1" : roomID;

        createAlert(ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID));
    }

    /**
     * Delete room.
     */
    public void deleteRoom() throws IOException {
        String id = updateRoomField.getText();
        id = id.contentEquals("") ? "1" : id;
        ServerCommunication.removeRoom(id);

        createAlert(ServerCommunication.get(ServerCommunication.room));

    }


    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void switchBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }
}

