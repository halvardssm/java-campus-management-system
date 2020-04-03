package nl.tudelft.oopp.group39.controllers.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.event.model.Event;



public class EventCreateController extends EventListController implements Initializable {

    private ObjectMapper mapper = new ObjectMapper();
    private String eventType;
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
    private TextArea dateMessage;
    @FXML
    private MenuBar navBar;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }
    /**
     * .
     */

    public void initData() throws JsonProcessingException {
        String types = ServerCommunication.getEventTypes();
        ArrayNode body = (ArrayNode) mapper.readTree(types).get("body");
        types = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(types, String[].class);
        ObservableList<String> data = FXCollections.observableArrayList(list);
        this.eventType = data.get(0);
        dateMessage.setText("");

        typeBox.setItems(data);
        typeBox.setPromptText(eventType);
        startField.setPromptText(LocalDate.now().toString());
        endField.setPromptText(LocalDate.now().toString());
    }
    /**
     * Gains the information needed to create event.
     */

    public void createEvent() throws IOException {
        dateMessage.setText("");
        Object typeObj = typeBox.getValue();
        String type = typeObj == null ? this.eventType : typeObj.toString();
        LocalDate start = startField.getValue();
        boolean startNull = start == null;
        String startDate = startNull ? LocalDate.now().toString() : start.toString();
        LocalDate end = endField.getValue();
        boolean endNull = end == null;
        String endDate = endNull ? LocalDate.now().toString() : end.toString();
        checkValidity(startDate, endDate, startNull, endNull, type);
    }
    /**
     * Communicates information to create event to server.
     */

    public void createEventFinal(String type, String startDate, String endDate) throws IOException {
        ServerCommunication.addEvent(type, startDate, endDate);
        getBack();
//        createAlert("Created an event of type: " + type);
    }
    /**
     * Makes sure that values put into event are valid.
     */

    public void checkValidity(String startDate, String endDate, boolean startNull, boolean endNull, String type) throws IOException {
        if (!endNull || !startNull) {
            if (!endNull && !startNull) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                if (!end.isAfter(start)) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The end date needs to be later than the start date!");
                    return;
                }
                if (!start.isAfter(LocalDate.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!\n(Imputted start date was: " + start.toString() + ", Imputted end date was: " + end.toString() + " )");
                    return;
                }
                createEventFinal(type, start.toString(), end.toString());
            }
            if (!endNull) {
                LocalDate end = LocalDate.parse(endDate);
                LocalDate start = end.minusDays(1);
                if (!start.isAfter(LocalDate.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!\n(Automatically generated start date was: " + start.toString() + " )");
                    return;
                }
                createEventFinal(type, start.toString(), end.toString());
            }
            if (!startNull) {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = start.plusDays(1);
                if (!start.isAfter(LocalDate.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!\n(Imputted start date was: " + start.toString() + " )");
                    return;
                }
                createEventFinal(type, start.toString(), end.toString());
            }
        }
        dateMessage.setStyle("-fx-text-fill: Red");
        dateMessage.setText("Please input a start date or an end date.");
    }
    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/admin/event/EventList.fxml", currentstage);
    }

}
