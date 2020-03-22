package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;

public class BuildingSceneController extends MainSceneController implements Initializable {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    /**
     * Retrieves buildings from the server and shows them.
     */
    public void showBuildings() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.get(ServerCommunication.building);
            System.out.println(buildingString);
            ArrayNode body = (ArrayNode) mapper.readTree(buildingString).get("body");
            buildingString = mapper.writeValueAsString(body);
            Building[] list = mapper.readValue(buildingString, Building[].class);
            for (Building building : list) {
                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));
                long buildingId = building.getId();
                String buildingName = building.getName();
                String address = building.getLocation();
                String desc = building.getDescription();
                newBuilding.setOnMouseClicked(e -> {
                    try {
                        goToRoomsScene(buildingId, buildingName, address);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                });

                Label name = (Label) newBuilding.lookup("#bname");
                name.setText(buildingName);

                String newDetails = (address
                    + "\n" + desc
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
            createAlert("Users shown.", ServerCommunication.get(ServerCommunication.building));
        } catch (Exception e) {
            createAlert("Error Occurred.");
        }
    }

    /**
     * Retrieves the buildings when page is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showBuildings();
    }


}
