package nl.tudelft.oopp.group39.controllers.Admin.Booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.Event.EventListController;
import nl.tudelft.oopp.group39.models.Booking;
import nl.tudelft.oopp.group39.models.Reservation;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;

public class BookingEditController extends EventListController implements Initializable {

    private Booking booking;
    private HashMap<String, Integer> RoomIdByNameMap = new HashMap<>();
    private HashMap<String, String> UserIdByNameMap = new HashMap<>();

    @FXML
    private ComboBox userBox;
    @FXML
    private ComboBox roomBox;
    @FXML
    private DatePicker reservationDate;
    @FXML
    private Button backbtn;
    @FXML
    private MenuBar navBar;
    @FXML
    private TextArea dateMessage;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
    }

    public void initData(Booking booking) throws JsonProcessingException {
        this.booking = booking;
        initRooms();
        initUsers();

    }

    private void initRooms() throws JsonProcessingException {
        String rooms = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(rooms).get("body");
        rooms = mapper.writeValueAsString(body);
        Room[] list = mapper.readValue(rooms, Room[].class);
        Room[] roomList = list;
        List<String> dataList = new ArrayList<>();
        for (Room room : roomList) {
            RoomIdByNameMap.put(room.getName(), (int)room.getId());
            dataList.add(room.getName());
        }
        ObservableList<String> data = FXCollections.observableArrayList(dataList);
        Room cRoom = ServerCommunication.getRoom(booking.getRoom());
        roomBox.setPromptText(cRoom.getName());
        roomBox.setItems(data);
    }

    private void initUsers() throws JsonProcessingException {
        String users = ServerCommunication.get(ServerCommunication.user);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        User[] list = mapper.readValue(users, User[].class);
        User[] roomList = list;
        List<String> dataList = new ArrayList<>();
        for (User user : roomList) {
            UserIdByNameMap.put(user.getEmail(), user.getUsername());
            dataList.add(user.getEmail());
        }
        ObservableList<String> data = FXCollections.observableArrayList(dataList);
        User cUser = ServerCommunication.getUser(booking.getUser());
        userBox.setPromptText(cUser.getEmail());
        userBox.setItems(data);
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Booking/BookingList.fxml", currentstage);
    }

    public void editBooking() throws IOException {
        Object roomObj = roomBox.getValue();
        String room = roomBox == null ? Integer.toString(booking.getRoom()) : roomObj.toString();
        Object userObj = userBox.getValue();
        String user = userObj == null ? booking.getUser() : userObj.toString();
//        LocalDate timeOfPickUp = reservationDate.getValue();
//        boolean startNull = start == null;
//        String startDate = startNull ? cEvent.getStartDate() : start.toString();
//        String id = Integer.toString(cEvent.getId());
//        checkValidity(id, startDate, endDate, startNull, endNull, type);
    }

    public void createEventFinal(String id, String type, String startDate, String endDate) throws IOException {
//        ServerCommunication.updateEvent(id, type, startDate, endDate);
//        getBack();
//        createAlert("Updated: " + cEvent.getType());
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
