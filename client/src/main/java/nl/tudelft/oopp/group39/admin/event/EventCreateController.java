package nl.tudelft.oopp.group39.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;


public class EventCreateController extends EventListController {

    private ObjectMapper mapper = new ObjectMapper();
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
    private ComboBox<String> userComboBox;
    @FXML
    private CheckBox globalCheckbox;

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
    }
    /**
     * .
     */

    public void initData() throws JsonProcessingException {
        dateMessage.setText("");

        ObservableList<String> data = getUserIds();
        userComboBox.setItems(data);
        userComboBox.getSelectionModel().selectFirst();
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
        ) : start.toString() + " 00:00:00";
        LocalDate end = endField.getValue();
        String userId = userComboBox.getValue();
        boolean endNull = end == null;
        boolean globalBool = globalCheckbox.isSelected();
        String endDate = endNull ? LocalDateTime.now().toString() : end.toString() + " 23:59:00";
        checkValidity(startDate, endDate, startNull, endNull, title, globalBool, userId);
    }
    /**
     * Communicates information to create event to server.
     */

    public void createEventFinal(
            String title, String startDate, String endDate,
            boolean globalBool, String userId) throws IOException {
        Event newEvent = new Event(title,startDate,endDate, globalBool,userId, new ArrayList<>());
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
          String title,
          boolean globalBool,
          String userId) throws IOException {
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
                createEventFinal(title, startDate, endDate, globalBool, userId);
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
                createEventFinal(title, start.format(formatter), endDate, globalBool, userId);
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
                createEventFinal(title, startDate, end.format(formatter), globalBool, userId);
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
