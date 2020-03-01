package nl.tudelft.oopp.group39.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

import java.io.IOException;
import java.time.LocalTime;

public class MainSceneController {
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
    @FXML
    private TextField updateRoomField;
    @FXML
    private TextField roomBuildingIdField;
    @FXML
    private TextField roomCapacityField;
    @FXML
    private TextField roomDescriptionField;
    /**
     * Handles clicking the button.
     */

    public void goToBuildingScene() throws IOException {
        UsersDisplay.sceneHandler("/buildingScene.fxml");
    }

    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
    }

    public void goToMainScene() throws IOException {
        UsersDisplay.sceneHandler("/mainScene.fxml");
    }

    public void getFacilitiesButton() {
        buttonClicked("getFacilities");
    }

    public void getRoomsButton() {
        buttonClicked("getRooms");
    }

    public void getBuildingsButton() {
        buttonClicked("getBuildings");
    }

    public void getFilteredBuildings() {
        buttonClicked("getFilteredBuilding");
    }

    public void newBuildingButton() {
        buttonClicked("newBuildingButton");
    }

    public void getUsersButton() {
        buttonClicked("getUsers");
    }

    public void updateBuildingButton() {
        buttonClicked("updateBuilding");
    }

    public void newRoomButton() {
        buttonClicked("newRoom");
    }

    public void updateRoomButton() {
        buttonClicked("updateRoom");
    }

    public void buttonClicked(String function) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Building shown.");
        alert.setHeaderText(null);
        switch (function) {
            case "getBuildings":
                alert.setContentText(ServerCommunication.getBuildings());
                break;
            case "getRooms":
                alert.setContentText(ServerCommunication.getRooms());
                break;
            case "getFacilities":
                alert.setContentText(ServerCommunication.getFacilities());
                break;
            case "getFilteredBuilding":
                String name = nameField.getText();
                String location = locationField.getText();
                String capacity = capacityField.getText().contentEquals("") ? "0" : capacityField.getText();
                String open = getTime(timeOpenField.getText(), true);
                String closed = getTime(timeClosedField.getText(), false);
                alert.setContentText(ServerCommunication.getFilteredBuildings(name, location, open, closed, capacity));
                break;
            case "newBuildingButton":
                String nName = nameFieldNew.getText();
                String nLocation = locationFieldNew.getText();
                String nDescr = descriptionFieldNew.getText();
                String nOpen = getTime(timeOpenFieldNew.getText(), true);
                String nClosed = getTime(timeClosedFieldNew.getText(), false);
                alert.setContentText(ServerCommunication.addBuilding(nName, nLocation, nDescr, nOpen, nClosed));
                break;
            case "updateBuilding":
                String uName = nameFieldNew.getText();
                String uLocation = locationFieldNew.getText();
                String uDescr = descriptionFieldNew.getText();
                String uOpen = getTime(timeOpenFieldNew.getText(), true);
                String uClosed = getTime(timeClosedFieldNew.getText(), false);
                String uId = updateBuildingField.getText().contentEquals("") ? "1" : updateBuildingField.getText();
                alert.setContentText(ServerCommunication.updateBuilding(uName, uLocation, uDescr, uOpen, uClosed, uId));
                break;
            case "newRoom":
                String buildingId = roomBuildingIdField.getText();
                String roomCapacity = roomCapacityField.getText();
                String roomDescription = roomDescriptionField.getText();
                alert.setContentText(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
                break;
            case "updateRoom":
                String uBuildingId = roomBuildingIdField.getText();
                String uRoomCapacity = roomCapacityField.getText();
                String uRoomDescription = roomDescriptionField.getText();
                String uIdRoom = updateRoomField.getText().contentEquals("") ? "1" : updateRoomField.getText();
                alert.setContentText(ServerCommunication.updateRoom(uBuildingId, uRoomCapacity, uRoomDescription, uIdRoom));
                break;
            case "getUsers":
                alert.setContentText(ServerCommunication.getUsers());
                break;
            default:
                alert.setContentText("No such function");
        }
        alert.showAndWait();
    }

    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }
}
