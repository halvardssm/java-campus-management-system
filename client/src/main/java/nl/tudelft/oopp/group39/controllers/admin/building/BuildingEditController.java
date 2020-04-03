package nl.tudelft.oopp.group39.controllers.admin.building;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;


public class BuildingEditController extends BuildingListController {

    private Stage currentStage;
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
    private ComboBox<String> timeOpenFieldNew;
    @FXML
    private ComboBox<String> timeClosedFieldNew;
    @FXML
    private MenuBar navBar;

    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Initializes data into their respective boxes to be used for editing.
     */

    public void initData(Building building) {
        customInit();
        this.building = building;
        nameFieldNew.setPromptText(building.getName());
        locationFieldNew.setPromptText(building.getLocation());
        descriptionFieldNew.setPromptText(building.getDescription());
        List<String> timeSlots = initiateTimeslots();
        this.start = building.getOpen();
        this.end = building.getClosed();
        ObservableList<String> list = FXCollections.observableList(timeSlots);
        timeOpenFieldNew.setPromptText(start);
        timeClosedFieldNew.setPromptText(end);
        timeOpenFieldNew.setItems(list);
        timeClosedFieldNew.setItems(list);
    }

    private List<String> initiateTimeslots()  {
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
     * Goes back to main building panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchBuildingView(currentStage);
    }
    /**
     * Retrieves data from boxes and sends to database.
     */

    public void updateBuilding() throws IOException {
        String name = nameFieldNew.getText();
        name = name.contentEquals("") ? building.getName() : name;
        System.out.println(name);
        String location = locationFieldNew.getText();
        location = location.contentEquals("") ? building.getLocation() : location;
        String desc = descriptionFieldNew.getText();
        desc = desc.contentEquals("") ? building.getDescription() : desc;
        Object reservationStartValue = timeOpenFieldNew.getValue();
        String reservationStartString = reservationStartValue == null ? start
                : reservationStartValue.toString() + ":00";
        Object reservationEndValue = timeClosedFieldNew.getValue();
        String reservationEndString = reservationEndValue == null ? end
              : reservationEndValue.toString() + ":00";
        String id = Integer.toString(building.getId());
        ServerCommunication.updateBuilding(
                name, location, desc, reservationStartString, reservationEndString, id);
        getBack();

        nameFieldNew.clear();
        locationFieldNew.clear();
        descriptionFieldNew.clear();
    }

}
