package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class BuildingSceneController {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    /**
     * Opens login window.
     *
     * @throws IOException when not found.
     */
    public void toLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));

        Stage login = new Stage();
        login.setScene(new Scene(root));
        login.show();
    }

    public void refreshBuildings() {
        Alert alert = (new Alert(Alert.AlertType.INFORMATION));

        flowPane.getChildren().clear();

        try {
            String room = ServerCommunication.getBuildings();

            JsonArray buildingArray = (JsonArray) JsonParser.parseString(room);

            for (JsonElement building : buildingArray) {
                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));

                Label name = (Label) newBuilding.lookup("#bname");
                name.setText(((JsonObject) building).get("name").getAsString());

                String bDetails = ((JsonObject) building).get("location").getAsString()
                    + "\n" + ((JsonObject) building).get("description").getAsString()
                    + "\n" + "Max. Capacity"
                    + "\n" + "Opening times: " + ((JsonObject) building).get("open").getAsString()
                    + " - " + ((JsonObject) building).get("closed").getAsString();

                Label details = (Label) newBuilding.lookup("#bdetails");
                details.setText(bDetails);

                flowPane.getChildren().add(newBuilding);
            }
            return;
        } catch (InterruptedException e) {
            alert.setContentText("Error: Interrupted");
        } catch (IOException e) {
            alert.setContentText("Error: Wrong IO");
        }
        alert.showAndWait();
    }

    public void alertAllBuildings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Users shown.");
        alert.setHeaderText(null);
        try {
            alert.setContentText(ServerCommunication.getBuildings());
        } catch (Exception e) {
            alert.setContentText("Error Occurred.");
        }
        alert.showAndWait();
    }
}
