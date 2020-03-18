package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.List;
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

    public void getAllRooms(){
        rooms.getChildren().clear();
        try {
            String roomsString = ServerCommunication.getAllRooms();
            System.out.println(roomsString);

            ArrayNode body = (ArrayNode) mapper.readTree(roomsString).get("body");

            for (JsonNode roomJson : body) {
                String roomstr = mapper.writeValueAsString(roomJson);

                Room room = mapper.readValue(roomstr, Room.class);
                newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));

                Label name = (Label) newRoom.lookup("#roomname");
                name.setText(room.getName());
                List<String> facilitiesArray = room.getFacilities();
                String facilities = "";
                for (String facilty : facilitiesArray) {
                    facilities = facilities + facilty + ", ";
                }

                String bDetails = room.getDescription()
                    + "\n" + "Capacity: " + room.getCapacity()
                    + "\n" + "Facilities: " + facilities;

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
