package nl.tudelft.oopp.group39.controllers.Admin.Room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.Room.RoomListController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;

public class RoomEditController extends RoomListController implements Initializable {

    private Room room;
    private Building building;
    private HashMap<String, Integer> buildingsByName = new HashMap();
    private HashMap<Integer, String> buildingsById = new HashMap();
    @FXML
    private Button backbtn;
    @FXML
    private TextField roomNameField;
    @FXML
    private TextField roomDescriptionField;
    @FXML
    private ComboBox roomBuildingIdField;
    @FXML
    private ComboBox roomOnlyStaffField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private MenuBar navBar;


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }


    public void initData(Room room) throws JsonProcessingException {
        this.room = room;
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> data = getData(b);
        System.out.println(room.getBuilding() + " " + this.buildingsById + " " + this.buildingsById.keySet());
        String cName = this.buildingsById.get((int) room.getBuilding());
        List<String> options = new ArrayList<>();
        options.add("All users"); options.add("Only staff members");
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        roomNameField.setPromptText(room.getName());
        roomDescriptionField.setPromptText(room.getDescription());
        roomBuildingIdField.setItems(data);
        roomBuildingIdField.setPromptText(cName);
        roomOnlyStaffField.setItems(dataOptions);
        roomOnlyStaffField.setPromptText(getOnlyStaff(room));
        roomCapacityField.setPromptText(Integer.toString(room.getCapacity()));
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Room/RoomList.fxml", currentstage);
    }

    public void editRoom() throws IOException {
        String name = roomNameField.getText();
        name = name.contentEquals("") ? room.getName() : name;
        Object building = roomBuildingIdField.getValue();
        String buildingId = building == null ? Integer.toString((int) this.room.getBuilding()) : Integer.toString(this.buildingsByName.get(building.toString()));
        String roomCap = roomCapacityField.getText();
        String roomDesc = roomDescriptionField.getText();
        String roomID = Integer.toString((int) room.getId());
        Object onlyStaffObj = roomOnlyStaffField.getValue();
        String onlyStaff = onlyStaffObj == null ? getOnlyStaff(room) : (String) onlyStaffObj;
        onlyStaff = Boolean.toString((onlyStaff).contentEquals("Only staff members"));
        System.out.println(building + " " + buildingId + " " + this.room.getBuilding());
        ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID, onlyStaff, name);
        getBack();
        createAlert("Updated: " + room.getName());
    }

    public List<Building> getBuildings(String buildings) throws JsonProcessingException {
        System.out.println(buildings);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        return Arrays.asList(list);
    }

    public List<String> getBuildingNames(List<Building> buildings) {
        List<String> a = new ArrayList<>();
        for(Building building : buildings) {
            this.buildingsByName.put(building.getName(), building.getId());
            this.buildingsById.put(building.getId(), building.getName());
            a.add(building.getName());
        }
        return a;
    }

}
