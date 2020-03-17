package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.entities.Building;

public class BuildingSceneController extends MainSceneController {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    /**
     * Doc. TODO Sven
     */
    public void refreshBuildings() {
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();

            ObjectMapper mapper = new ObjectMapper();

            ArrayNode body = (ArrayNode) mapper.readTree(buildingString).get("body");

            for (JsonNode buildingJson : body) {

                String buildings = mapper.writeValueAsString(buildingJson);

                Building building = mapper.readValue(buildings, Building.class);

                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));

                Label name = (Label) newBuilding.lookup("#bname");
                name.setText(building.getName());

                String newDetails = (building.getLocation()
                    + "\n" + building.getDescription()
                    + "\n" + "Max. Capacity"
                    + "\n" + "Opening times: " + building.getOpen()
                    + " - " + building.getClosed());

                Label details = (Label) newBuilding.lookup("#bdetails");
                details.setText(newDetails);

                flowPane.getChildren().add(newBuilding);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Doc. TODO Sven
     */
    public void alertAllBuildings() {
        try {
            createAlert("Users shown.", ServerCommunication.getBuildings());
        } catch (Exception e) {
            createAlert("Error Occurred.");
        }
    }
}
