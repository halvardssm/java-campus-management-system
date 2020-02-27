package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class RoomSceneController {

    //    @FXML
    //    private BorderPane mainpane;

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newRoom;

    /**
     * Opens login window.
     *
     * @throws IOException when not found.
     */
    public void toLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));

        Stage login = new Stage();
        login.setScene(new Scene(root));
        login.show();
    }

    public void addBuilding() throws IOException {

//        String room = ServerCommunication.getRooms();

        newRoom = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));

        Label bname = (Label) newRoom.lookup("#bname");

        bname.setText("Placeholder");

        flowPane.getChildren().add(newRoom);
    }

    public void alertAllBuildings() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("Users shown.");
        alert.setHeaderText(null);
        try {
            alert.setContentText(ServerCommunication.getBuildings());
        } catch (Exception e) {
            alert.setContentText("Error Occurred.");
        }
        alert.showAndWait();
    }

    public void removeBuilding() {
        flowPane.getChildren().clear();
    }

}
