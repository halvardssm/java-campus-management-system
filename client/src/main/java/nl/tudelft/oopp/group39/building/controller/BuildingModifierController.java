package nl.tudelft.oopp.group39.building.controller;

import java.time.LocalTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.MainSceneController;

public class BuildingModifierController extends MainSceneController {

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

    public void getBuildingsButton() {
        createAlert(ServerCommunication.get(ServerCommunication.building));
    }

    /**
     * Button. TODO Sven
     */
    public void newBuildingButton() {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        createAlert(ServerCommunication.addBuilding(name, location, desc, open, closed));
    }

    /**
     * Button. TODO Sven
     */
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

    /**
     * Doc. TODO Sven
     */
    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }

    /**
     * Doc. TODO Sven
     */
    public void removeBuildingButton(ActionEvent actionEvent) {

        String id = updateBuildingField.getText();

        id = id.contentEquals("") ? "1" : id;

        ServerCommunication.removeBuilding(id);

        getBuildingsButton();
    }
}
