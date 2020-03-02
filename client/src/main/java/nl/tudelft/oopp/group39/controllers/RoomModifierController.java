package nl.tudelft.oopp.group39.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

import java.io.IOException;
import java.time.LocalTime;

public class RoomModifierController extends MainSceneController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField timeOpenField;
    @FXML
    private TextField timeClosedField;
    @FXML
    private TextField timeOpenFieldNew;
    @FXML
    private TextField timeClosedFieldNew;
    @FXML
    private TextField updateBuildingField;

    public void goToBuildingScene() throws IOException {
        UsersDisplay.sceneHandler("/buildingModifyScene.fxml");
    }

    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
    }

    public void getBuildingsButton() {
        createAlert(ServerCommunication.getBuildings());
    }

    public void getFilteredBuildings() {
        String name = nameField.getText();
        String location = locationField.getText();
        String capacity = capacityField.getText();

        capacity = capacity.contentEquals("") ? "0" : capacity;

        String open = getTime(timeOpenField.getText(), true);
        String closed = getTime(timeClosedField.getText(), false);
        createAlert(ServerCommunication.getFilteredBuildings(name, location, open, closed, capacity));
    }

    public void newBuildingButton() {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        createAlert(ServerCommunication.addBuilding(name, location, desc, open, closed));
    }

    public void updateBuildingButton() {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        String id = updateBuildingField.getText();
        id = id.contentEquals("") ? "1" : id;

        createAlert(ServerCommunication.updateBuilding(name, location, desc, open, closed, id));
    }

    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }
}
