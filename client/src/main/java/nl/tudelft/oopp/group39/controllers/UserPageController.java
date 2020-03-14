package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;

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

public class UserPageController extends MainSceneController {
    @FXML
    private FlowPane flowPane; //The User Page screen

    @FXML
    private AnchorPane newRoom; //The whole card

    public void refreshBuildings2() {
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings(); //ServerCommunication.getRooms();

            JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
            JsonArray buildingArray = body.getAsJsonArray("body");

//            JsonArray buildingArray = (JsonArray) JsonParser.parseString(room);

            for (JsonElement building : buildingArray) {
                newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));

                Label name = (Label) newRoom.lookup("#rName");
                name.setText(((JsonObject) building).get("name").getAsString());

//                String bDetails = ((JsonObject) building).get("location").getAsString()
//                        + "\n" + ((JsonObject) building).get("description").getAsString()
//                        + "\n" + "Max. Capacity"
//                        + "\n" + "Opening times: " + ((JsonObject) building).get("open").getAsString()
//                        + " - " + ((JsonObject) building).get("closed").getAsString();

//                Label details = (Label) newBuilding.lookup("#bdetails");
//                details.setText(bDetails);

                flowPane.getChildren().add(newRoom);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }
}
