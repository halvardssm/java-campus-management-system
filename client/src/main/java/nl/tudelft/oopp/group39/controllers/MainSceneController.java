package nl.tudelft.oopp.group39.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

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

    /**
     * Handles clicking the button.
     */
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
