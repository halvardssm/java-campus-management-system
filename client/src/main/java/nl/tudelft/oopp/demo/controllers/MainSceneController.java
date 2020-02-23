package nl.tudelft.oopp.demo.controllers;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.demo.communication.ServerCommunication;

public class MainSceneController {

    /**
     * Handles clicking the button.
     */
    public void getUsers() {
        buttonClicked("getUsers");
    }

    public void buttonClicked(String function) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Users shown.");
        alert.setHeaderText(null);
        switch(function) {
            //IMPORTANT FOR SUNDAY
            case "getUsers":
                alert.setContentText(ServerCommunication.getUsers());
                break;
            default:
                alert.setContentText("No such function");
        }
        alert.showAndWait();
    }
}
