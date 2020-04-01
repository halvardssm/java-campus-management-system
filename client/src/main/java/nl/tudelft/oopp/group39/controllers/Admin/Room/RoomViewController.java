package nl.tudelft.oopp.group39.controllers.Admin.Room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.Room.RoomListController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Facility;
import nl.tudelft.oopp.group39.models.Room;

public class RoomViewController extends RoomListController implements Initializable {

    private Room room;
    private Building building;
    private HashMap<String, Integer> buildingsByName = new HashMap();
    private HashMap<Integer, String> buildingsById = new HashMap();
    @FXML
    private Button backbtn;
    @FXML
    private TextField roomIdFieldView;
    @FXML
    private TextField nameFieldView;
    @FXML
    private TextField descriptionFieldView;
    @FXML
    private TextField buildingFieldView;
    @FXML
    private TextField onlyStaffFieldView;
    @FXML
    private TextField capacityFieldView;
    @FXML
    private TextArea facilitiesBox;
    @FXML
    private MenuBar navBar;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }

    /**
     * Initialize rooms data into their respective boxes to be used for updating.
     */

    public void initData(Room room) throws JsonProcessingException {
        this.room = room;
        String nBuilding = ServerCommunication.getBuilding(room.getBuilding());
        System.out.println(nBuilding);
        ObjectNode body = (ObjectNode) mapper.readTree(nBuilding).get("body");
        nBuilding = mapper.writeValueAsString(body);
        Building building = mapper.readValue(nBuilding, Building.class);
        nameFieldView.setText(room.getName());
        nameFieldView.setDisable(true);
        descriptionFieldView.setText(room.getDescription());
        descriptionFieldView.setDisable(true);
        buildingFieldView.setText(building.getName());
        buildingFieldView.setDisable(true);
        onlyStaffFieldView.setText(getOnlyStaff(room));
        onlyStaffFieldView.setDisable(true);
        capacityFieldView.setText(Integer.toString(room.getCapacity()));
        capacityFieldView.setDisable(true);
        facilitiesBox.setText(getFacilitiesString(room));
        facilitiesBox.setDisable(true);
    }
    /**
     * Returns the rooms facilities as a string.
     */

    public String getFacilitiesString(Room room) throws JsonProcessingException {
        ArrayNode body = room.getFacilities();
        String facilities = mapper.writeValueAsString(body);
        Facility[] facilitiesList = mapper.readValue(facilities, Facility[].class);
        StringBuilder facilitiesString = new StringBuilder();
        for (Facility facility : facilitiesList) {
            facilitiesString.append(facility.getDescription()).append("\n");
        }
        return facilitiesString.toString();
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Room/RoomList.fxml", currentstage);
    }

}
