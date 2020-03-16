package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.JSONPObject;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.io.IOException;
import nl.tudelft.oopp.group39.entities.Building;

public class BuildingSceneController extends MainSceneController {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    public void refreshBuildings() {
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();

            ObjectMapper mapper = new ObjectMapper();

            JsonNode body = mapper.readTree(buildingString).get("body");

            JsonParser parser = body.traverse();

            TypeFactory typeFactory = mapper.getTypeFactory();

            Building[] buildingArray = mapper.readValue(body, Building[].class);

            for (Building building : buildingArray) {
                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));

                Label name = (Label) newBuilding.lookup("#bname");
                name.setText(building.getName());

                String bDetails = (building.getLocation()
                        + "\n" + building.getDescription()
                        + "\n" + "Max. Capacity"
                        + "\n" + "Opening times: " + building.getOpen().toString()
                        + " - " + building.getClosed().toString());

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
