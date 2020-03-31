package nl.tudelft.oopp.group39.controllers.Admin.Building;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.models.Building;

public class BuildingEditController extends BuildingListController implements Initializable {

    private String start;
    private String end;
    private Building building;
    @FXML
    private Button backbtn;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private ComboBox timeOpenFieldNew;
    @FXML
    private ComboBox timeClosedFieldNew;
    @FXML
    private MenuBar navBar;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }

    public void initData(Building building) throws JsonProcessingException {
        this.building = building;
        nameFieldNew.setPromptText(building.getName());
        locationFieldNew.setPromptText(building.getLocation());
        descriptionFieldNew.setPromptText(building.getDescription());
        List<String> timeSlots = initiateTimeslots();
        ObservableList<String> list = FXCollections.observableList(timeSlots);
        this.start = building.getOpen();
        this.end = building.getClosed();
        timeOpenFieldNew.setPromptText(start);
        timeClosedFieldNew.setPromptText(end);
        timeOpenFieldNew.setItems(list);
        timeClosedFieldNew.setItems(list);
    }

    private List<String> initiateTimeslots() throws JsonProcessingException {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String time;
            if (i < 10) {
                time = "0" + i + ":00";
            } else {
                time = i + ":00";
            }
            times.add(time);
        }
        return times;
    }
    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Building/BuildingList.fxml", currentstage);
    }

    public void updateBuilding() throws IOException {
        String name = nameFieldNew.getText();
        name = name.contentEquals("") ? building.getName() : name;
        System.out.println(name);
        String location = locationFieldNew.getText();
        location = location.contentEquals("") ? building.getLocation() : location;
        String desc = descriptionFieldNew.getText();
        desc = desc.contentEquals("") ? building.getDescription() : desc;
        Object reservationStartValue = timeOpenFieldNew.getValue();
        String reservationStartString = reservationStartValue == null ? start : reservationStartValue.toString() + ":00";
        Object reservationEndValue = timeClosedFieldNew.getValue();
        String reservationEndString = reservationEndValue == null ? end : reservationEndValue.toString() + ":00";
        String id = Integer.toString(building.getId());
        ServerCommunication.updateBuilding(name, location, desc, reservationStartString, reservationEndString, id);
        getBack();
        createAlert("Updated: " + building.getName());

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
    }

}
