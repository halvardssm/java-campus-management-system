package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import nl.tudelft.oopp.group39.models.Building;
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
    @FXML
    private TableColumn<Room, Room> roomDelCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Room, Room> roomUpdateCol = new TableColumn<>("Update");


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadRooms();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display rooms and data in tableView buildingTable. -- Likely doesn't work yet. Need to add nameCol.
     */
    void loadRooms() throws JsonProcessingException {
        roomTable.setVisible(true);
        roomTable.getItems().clear();
        roomTable.getColumns().clear();
        String rooms = ServerCommunication.get(ServerCommunication.room);
        System.out.println(rooms);

        ArrayNode body = (ArrayNode) mapper.readTree(rooms).get("body");
        rooms = mapper.writeValueAsString(body);
        Room[] list = mapper.readValue(rooms, Room[].class);
        ObservableList<Room> data = FXCollections.observableArrayList(list);
        roomBuildingIdCol.setCellValueFactory(new PropertyValueFactory<>("building"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        roomCapacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        roomStaffCol.setCellValueFactory(new PropertyValueFactory<>("onlyStaff"));
        roomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        roomDelCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        roomDelCol.setCellFactory(param -> new TableCell<Room, Room>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);

                if (room == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    event -> {
                        try {
                            deleteRoomView(room);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        roomUpdateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        roomUpdateCol.setCellFactory(param -> new TableCell<Room, Room>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);

                if (room == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            updateRoomField(room);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        roomTable.setItems(data);
        roomTable.getColumns().addAll(roomBuildingIdCol, roomIdCol, roomCapacityCol, roomStaffCol, roomNameCol, roomDelCol, roomUpdateCol);
    }

    public void adminAddRoom() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminAddRoom.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    public void deleteRoomView(Room room) throws IOException {
        String id = Integer.toString((int) room.getId());
        ServerCommunication.removeRoom(id);
        createAlert("removed: " + room.getName());
        loadRooms();
    }

    public void updateRoomField(Room room) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminUpdateRoom.fxml"));
        Parent root = loader.load();
        AdminUpdateRoomController controller = loader.getController();
        controller.initData(room);
        currentstage.setScene(new Scene(root, 700, 600));
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

