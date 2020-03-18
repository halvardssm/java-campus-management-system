package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import nl.tudelft.oopp.group39.models.Building;

public class BuildingSceneController extends MainSceneController implements Initializable{

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    /**
     * Doc. TODO Sven
     */
    public void refreshBuildings() {
        System.out.println(flowPane);
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();
            System.out.println(buildingString);

            ArrayNode body = (ArrayNode) mapper.readTree(buildingString).get("body");

            for (JsonNode buildingJson : body) {

                String buildings = mapper.writeValueAsString(buildingJson);

                Building building = mapper.readValue(buildings, Building.class);

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshBuildings();
    }


}
