package nl.tudelft.oopp.group39.building.controller;

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
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.MainSceneController;

public class BuildingSceneController extends MainSceneController implements Initializable {

    @FXML
    private FlowPane flowPane;

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

            FXMLLoader loader;

            for (Building building : list) {

                loader = new FXMLLoader(getClass()
                    .getResource("/building/buildingCell.fxml"));

                GridPane newBuilding = loader.load();
                BuildingCellController controller = loader.getController();

                controller.createPane(building);

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
