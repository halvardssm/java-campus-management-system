package nl.tudelft.oopp.group39.controllers;

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
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Room;

public class RoomSceneController extends MainSceneController {

    public long buildingId;

    public void setup(long buildingId, String name, String address){
        this.buildingId = buildingId;
        setBuildingDetails(name, address);
        //getRooms(buildingId);
    }

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

    public void getRoomsButton() {
        createAlert(ServerCommunication.getRooms());
    }

    /**
     * Doc. TODO Sven
     */
    public void deleteRoomButton(ActionEvent actionEvent) {
        String id = updateRoomField.getText();

        id = id.contentEquals("") ? "1" : id;

        ServerCommunication.removeRoom(id);

        createAlert(ServerCommunication.getRooms());
    }

    public void getAllRooms() {
        mapper.configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        rooms.getChildren().clear();
        try {
            String roomsString = ServerCommunication.getAllRooms();
            String testString = "{\"body\":[{\"id\":1,\"capacity\":10,\"name\":\"Ampere\",\"onlyStaff\":true,\"description\":\"test1\",\"facilities\":[],\"events\":[],\"bookings\":[],\"building\":1},{\"id\":2,\"capacity\":6,\"name\":\"test2\",\"onlyStaff\":true,\"description\":\"test2\",\"facilities\":[{\"id\":1,\"description\":\"smartboard\"}],\"events\":[],\"bookings\":[],\"building\":1}],\"error\":null}";

            ArrayNode body = (ArrayNode) mapper.readTree(testString).get("body");

            for (JsonNode roomJson : body) {
                String roomAsString = mapper.writeValueAsString(roomJson);
                Room room = mapper.readValue(roomAsString, Room.class);
                newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));

                Label name = (Label) newRoom.lookup("#roomname");
                name.setText(room.getName());
//                ArrayNode facilitiesArray = (ArrayNode) roomJson.get("facilities");
//                System.out.println(facilitiesArray);
//                List<String> facilities = new ArrayList<>();
//                for (JsonNode facilty : facilitiesArray) {
//                    facilities.add(facilty.get("description").asText());
//                }
//                room.setFacilities(facilities);
                String bDetails = room.getDescription()
                    + "\n" + "Capacity: " + room.getCapacity()
                    + "\n" + "Facilities: " + room.facilitiesToString();

                Label details = (Label) newRoom.lookup("#roomdetails");
                details.setText(bDetails);
                System.out.println(newRoom);

                rooms.getChildren().add(newRoom);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

//    public void getRooms(long buildingId){
//        rooms.getChildren().clear();
//        try {
//            String roomsString = ServerCommunication.getRooms(buildingId);
//            System.out.println(roomsString);
//
//            JsonObject body = ((JsonObject) JsonParser.parseString(roomsString));
//            JsonArray roomArray = body.getAsJsonArray("body");
//
//            for (JsonElement room : roomArray) {
//                newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));
//
//                Label name = (Label) newRoom.lookup("#roomname");
//                name.setText(((JsonObject) room).get("name").getAsString());
//
//                String bDetails = ((JsonObject) room).get("description").getAsString()
//                        + "\n" + "Capacity: " + ((JsonObject) room).get("capacity").getAsInt()
//                        + "\n" + "Facilities: " + ((JsonObject) room).get("facilities").getAsString();
//
//                Label details = (Label) newRoom.lookup("#roomdetails");
//                details.setText(bDetails);
//
//                rooms.getChildren().add(newRoom);
//            }
//        } catch (IOException e) {
//            createAlert("Error: Wrong IO");
//        }
//    }

    public void setBuildingDetails(String name, String address) {
        buildingInfo.setPadding(new Insets(15, 15, 15, 15));
        Label buildingName = new Label(name);
        buildingName.getStyleClass().add("buildingName");
        Label buildingAddress = new Label(address);
        buildingAddress.getStyleClass().add("buildingAddress");
        buildingInfo.getChildren().addAll(buildingName, buildingAddress);
    }

}
