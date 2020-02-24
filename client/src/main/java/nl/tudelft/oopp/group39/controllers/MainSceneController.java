package nl.tudelft.oopp.group39.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class MainSceneController {

    public Button button;

    /**
     * Handles clicking the button.
     */
    public void getUsers() {
        buttonClicked("getUsers");
    }

    /**
     * Method to handle button click.
     *
     * @param function The function which is clicked
     */
    public void buttonClicked(String function) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Users shown.");
        alert.setHeaderText(null);
        switch (function) {
            //IMPORTANT FOR SUNDAY
            case "getUsers":
                alert.setContentText(ServerCommunication.getUsers());
                break;
            case "readUser":
            default:
                alert.setContentText("No such function");
        }
        alert.showAndWait();
    }
}
