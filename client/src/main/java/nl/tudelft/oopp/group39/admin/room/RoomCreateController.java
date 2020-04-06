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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

// Suppress all only used for Suspicious call to 'HashMap.get' in line 126
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection, ALL")
public class RoomCreateController extends RoomListController {
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private HashMap<String, Long> buildingsByName = new HashMap<>();
    private HashMap<Long, Building> buildingById = new HashMap<>();
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
    private MenuBar navBar;

    /**
     * Initializes the scene.
     */
    public void customInit() {
        try {
            initData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Creates a list of buildings.
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
     * Returns a list of building names.
     */
    public List<String> getBuildingNames(List<Building> buildings) {
        List<String> a = new ArrayList<>();
        for (Building building : buildings) {
            this.buildingsByName.put(building.getName(), building.getId());
            this.buildingById.put(building.getId(), building);
            a.add(building.getName());
        }
        return a;
    }

    /**
     * Initializes data for usage in creating room.
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    public void initData() throws JsonProcessingException {
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> data = getData(b);
        List<String> options = new ArrayList<>();
        options.add("All users");
        options.add("Only staff members");
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        roomBuildingIdField.setItems(data);
        roomBuildingIdField.setPromptText(data.get(0));
        roomOnlyStaffField.setItems(dataOptions);
        roomOnlyStaffField.setPromptText(dataOptions.get(0));
        roomCapacityField.setPromptText("0");
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
     * Creates a room.
     *
     * @throws IOException if an error occurs during loading
     */
    public void createRoom() throws IOException {
        String name = roomNameField.getText();
        name = name == null ? "" : name;
        Object building = roomBuildingIdField.getValue();
        String roomCap = roomCapacityField.getText();
        roomCap = roomCap == null || roomCap.contentEquals("") ? "0" : roomCap;
        String roomDesc = roomDescriptionField.getText();
        roomDesc = roomDesc == null ? "" : roomDesc;
        String buildingId = building == null ? Long.toString(buildingsByName.get(
                buildingsByName.keySet().toArray()[0])) : Long.toString(
                        this.buildingsByName.get(building.toString()));
        String onlyStaffObj = roomOnlyStaffField.getValue();
        String onlyStaff = onlyStaffObj == null ? Boolean.toString(false) : onlyStaffObj;
        onlyStaff = Boolean.toString((onlyStaff).contentEquals("Only staff members"));
        ServerCommunication.addRoom(buildingId, roomCap, roomDesc, onlyStaff, name);
        goToAdminRoomScene();
    }
}
