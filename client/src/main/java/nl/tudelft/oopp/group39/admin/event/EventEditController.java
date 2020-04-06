package nl.tudelft.oopp.group39.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

//Supress ALL only suppresses booleans on line 110 not being used
@SuppressWarnings("ALL")
public class EventEditController extends EventListController {
    private Stage currentStage;
    private Event abcEvent;
    private List<CheckBox> rooms = new ArrayList<>();
    @FXML
    private CheckBox selectAll;
    @FXML
    private VBox roomSelector;
    @FXML
    private Button backbtn;
    @FXML
    private TextField titleField;
    @FXML
    private MenuBar navBar;
    @FXML
    private DatePicker startField;
    @FXML
    private DatePicker endField;
    @FXML
    private Label dateMessage;
    @FXML
    private ComboBox<String> userComboBox;
    @FXML
    private CheckBox globalCheckbox;

    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
    }

    /**
     * Initializes data for usage in editing event.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void initData(Event abcEvent) throws JsonProcessingException {
        customInit();
        setRoomSelector();
        this.abcEvent = abcEvent;
        titleField.setPromptText(abcEvent.getTitle());
        globalCheckbox.setSelected(abcEvent.isGlobal());
        ObservableList<String> data = getUserIds();
        userComboBox.setItems(data);
        String oldUser = abcEvent.getUser();
        if (oldUser == null) {
            userComboBox.getSelectionModel().selectFirst();

        } else {
            userComboBox.getSelectionModel().select(oldUser);
        }
        startField.setPromptText(abcEvent.getStartsAt().toString());
        endField.setPromptText(abcEvent.getEndsAt().toString());
        setNavBar(navBar, currentStage);
        uncheckUserComboBox();
    }

    /**
     * Goes back to main Event panel.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void getBack() throws IOException {
        switchEventView(currentStage);
    }

    /**
     * Edits the values of an event.
     *
     * @throws IOException when there is an IO exception
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
        String userId = userComboBox.getValue();
        String endDate = endNull ? abcEvent.getEndsAt().toString() : end.toString() + " 23:59:00";
        String id = Long.toString(abcEvent.getId());
        checkValidity(id, startDate, endDate, startNull, endNull, title, userId, globalBool);
    }

    /**
     * Communicates edit of event to server.
     */
    public void createEventFinal(
            String id,
            String title,
            String startDate,
            String endDate,
            String userId,
            boolean globalBool) throws IOException {
        Event newEvent = new Event(title,
            startDate,
            endDate,
            globalBool,
            userId,
            new ArrayList<>());
        ServerCommunication.updateEvent(newEvent, Long.valueOf(id));
        goToAdminEventScene();
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
            String userId,
            boolean globalBool) throws IOException {
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
        createEventFinal(id, title, startDate, endDate, userId, globalBool);
    }

    public List<Long> createRoomsList() {
        List<Long> roomsList = new ArrayList<>();
        for (CheckBox roomBox : rooms) {
            if (roomBox.isSelected()) {
                roomsList.add(Long.parseLong(roomBox.getId()));
            }
        }
        return roomsList;
    }

    public boolean checkRoomSelected() {
        boolean roomSelected = false;
        for (CheckBox roomBox : rooms) {
            if (roomBox.isSelected()) {
                roomSelected = true;
            }
        }
        if (!roomSelected) {
            dateMessage.setStyle("-fx-text-fill: Red");
            dateMessage.setText("Please select the rooms the event affects");
        }
        return roomSelected;
    }

    public void setRoomSelector() throws JsonProcessingException {
        String roomString = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(roomString).get("body");
        roomString = mapper.writeValueAsString(body);
        Room[] roomList = mapper.readValue(roomString, Room[].class);
        for (Room room : roomList) {
            CheckBox roomBox = new CheckBox(room.getName());
            roomBox.setId(room.getId().toString());
            rooms.add(roomBox);
            roomSelector.getChildren().add(roomBox);
        }
        System.out.println(abcEvent);
        List<Long> selectedRooms = abcEvent.getRooms();
        System.out.println(selectedRooms);
        for (Long roomId : selectedRooms) {
            for (CheckBox roomBox : rooms) {
                if (Long.parseLong(roomBox.getId()) == roomId) {
                    roomBox.setSelected(true);
                }
            }
        }
    }

    public void selectAllRooms() {
        if (selectAll.isSelected()) {
            for (CheckBox roomBox : rooms) {
                roomBox.setSelected(true);
            }
        } else {
            for (CheckBox roomBox : rooms) {
                roomBox.setSelected(false);
            }
        }
    }

    /**
     * If the event is selected to be global, this method disables the combobox to choose a user.
     */
    public void uncheckUserComboBox() {
        if (globalCheckbox.isSelected()) {
            System.out.println("It is now changed");
            userComboBox.setDisable(true);
        } else {
            userComboBox.setDisable(false);
        }
    }
}
