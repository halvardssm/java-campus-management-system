package nl.tudelft.oopp.group39.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;



public class EventCreateController extends EventListController {

    private Stage currentStage;
    @FXML
    private Button backbtn;
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private TextArea dateMessage;
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit() {
        try {
            initData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }
    /**
     * .
     */

    public void initData() throws JsonProcessingException {
        dateMessage.setText("");

        startField.setPromptText(LocalDate.now().toString());
        endField.setPromptText(LocalDate.now().toString());
    }
    /**
     * Gains the information needed to create event.
     */

    public void createEvent() throws IOException {
        dateMessage.setText("");
        String title = titleField.getText();
        title = title == null ? "" : title;
        LocalDate start = startField.getValue();
        boolean startNull = start == null;
        String startDate = startNull ? LocalDateTime.now().toString(
        ) : start.toString() + "00:00:00";
        LocalDate end = endField.getValue();
        boolean endNull = end == null;
        String endDate = endNull ? LocalDateTime.now().toString() : end.toString() + " 23:59:00";
        checkValidity(startDate, endDate, startNull, endNull, title);
    }
    /**
     * Communicates information to create event to server.
     */

    public void createEventFinal(
            String title, String startDate, String endDate) throws IOException {
        Event newEvent = new Event(title,startDate,endDate, true,null, new ArrayList<>());
        createAlert(ServerCommunication.addEvent(newEvent));
        getBack();
        createAlert("Created an event of type: " + title);
    }
    /**
     * Makes sure that values put into event are valid.
     */

    public void checkValidity(
          String startDate,
          String endDate,
          boolean startNull,
          boolean endNull,
          String title) throws IOException {
        if (!endNull || !startNull) {
            if (!endNull && !startNull) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                LocalDateTime end = LocalDateTime.parse(endDate, formatter);
                if (!end.isAfter(start)) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The end date needs to be later than the start date!");
                    return;
                }
                if (!start.isAfter(LocalDateTime.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!\n"
                            + "(Inputted start date was: "
                            + start.toString() + ", Inputted end date was:" + end.toString() + ")");
                    return;
                }
                createEventFinal(title, startDate, endDate);
                return;
            }
            if (!endNull) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime end = LocalDateTime.parse(endDate, formatter);
                LocalDateTime start = end.minusDays(1);
                if (!start.isAfter(LocalDateTime.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!\n"
                            + "(Automatically generated start date was: " + start.toString() + ")");
                    return;
                }
                createEventFinal(title, start.format(formatter), endDate);
                return;
            }
            if (!startNull) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                LocalDateTime end = start.plusDays(1);
                if (!start.isAfter(LocalDateTime.now())) {
                    dateMessage.setStyle("-fx-text-fill: Red");
                    dateMessage.setText("The start date needs to be later than today!"
                            + "\n(Inputted start date was: "
                        + start.toString() + " )");
                    return;
                }
                createEventFinal(title, startDate, end.format(formatter));
                return;
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
        switchEventView(currentStage);
    }

}
