package nl.tudelft.oopp.group39.controllers.Admin.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Event;
import org.w3c.dom.Text;

public class EventEditController extends EventListController implements Initializable {

    private Event cEvent;
    @FXML
    private Button backbtn;

    @FXML
    private ComboBox typeBox;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private MenuBar NavBar;
    @FXML
    private TextArea dateMessage;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(NavBar);
    }

    public void initData(Event cEvent) throws JsonProcessingException {
        this.cEvent = cEvent;
        typeBox.setPromptText(cEvent.getType());
        String types = ServerCommunication.getEventTypes();

        System.out.println(types);

        ArrayNode body = (ArrayNode) mapper.readTree(types).get("body");
        types = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(types, String[].class);
        ObservableList<String> data = FXCollections.observableArrayList(list);

        typeBox.setItems(data);
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
        Object typeObj = typeBox.getValue();
        String type = typeObj == null ? cEvent.getType() : typeObj.toString();
        LocalDate start = startField.getValue();
        boolean startNull = start == null;
        String startDate = startNull ? cEvent.getStartDate() : start.toString();
        LocalDate end = endField.getValue();
        boolean endNull = end == null;
        String endDate = endNull ? cEvent.getEndDate() : end.toString();
        String id = Integer.toString(cEvent.getId());
        checkValidity(id, startDate, endDate, startNull, endNull, type);
    }

    public void createEventFinal(String id, String type, String startDate, String endDate) throws IOException {
        ServerCommunication.updateEvent(id, type, startDate, endDate);
        getBack();
        createAlert("Updated: " + cEvent.getType());
    }

    public void checkValidity(String id, String startDate, String endDate, boolean startNull, boolean endNull, String type) throws IOException {
        if(!endNull || !startNull) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            if (!end.isAfter(start)) {
                dateMessage.setStyle("-fx-text-fill: Red");
                dateMessage.setText("The end date needs to be later than the start date!\n(Start date was: " + start.toString() + ", end date was: " + end.toString() + " )");
                return;
            }
            if (!start.isAfter(LocalDate.now())) {
                dateMessage.setStyle("-fx-text-fill: Red");
                dateMessage.setText("The start date needs to be later than today!\n(Start date was: " + start.toString() + ", end date was: " + end.toString() + " )");
                return;
            }
            createEventFinal(id, type, start.toString(), end.toString());
        }
    }

}
