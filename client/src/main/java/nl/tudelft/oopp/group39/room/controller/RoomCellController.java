package nl.tudelft.oopp.group39.room.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class RoomCellController extends AbstractSceneController {

    @FXML
    private Label name;

    @FXML
    private Label details;

    private Room room;

    private Building building;

    public void createPane(Room room, Building building) {

        this.room = room;
        this.building = building;

        name.setText(room.getName());
        String roomDetails = room.getDescription()
            + "\n" + "Capacity: " + room.getCapacity()
            + "\n" + "Facilities: " + room.facilitiesToString();

        details.setText(roomDetails);
    }

    public void goToReservationScene() throws IOException {
        super.goToReservationScene(room,building);
    }
    public void toggleFilter() {

    }
}
