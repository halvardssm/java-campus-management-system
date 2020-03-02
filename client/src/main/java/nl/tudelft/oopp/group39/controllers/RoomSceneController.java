package nl.tudelft.oopp.group39.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class RoomSceneController extends MainSceneController {

    @FXML
    public TextField roomBuildingIdField;

    @FXML
    public TextField roomCapacityField;

    @FXML
    public TextField roomDescriptionField;

    @FXML
    public TextField updateRoomField;

    public void newRoomButton() {
        String buildingId = roomBuildingIdField.getText();

        String roomCapacity = roomCapacityField.getText();

        String roomDescription = roomDescriptionField.getText();

        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
    }

    public void updateRoomButton() {
        String buildingId = roomBuildingIdField.getText();

        String roomCap = roomCapacityField.getText();

        String roomDesc = roomDescriptionField.getText();

        String roomID = updateRoomField.getText();

        roomID = roomID.contentEquals("") ? "1" : roomID;

        createAlert(ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID));
    }

    public void getRoomsButton() {
        createAlert(ServerCommunication.getRooms());
    }

    public void deleteRoomButton(ActionEvent actionEvent) {
        String id = updateRoomField.getText();

        id = id.contentEquals("") ? "1" : id;

        ServerCommunication.removeRoom(id);

        createAlert(ServerCommunication.getRooms());
    }
}
