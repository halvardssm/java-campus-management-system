package nl.tudelft.oopp.group39.controllers.Admin.Event;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Event;

public class EventEditController extends EventListController implements Initializable {

    private Event cEvent;
    @FXML
    private Button backbtn;

    @FXML
    private TextField typeField;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private MenuBar NavBar;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(NavBar);
    }

    public void initData(Event cEvent) {
        this.cEvent = cEvent;
        typeField.setPromptText(cEvent.getType());
        startField.setPromptText(cEvent.getStartDate());
        endField.setPromptText(cEvent.getEndDate());
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
        mainSwitch("/Admin/Event/EventList.fxml", currentstage);
    }

    public void editEvent() throws IOException {
//        String id = idField.getText();
//        id = id.contentEquals("") ? building.getName() : name;
//        String location = locationFieldNew.getText();
//        location = location.contentEquals("") ? building.getLocation() : location;
//        String desc = descriptionFieldNew.getText();
//        desc = desc.contentEquals("") ? building.getDescription() : desc;
//        String open = timeOpenFieldNew.getText();
//        open = open.contentEquals("") ? building.getOpen() : getTime(open, true);
//        String closed =timeClosedFieldNew.getText();
//        closed = closed.contentEquals("") ? building.getClosed() : getTime(closed, false);
//        String id = Integer.toString(building.getId());
//        ServerCommunication.updateBuilding(name, location, desc, open, closed, id);
//        getBack();
//        createAlert("Updated: " + building.getName());
//
//        nameFieldNew.clear();
//        locationFieldNew.clear();
//        descriptionFieldNew.clear();
//        timeOpenFieldNew.clear();
//        timeClosedFieldNew.clear();
    }

}
