package nl.tudelft.oopp.group39.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

//Supress ALL only suppresses booleans on line 110 not being used
@SuppressWarnings("ALL")
public class EventEditController extends EventListController {

    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Event abcEvent;
    @FXML
    private Button backbtn;
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private MenuBar navBar;
    @FXML
    private TextArea dateMessage;
    @FXML
    private CheckBox globalCheckbox;

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
        titleField.setPromptText(abcEvent.getTitle());
        globalCheckbox.setSelected(abcEvent.isGlobal());


        startField.setPromptText(abcEvent.getStartsAt().toString());
        endField.setPromptText(abcEvent.getEndsAt().toString());
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
        String title = titleField.getText();
        title = title.contentEquals("") ? abcEvent.getTitle() : title;
        LocalDate start = startField.getValue();
        boolean startNull = start == null;
        String startDate = startNull ? abcEvent.getStartsAt().toString() : start.toString()
                + " 00:00:00";
        LocalDate end = endField.getValue();
        boolean endNull = end == null;
        boolean globalBool = globalCheckbox.isSelected();
        String endDate = endNull ? abcEvent.getEndsAt().toString() : end.toString() + " 23:59:00";
        String id = Long.toString(abcEvent.getId());
        checkValidity(id, startDate, endDate, startNull, endNull, title, globalBool);
    }
    /**
     * Communicates edit of event to server.
     */

    public void createEventFinal(
            String id,
            String title,
            String startDate,
            String endDate,
            Boolean globalBool) throws IOException {
        System.out.println(globalBool);
        ServerCommunication.updateEvent(id, title, startDate, endDate, globalBool);
        getBack();
        createAlert("Updated: " + abcEvent.getTitle());
    }
    /**
     * Makes sure that values put into event are valid.
     */

    public void checkValidity(
            String id,
            String startDate,
            String endDate,
            boolean startNull,
            boolean endNull,
            String title,
            Boolean globalBool) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        if (!end.isAfter(start)) {
            dateMessage.setStyle("-fx-text-fill: Red");
            dateMessage.setText(
                "The end date needs to be later than the start date!\n(Start date was: "
                + start.toString() + ", end date was: " + end.toString() + " )");
            return;
        }
        if (!start.isAfter(LocalDateTime.now())) {
            dateMessage.setStyle("-fx-text-fill: Red");
            dateMessage.setText(
                "The start date needs to be later than today!\n(Start date was: "
                + start.toString()
                + ", end date was: "
                + end.toString() + " )");
            return;
        }
        createEventFinal(id, title, startDate, endDate, globalBool);
    }

}
