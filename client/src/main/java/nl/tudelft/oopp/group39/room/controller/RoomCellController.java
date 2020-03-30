package nl.tudelft.oopp.group39.room.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.controller.MainSceneController;

public class RoomCellController extends MainSceneController {

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
}
