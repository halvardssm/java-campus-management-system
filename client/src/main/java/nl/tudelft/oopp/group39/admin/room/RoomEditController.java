package nl.tudelft.oopp.group39.admin.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;


public class RoomEditController extends RoomListController {
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Room room;
    private HashMap<String, Long> buildingsByName = new HashMap<>();
    private HashMap<Long, String> buildingsById = new HashMap<>();
    @FXML
    private Button backbtn;
    @FXML
    private TextField roomNameField;
    @FXML
    private TextField roomDescriptionField;
    @FXML
    private ComboBox<String> roomBuildingIdField;
    @FXML
    private ComboBox<String> roomOnlyStaffField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private TextArea dateMessage;
    @FXML
    private MenuBar navBar;

    /**
     * Initializes the scene.
     */
    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
    }

    /**
     * Initializes data into their respective boxes to be used for editing.
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    public void initData(Room room) throws JsonProcessingException {
        customInit();
        setNavBar(navBar, currentStage);
        this.room = room;
        System.out.println(
                room.getBuilding() + " " + this.buildingsById + " " + this.buildingsById.keySet());
        List<String> options = new ArrayList<>();
        options.add("All users");
        options.add("Only staff members");
        roomNameField.setPromptText(room.getName());
        roomDescriptionField.setPromptText(room.getDescription());
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> data = getData(b);
        roomBuildingIdField.setItems(data);
        String romName = this.buildingsById.get(room.getBuilding());
        roomBuildingIdField.setPromptText(romName);
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        roomOnlyStaffField.setItems(dataOptions);
        roomOnlyStaffField.setPromptText(getOnlyStaff(room));
        roomCapacityField.setPromptText(Integer.toString(room.getCapacity()));
    }

    /**
     * Goes back to main Room panel.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void getBack() throws IOException {
        switchRoomView(currentStage);
    }

    /**
     * Edits the values of the room and communicates it  to server.
     *
     * @throws IOException if an error occurs during loading
     */
    public void editRoom() throws IOException {
        String name = roomNameField.getText();
        name = name.contentEquals("") ? room.getName() : name;
        Object building = roomBuildingIdField.getValue();
        String roomCap = roomCapacityField.getText();
        if (!isValidNumb(roomCap)) {
            dateMessage.setStyle("-fx-text-fill: Red");
            dateMessage.setText("Please input a valid number as capacity!");
            return;
        }
        String buildingId = building == null ? Long.toString(
                this.room.getBuilding()) : Long.toString(
                        this.buildingsByName.get(building.toString()));
        roomCap = roomCap.contentEquals("") ? Integer.toString(room.getCapacity()) : roomCap;
        String roomDesc = roomDescriptionField.getText();
        String roomID = Long.toString(room.getId());
        String onlyStaffObj = roomOnlyStaffField.getValue();
        String onlyStaff = onlyStaffObj == null ? getOnlyStaff(room) : onlyStaffObj;
        onlyStaff = Boolean.toString((onlyStaff).contentEquals("Only staff members"));
        System.out.println(building + " " + buildingId + " " + this.room.getBuilding());
        ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID, onlyStaff, name);
        getBack();
    }

    /**
     * Checks if string is a number.
     */
    public boolean isValidNumb(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets a list of buildings.
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    public List<Building> getBuildings(String buildings) throws JsonProcessingException {
        System.out.println(buildings);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        return Arrays.asList(list);
    }

    /**
     * Gets the names of buildings.
     */
    public List<String> getBuildingNames(List<Building> buildings) {
        List<String> a = new ArrayList<>();
        for (Building building : buildings) {
            this.buildingsByName.put(building.getName(), building.getId());
            this.buildingsById.put(building.getId(), building.getName());
            a.add(building.getName());
        }
        return a;
    }
}
