package nl.tudelft.oopp.group39.controllers.Admin.Building;

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
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.Building;

public class BuildingCreateController extends BuildingListController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private TextField timeOpenFieldNew;
    @FXML
    private TextField timeClosedFieldNew;
    @FXML
    private MenuBar navBar;


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }
    /**
     * Adds a new building with auto-generated ID.
     */
    public void addBuilding() throws IOException {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        String open = getTime(timeOpenFieldNew.getText(), true);
        String closed = getTime(timeClosedFieldNew.getText(), false);
        ServerCommunication.addBuilding(name, location, desc, open, closed);
        createAlert("Added a new building.");
        getBack();
        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
        timeOpenFieldNew.clear();
        timeClosedFieldNew.clear();
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
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Building/BuildingList.fxml", currentstage);
    }

}
