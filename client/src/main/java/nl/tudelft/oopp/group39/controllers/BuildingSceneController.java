package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;

public class BuildingSceneController extends MainSceneController {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    public void refreshBuildings() {
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();

            JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
            JsonArray buildingArray = body.getAsJsonArray("body");

//            JsonArray buildingArray = (JsonArray) JsonParser.parseString(room);

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
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    public void alertAllBuildings() {
        try {
            createAlert("Users shown.",ServerCommunication.getBuildings());
        } catch (Exception e) {
            createAlert("Error Occurred.");
        }
    }
}
