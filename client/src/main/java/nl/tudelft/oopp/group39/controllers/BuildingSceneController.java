package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class BuildingSceneController extends MainSceneController implements Initializable{

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    public void refreshBuildings() {
        System.out.println(flowPane);
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();
            System.out.println(buildingString);

            JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
            JsonArray buildingArray = body.getAsJsonArray("body");

            for (JsonElement building : buildingArray) {
                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));
                long buildingId = ((JsonObject) building).get("id").getAsLong();
                String buildingName = ((JsonObject) building).get("name").getAsString();
                String address = ((JsonObject) building).get("location").getAsString();
                newBuilding.setOnMouseClicked(e -> {
                    try {
                        goToRoomsScene();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                });

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshBuildings();
    }


}
