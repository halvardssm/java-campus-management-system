package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;

public class AdminUpdateRoomController extends MainSceneController implements Initializable {

    private Room room;
    @FXML
    private Button backbtn;
    @FXML
    private TextField roomNameField;
    @FXML
    private TextField roomDescriptionField;
    @FXML
    private ComboBox roomBuildingIdField;
    @FXML
    private ComboBox roomOnlyStaffField;
    @FXML
    private TextField roomCapacityField;


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(Room room) {
        this.room = room;
        roomNameField.setPromptText(room.getName());
        roomDescriptionField.setPromptText(room.getDescription());
        roomBuildingIdField.setPromptText(room.getBuilding());
        roomOnlyStaffField.setPromptText(room.getName());
        roomCapacityField.setPromptText(room.getDescription());
    }

    /**
     * Doc. TODO
     */
    public String getTime(String time, boolean open) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
        if (open) {
            return time.contentEquals("") ? formatter.format(LocalTime.MAX) : time;
        }
        return time.contentEquals("") ? formatter.format(LocalTime.MIN) : time;
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void switchBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/AdminBuildingView.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    public void updateBuilding() throws IOException {

        String name = roomNameField.getText();
        name = name.contentEquals("") ? room.getName() : name;
        String buildingId = roomBuildingIdField.getText();
        String roomCap = roomCapacityField.getText();
        String roomDesc = roomDescriptionField.getText();
        String roomID = Integer.toString((int) room.getId());
        ServerCommunication.updateRoom(buildingId, roomCap, roomDesc, roomID)
        switchBack();
        createAlert("Updated: " + room.getName());

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
        updateBuildingField.clear();
    }

}
