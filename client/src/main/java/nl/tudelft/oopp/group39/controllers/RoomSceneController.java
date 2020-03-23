package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Room;

public class RoomSceneController extends MainSceneController {

    public long buildingId;
    private boolean filterBarShown = false;

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
    private VBox filterBar;


    /**
     * Sets up the page to show rooms for selected building.
     *
     * @param buildingId id of the selected building
     * @param name       name of the selected building
     * @param address    of the selected building
     */
    public void setup(long buildingId, String name, String address) {
        this.buildingId = buildingId;
        setBuildingDetails(name, address);
        getRooms(buildingId);
    }

    /**
     * Doc. TODO Sven
     */
    public void newRoomButton() {
        String buildingId = roomBuildingIdField.getText();

        String roomCapacity = roomCapacityField.getText();

        String roomDescription = roomDescriptionField.getText();

        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
    }

    /**
     * Doc. TODO Sven
     */
    public void updateRoomButton() {
        String buildingId = roomBuildingIdField.getText();

        String roomCap = roomCapacityField.getText();

        String roomDesc = roomDescriptionField.getText();

        String roomID = updateRoomField.getText();

        roomID = roomID.contentEquals("") ? "1" : roomID;

        createAlert(ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID));
    }

    /**
     * Doc. TODO Sven
     */
    public void getRoomsButton() {
        createAlert(ServerCommunication.get(ServerCommunication.room));
    }

    /**
     * Doc. TODO Sven
     */
    public void deleteRoomButton(ActionEvent actionEvent) {
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

            for (JsonNode roomJson : body) {
                String roomAsString = mapper.writeValueAsString(roomJson);
                Room room = mapper.readValue(roomAsString, Room.class);
                newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));

                Label name = (Label) newRoom.lookup("#roomname");
                name.setText(room.getName());
                String roomDetails = room.getDescription()
                    + "\n" + "Capacity: " + room.getCapacity()
                    + "\n" + "Facilities: " + room.facilitiesToString();

                Label details = (Label) newRoom.lookup("#roomdetails");
                details.setText(roomDetails);
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
        buildingInfo.setPadding(new Insets(15, 15, 15, 15));
        Label buildingName = new Label(name);
        buildingName.getStyleClass().add("buildingName");
        Label buildingAddress = new Label(address);
        buildingAddress.getStyleClass().add("buildingAddress");
        buildingInfo.getChildren().addAll(buildingName, buildingAddress);
    }

    public void toggleFilterBar() {
        if (!filterBarShown) {
            Hyperlink buildings = new Hyperlink("Buildings");
            buildings.getStyleClass().add("sidebar-item");
            buildings.setOnAction(event -> {
                try {
                    goToBuildingScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink rooms = new Hyperlink("Rooms");
            rooms.getStyleClass().add("sidebar-item");
            rooms.setOnAction(event -> {
                try {
                    goToRoomsScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink bikerental = new Hyperlink("Bike rental");
            bikerental.getStyleClass().add("sidebar-item");
            bikerental.setOnAction(event -> {
                try {
                    goToBikeRentalScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink foodorder = new Hyperlink("Order food");
            foodorder.getStyleClass().add("sidebar-item");
            foodorder.setOnAction(event -> {
                try {
                    goToFoodOrderScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sidebar.getChildren().addAll(buildings, rooms, bikerental, foodorder);
            filterBarShown = true;
        } else {
            sidebar.getChildren().clear();
            filterBarShown = false;
        }
    }

}
