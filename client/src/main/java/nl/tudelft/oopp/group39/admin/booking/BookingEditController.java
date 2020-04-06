package nl.tudelft.oopp.group39.admin.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

public class BookingEditController extends BookingListController {
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Booking booking;
    private String date;
    private Room room;
    private HashMap<String, Long> roomIdByNameMap = new HashMap<>();
    private HashMap<String, String> userIdByNameMap = new HashMap<>();
    private Building building;

    @FXML
    private ComboBox<String> userBoxN;
    @FXML
    private ComboBox<String> roomBox;
    @FXML
    private ComboBox<String> startTimeBox;
    @FXML
    private ComboBox<String> endTimeBox;
    @FXML
    private DatePicker reservationDate;
    @FXML
    private Button backbtn;
    @FXML
    private TextArea dateMessage;

    /**
     * Initializes scene.
     */
    public void customInit() {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        roomBox.valueProperty().addListener((ov, t, t1) -> {
            String reservationStartString = roomBox.getValue();
            try {
                room = ServerCommunication.getRoom(roomIdByNameMap.get(reservationStartString));
                building = getBuilding(room);
                setTimeSlots(date);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
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
    }

    /**
     * Initializes the data necessary for bookings.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void initData(Booking booking) throws JsonProcessingException {
        this.booking = booking;
        customInit();
        initRooms();
        initUsers();
    }

    /**
     * Gets the building.
     *
     * @param room                     the room of the building
     * @return                         the building of the room
     * @throws JsonProcessingException when there is a processing exception
     */
    private Building getBuilding(Room room) throws JsonProcessingException {
        String buildingString = ServerCommunication.getBuilding(room.getBuilding());
        ObjectNode roomBody = (ObjectNode) mapper.readTree(buildingString).get("body");
        buildingString = mapper.writeValueAsString(roomBody);
        return mapper.readValue(buildingString, Building.class);
    }

    /**
     * Initializes the rooms.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    private void initRooms() throws JsonProcessingException {
        String rooms = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(rooms).get("body");
        rooms = mapper.writeValueAsString(body);
        Room[] list = mapper.readValue(rooms, Room[].class);
        List<String> dataList = new ArrayList<>();
        for (Room room : list) {
            roomIdByNameMap.put(room.getName(), room.getId());
            dataList.add(room.getName());
        }
        this.room = ServerCommunication.getRoom(booking.getRoom());
        this.building = getBuilding(room);
        this.date = booking.getDate();
        setTimeSlots(date);
        roomBox.setPromptText(room.getName());
        ObservableList<String> data = FXCollections.observableArrayList(dataList);
        roomBox.setItems(data);
    }

    /**
     * Sets the timeslots for the booking.
     *
     * @param date                     the date for the time slots
     * @throws JsonProcessingException when there is a processing exception
     */
    private void setTimeSlots(String date) throws JsonProcessingException {
        List<String> timeSlots = initiateTimeslots(date);
        startTimeBox.getItems().clear();
        endTimeBox.getItems().clear();
        ObservableList<String> listString = FXCollections.observableArrayList(timeSlots);
        startTimeBox.setPromptText(booking.getStartTime());
        endTimeBox.setPromptText(booking.getEndTime());
        startTimeBox.setItems(listString);
        endTimeBox.setItems(listString);
    }

    /**
     * Initializes user data for updating.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    private void initUsers() throws JsonProcessingException {
        String users = ServerCommunication.get(ServerCommunication.user);
        ArrayNode body = (ArrayNode) mapper.readTree(users).get("body");
        users = mapper.writeValueAsString(body);
        User[] list = mapper.readValue(users, User[].class);
        List<String> dataList = new ArrayList<>();
        for (User user : list) {
            userIdByNameMap.put(user.getEmail(), user.getUsername());
            dataList.add(user.getEmail());
        }
        ObservableList<String> data = FXCollections.observableArrayList(dataList);
        User abcUser = ServerCommunication.getUser(booking.getUser());
        userBoxN.setPromptText(abcUser.getEmail());
        userBoxN.setItems(data);
    }

    /**
     * Initializes the time slots.
     *
     * @param date                     the date of the reservation
     * @return                         a list of the times
     * @throws JsonProcessingException when there is a processing exception
     */
    private List<String> initiateTimeslots(String date) throws JsonProcessingException {
        List<String> times = new ArrayList<>();
        int open = Integer.parseInt(building.getOpen().toString().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().toString().split(":")[0]);
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
     * Returns a list containing times that are booked.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public List<Integer> getBookedTimes(String date) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String roomDate = "room=" + room.getId() + "&date=" + date;
        String bookings = ServerCommunication.getBookings(roomDate);
        System.out.println(bookings);
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
     * Goes back to main bookings panel.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void getBack() throws IOException {
        switchBookingsView(currentStage);
    }

    /**
     * Edits values of booking.
     *
     * @throws IOException if an error occurs during loading
     */
    public void editBooking() throws IOException {
        Object roomObj = roomBox.getValue();
        String roomId = roomObj == null ? Long.toString(
                booking.getRoom()) : Long.toString(roomIdByNameMap.get(roomObj.toString()));
        Object userObj = userBoxN.getValue();
        String user = userObj == null ? booking.getUser() : userIdByNameMap.get(userObj.toString());
        LocalDate reservationDateValue = reservationDate.getValue();
        String reservationDateString = reservationDateValue
                == null ? booking.getDate() : reservationDateValue.toString();
        String id = Integer.toString(booking.getId());
        Object reservationStartValue = startTimeBox.getValue();
        String reservationStartString = reservationStartValue
                == null ? booking.getStartTime() : reservationStartValue.toString() + ":00";
        Object reservationEndValue = endTimeBox.getValue();
        String reservationEndString = reservationEndValue
                == null ? booking.getEndTime() : reservationEndValue.toString() + ":00";
        System.out.println(reservationStartString + " : " + reservationEndString);
        ServerCommunication.updateBooking(
               reservationDateString,
               reservationStartString,
               reservationEndString,
               user,
               roomId,
               id);
        getBack();
        createAlert("Updated the booking!");
    }
}
