package nl.tudelft.oopp.group39.controllers.Admin.Booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;

public class BookingCreateController extends EventListController implements Initializable {

    private Booking booking;
    private String date;
    private Room room;
    private HashMap<String, Integer> RoomIdByNameMap = new HashMap<>();
    private HashMap<String, String> UserIdByNameMap = new HashMap<>();
    private Building building;
    private String start;
    private String end;

    @FXML
    private ComboBox userBox;
    @FXML
    private ComboBox roomBox;
    @FXML
    private ComboBox startTimeBox;
    @FXML
    private ComboBox endTimeBox;
    @FXML
    private DatePicker reservationDate;
    @FXML
    private Button backbtn;
    @FXML
    private MenuBar navBar;
    @FXML
    private TextArea dateMessage;

    /**
     * TODO sasa.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(navBar);
        roomBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                String reservationStartString = roomBox.getValue().toString();
                try {
                    room = ServerCommunication.getRoom(RoomIdByNameMap.get(reservationStartString));
                    building = getBuilding(room);
                    setTimeSlots(date);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
        reservationDate.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.date = reservationDate.getValue().toString();
            try {
                setTimeSlots(date);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        try {
            initRooms();
            initUsers();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Building getBuilding(Room room) throws JsonProcessingException {
        String buildingString = ServerCommunication.getBuilding(room.getBuilding());
        ObjectNode nBody = (ObjectNode) mapper.readTree(buildingString).get("body");
        buildingString = mapper.writeValueAsString(nBody);
        return mapper.readValue(buildingString, Building.class);
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
        this.room = roomList[0];
        this.building = getBuilding(room);
        this.date = LocalDate.now().toString();
        setTimeSlots(date);
        roomBox.setPromptText(room.getName());
        roomBox.setItems(data);
    }

    private void setTimeSlots(String date) throws JsonProcessingException {
        List<String> timeSlots = initiateTimeslots(date);
        ObservableList<String> listString = FXCollections.observableArrayList(timeSlots);
        startTimeBox.getItems().clear();
        endTimeBox.getItems().clear();
        start = listString.get(0);
        end = listString.get(1);
        startTimeBox.setPromptText(start);
        endTimeBox.setPromptText(end);
        startTimeBox.setItems(listString);
        endTimeBox.setItems(listString);
    }

    private void initUsers() throws JsonProcessingException {
        String users = ServerCommunication.get(ServerCommunication.user);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        User[] list = mapper.readValue(users, User[].class);
        User[] userList = list;
        List<String> dataList = new ArrayList<>();
        for (User user : userList) {
            UserIdByNameMap.put(user.getEmail(), user.getUsername());
            dataList.add(user.getEmail());
        }
        ObservableList<String> data = FXCollections.observableArrayList(dataList);
        User cUser = userList[0];
        userBox.setPromptText(cUser.getEmail());
        userBox.setItems(data);
    }

    private List<String> initiateTimeslots(String date) throws JsonProcessingException {
        List<String> times = new ArrayList<>();
        int open = Integer.parseInt(building.getOpen().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().split(":")[0]);
        List<Integer> bookedTimes = getBookedTimes(date);
        for (int i = open; i < closed; i++) {
            String time;
            if (i < 10) {
                time = "0" + i + ":00";
            } else {
                time = i + ":00";
            }
            times.add(time);
        }
        if (bookedTimes.size() != 0) {
            for (int j = 0; j < bookedTimes.size(); j = j + 2) {
                for (int i = open; i < closed; i++) {
                    if (i >= bookedTimes.get(j) && i < bookedTimes.get(j + 1)) {
                        String time;
                        if (i < 10) {
                            time = "0" + i + ":00";
                        } else {
                            time = i + ":00";
                        }
                        times.remove(time);
                    }
                }
            }
        }
        return times;
    }
    /**
     * Returns a list containing times where there is a booking.
     */

    public List<Integer> getBookedTimes(String date) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String bookings = ServerCommunication.getBookings((int) room.getId(), date);
        ArrayNode body = (ArrayNode) mapper.readTree(bookings).get("body");
        String bookingString = mapper.writeValueAsString(body);
        Booking[] bookingsList = mapper.readValue(bookingString, Booking[].class);
        List<Integer> bookedTimes = new ArrayList<>();
        for (Booking booking : bookingsList) {
            int startTime = Integer.parseInt(booking.getStartTime().split(":")[0]);
            bookedTimes.add(startTime);
            int endTime = Integer.parseInt(booking.getEndTime().split(":")[0]);
            bookedTimes.add(endTime);
        }
        System.out.println(bookedTimes);
        return bookedTimes;
    }

    /**
     * Goes back to main booking panel.
     */

    @FXML
    private void getBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        mainSwitch("/Admin/Booking/BookingList.fxml", currentstage);
    }

    public void createBooking() throws IOException {
        Object roomObj = roomBox.getValue();
        String roomId = roomObj == null ? Integer.toString((int)room.getId()) : Integer.toString(RoomIdByNameMap.get(roomObj.toString()));
        Object userObj = userBox.getValue();
        boolean userNull = userObj == null;
        if (userNull) {
            dateMessage.setStyle("-fx-text-fill: Red");
            dateMessage.setText("The user needs to be filled in!");
            return;
        }
        String user = UserIdByNameMap.get(userObj.toString());
        LocalDate reservationDateValue = reservationDate.getValue();
        String reservationDateString = reservationDateValue == null ? date : reservationDateValue.toString();
        Object reservationStartValue = startTimeBox.getValue();
        String reservationStartString = reservationStartValue == null ? start + ":00" : reservationStartValue.toString() + ":00";
        Object reservationEndValue = endTimeBox.getValue();
        String reservationEndString = reservationEndValue == null ? end + ":00" : reservationEndValue.toString() + ":00";
        ServerCommunication.addBooking(reservationDateString, reservationStartString, reservationEndString, user, roomId);
        getBack();
        createAlert("Updated the booking!");
    }

}
