package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

public class MainSceneController {

    public void createAlert(String content) {
        createAlert(null, content);
    }

    /**
     * Doc. TODO Sven
     */
    public void createAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToMainScene() throws IOException {
        UsersDisplay.sceneHandler("/mainScene.fxml");
    }

    public void goToBuildingScene() throws IOException {
        UsersDisplay.sceneHandler("/buildingListView.fxml");
    }

    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
    }

    public void goToAddBuilding() throws IOException {
        UsersDisplay.sceneHandler("/buildingModifyScene.fxml");
    }


    public void getFacilitiesButton() {
        createAlert(null, ServerCommunication.getFacilities());
    }

    public void getUsersButton() {
        createAlert(ServerCommunication.getUsers());
    }
}
