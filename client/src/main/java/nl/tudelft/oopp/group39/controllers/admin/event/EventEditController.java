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
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;


public class EventEditController extends EventListController {

    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Event abcEvent;
    @FXML
    private Button backbtn;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private MenuBar navBar;
    @FXML
    private TextArea dateMessage;

    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Initializes data for usage in editing event.
     */

    public void initData(Event abcEvent) throws JsonProcessingException {
        customInit();
        this.abcEvent = abcEvent;
        typeBox.setPromptText(abcEvent.getType());
        String types = ServerCommunication.getEventTypes();

        ArrayNode body = (ArrayNode) mapper.readTree(types).get("body");
        types = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(types, String[].class);
        ObservableList<String> data = FXCollections.observableArrayList(list);

        typeBox.setItems(data);
        startField.setPromptText(abcEvent.getStartDate().toString());
        endField.setPromptText(abcEvent.getEndDate().toString());
    }

    /**
     * Goes back to main Event panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchEventView(currentStage);
    }

    /**
     * Edits the values of an event.
     */

    public void editEvent() throws IOException {
        Object typeObj = typeBox.getValue();
        String type = typeObj == null ? abcEvent.getType() : typeObj.toString();
        LocalDate start = startField.getValue();
        boolean startNull = start == null;
        String startDate = startNull ? abcEvent.getStartDate().toString() : start.toString();
        LocalDate end = endField.getValue();
        boolean endNull = end == null;
        String endDate = endNull ? abcEvent.getEndDate().toString() : end.toString();
        String id = Long.toString(abcEvent.getId());
        checkValidity(id, startDate, endDate, startNull, endNull, type);
    }
    /**
     * Communicates edit of event to server.
     */

    public void createEventFinal(String id, String type, String startDate, String endDate) throws IOException {
        ServerCommunication.updateEvent(id, type, startDate, endDate);
        getBack();
        createAlert("Updated: " + abcEvent.getType());
    }
    /**
     * Makes sure that values put into event are valid.
     */

    public void checkValidity(String id, String startDate, String endDate, boolean startNull, boolean endNull, String type) throws IOException {
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
