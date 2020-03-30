package nl.tudelft.oopp.group39.room.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.building.controller.BuildingCellController;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.MainSceneController;

public class RoomSceneController extends MainSceneController {

    private Building building;

    @FXML
    public TextField roomBuildingIdField;

    @FXML
    public TextField roomCapacityField;

    @FXML
    public TextField roomDescriptionField;

    @FXML
    public TextField updateRoomField;

    @FXML
    private FlowPane rooms;

    @FXML
    private GridPane newRoom;

    @FXML
    private VBox buildingInfo;

    @FXML
    public Label buildingName;

    /**
     * Sets up the page to show rooms for selected building.
     *
     * @param building where the rooms are located
     */
    public void setup(Building building) {
        this.building = building;
        setBuildingDetails(building.getName(), building.getDescription());
        getRooms(building.getId());
    }

    /**
     * Doc. TODO Sven
     */
    public void newRoom() {
        String buildingId = roomBuildingIdField.getText();

        String roomCapacity = roomCapacityField.getText();

        String roomDescription = roomDescriptionField.getText();

        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
    }

    /**
     * Doc. TODO Sven
     */
    public void updateRoom() {
        String buildingId = roomBuildingIdField.getText();

        String roomCap = roomCapacityField.getText();

        String roomDesc = roomDescriptionField.getText();

        String roomID = updateRoomField.getText();

        roomID = roomID.contentEquals("") ? "1" : roomID;

        String roomReservations = updateRoomField.getText();

        createAlert(ServerCommunication.updateRoom(
            buildingId,
            roomCap,
            roomDesc,
            roomID,
            roomReservations
        ));
    }

    /**
     * Doc. TODO Sven
     */
    public void getRooms() {
        createAlert(ServerCommunication.get(ServerCommunication.room));
    }

    /**
     * Doc. TODO Sven
     */
    public void deleteRoom() {
        String id = updateRoomField.getText();

        id = id.contentEquals("") ? "1" : id;

        ServerCommunication.removeRoom(id);

        createAlert(ServerCommunication.get(ServerCommunication.room));
    }

    /**
     * Shows the rooms.
     *
     * @param json json string that holds the rooms that needs to be parsed
     */
    public void showRooms(String json) {
        System.out.println(json);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        rooms.getChildren().clear();
        try {

            ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
            String roomAsString = mapper.writeValueAsString(body);

            Room[] list = mapper.readValue(roomAsString, Room[].class);

            for (Room room : list) {
                FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/room/roomCell.fxml"));
                GridPane newRoom = loader.load();
                RoomCellController controller = loader.getController();
                controller.createPane(room,building);
                rooms.getChildren().add(newRoom);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Gets rooms for a selected building.
     *
     * @param buildingId id of the selected building
     */
    public void getRooms(long buildingId) {
        String roomString = ServerCommunication.getRooms(buildingId);
        showRooms(roomString);
    }

    /**
     * Gets all rooms.
     */
    public void getAllRooms() {
        String roomsString = ServerCommunication.get(ServerCommunication.room);
        showRooms(roomsString);
    }

    /**
     * Sets the building details in the header of the page.
     *
     * @param name    name of the building
     * @param address address of the building
     */
    public void setBuildingDetails(String name, String address) {
        ((Label) buildingInfo.lookup("buildingName"))
            .setText(name);
        ((Label) buildingInfo.lookup("buildingAddress"))
            .setText(address);
    }

}
