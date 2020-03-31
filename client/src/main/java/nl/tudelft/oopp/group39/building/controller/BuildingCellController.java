package nl.tudelft.oopp.group39.building.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class BuildingCellController extends AbstractSceneController {

    private Building building;

    @FXML
    private Label name;

    @FXML
    private Label details;

    /**
     * Creates a Pane.
     *
     * @param building building to create pane from
     */
    public void createPane(Building building) {
        this.building = building;
        String buildingName = building.getName();
        String address = building.getLocation();
        String desc = building.getDescription();

        name.setText(buildingName);

        String newDetails = (address
            + "\n" + desc
            + "\n" + "Max. Capacity: " + building.getMaxCapacity()
            + "\n" + "Opening times: " + building.getOpen()
            + " - " + building.getClosed());

        details.setText(newDetails);
    }

    public void buildingSelected() throws IOException {
        goToRoomsScene(building);
    }
}
