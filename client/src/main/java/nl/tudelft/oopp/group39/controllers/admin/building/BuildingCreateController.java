package nl.tudelft.oopp.group39.controllers.admin.building;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class BuildingCreateController extends BuildingListController {

    private Stage currentStage;
    private String start;
    private String end;
    @FXML
    private Button backbtn;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private ComboBox<String> timeOpenFieldNew;
    @FXML
    private ComboBox<String> timeClosedFieldNew;
    @FXML
    private MenuBar navBar;


    /**
     * Initialize data into tableView.
     */
    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
        List<String> timeSlots = null;
        try {
            timeSlots = initiateTimeslots();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ObservableList<String> list = FXCollections.observableList(timeSlots);
        this.start = list.get(0);
        this.end = list.get(1);
        timeOpenFieldNew.setPromptText(start);
        timeClosedFieldNew.setPromptText(end);
        timeOpenFieldNew.setItems(list);
        timeClosedFieldNew.setItems(list);
    }
    /**
     * Adds a new building with auto-generated ID.
     */

    public void addBuilding() throws IOException {
        String name = nameFieldNew.getText();
        String location = locationFieldNew.getText();
        String desc = descriptionFieldNew.getText();
        Object reservationStartValue = timeOpenFieldNew.getValue();
        String reservationStartString = reservationStartValue == null ? start + ":00" : reservationStartValue.toString() + ":00";
        Object reservationEndValue = timeClosedFieldNew.getValue();
        String reservationEndString = reservationEndValue == null ? end + ":00" : reservationEndValue.toString() + ":00";

        ServerCommunication.addBuilding(name, location, desc, reservationStartString, reservationEndString);
        getBack();
        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();

    }

    /**
     * Returns string containing times inputted.
     */
    public String getTime(String time, boolean open) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
        if (open) {
            return time.contentEquals("") ? formatter.format(LocalTime.MAX) : time;
        }
        return time.contentEquals("") ? formatter.format(LocalTime.MIN) : time;
    }

    /**
     * Goes back to main Building panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchBuildingView(currentStage);
    }

}
