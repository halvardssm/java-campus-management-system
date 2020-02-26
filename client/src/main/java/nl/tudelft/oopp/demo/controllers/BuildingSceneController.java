package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class BuildingSceneController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField nameField2;
    @FXML
    private TextField locationField2;
    @FXML
    private TextField descriptionField;
//    @FXML
//    private TextField timeOpenField;
//    @FXML
//    private TextField timeClosedField;

//    @FXML
//    private Button submitButton;
    /**
     * Handles clicking the button.
     */
    public void getFacilitiesButton() {
        buttonClicked("getFacilities");
    }
    public void getRoomFacilitiesButton() {
        buttonClicked("getRoomFacilities");
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

    public void buttonClicked(String function) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Building shown.");
        alert.setHeaderText(null);
        switch(function) {
            case "getBuildings":
                alert.setContentText(ServerCommunication.getBuildings());
                break;
            case "getRooms":
                alert.setContentText(ServerCommunication.getRooms());
                break;
            case "getFacilities":
                alert.setContentText(ServerCommunication.getFacilities());
                break;
            case "getRoomFacilities":
                alert.setContentText(ServerCommunication.getRoomFacilities());
                break;
            case "getFilteredBuilding":
                String name = nameField.getText();
                String location = locationField.getText();
//                LocalTime open = timeOpenField.getT;
//                LocalTime closed = timeClosedField;
//                alert.setContentText(ServerCommunication.getFilteredBuildings(name, location,LocalTime.now(), LocalTime.now()));
                alert.setContentText(ServerCommunication.getFilteredBuildings(name, location, null, null));
                break;
            case "newBuildingButton":
                String nName = nameField2.getText();
                String nLocation = locationField2.getText();
                String nDescr = descriptionField.getText();
                alert.setContentText(ServerCommunication.addBuilding(nName, nLocation, nDescr));
                break;
            default:
                alert.setContentText("No such function");
        }
        alert.showAndWait();
    }
}
