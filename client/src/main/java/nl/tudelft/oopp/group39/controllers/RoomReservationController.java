package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.swing.text.html.ImageView;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Booking;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;


public class RoomReservationController extends MainSceneController {
    @FXML
    private DatePicker date;
    @FXML
    private ImageView image;
    @FXML
    private Button reserveButton;
    @FXML
    private Button backButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button homeButton;
    @FXML
    private ComboBox<String> fromTime;
    @FXML
    private ComboBox<String> toTime;
    @FXML
    private Label roomName;
    @FXML
    private Label roomDetails;
    @FXML
    private VBox roomInfo;
    @FXML
    private Label titleLabel;

    private Building building;
    private Room room;

    /**
     * Generates an alert when called.
     *
     * @param alertType the type of alert
     * @param title     the title of the alert
     * @param content   the content of the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Sets the entire scene up, so loading the timeslots, room information and sets up the buttons.
     *
     * @param room     the room you've selected
     * @param building the building of the room you've selected
     */
    public void setup(Room room, Building building) throws JsonProcessingException {
        this.building = building;
        this.room = room;
        titleLabel.setText(room.getName());
        loadTimeslots();
        loadRoom(room);
        reserveButton.setOnAction(event -> {
            try {
                reserveRoom(room);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Reserves a room and shows a confirmed alert if the action was successful.
     */
    @FXML
    private void reserveRoom(Room room) throws IOException {
        LocalDate bookingDate = date.getValue();
        String bookingStart = fromTime.getValue() + ":00";
        String bookingEnd = toTime.getValue() + ":00";

        if (checkEmpty(date, fromTime, toTime)) {
            if (loggedIn) {
                String dateString = date.getValue()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long roomId = room.getId();
                String roomIdString = "" + roomId;
                String username = MainSceneController.user.getUsername();
                ServerCommunication.addBooking(
                    dateString,
                    bookingStart,
                    bookingEnd,
                    username,
                    roomIdString
                );

                showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
                System.out.println(dateString
                    + bookingStart + bookingEnd
                    + username + roomIdString);

                System.out.println(bookingDate);
                System.out.println(bookingStart + "\n" + bookingEnd);
                backToRoom();
            } else {
                showAlert(Alert.AlertType.ERROR, "",
                    "Please log in if you want to reserve a room.");
                goToLoginScene();
            }
        }
    }

    /**
     * Checks if the date, start and end fields are empty.
     * If that's the case, the method will generate an error message and return false.
     *
     * @param date  the date of the reservation
     * @param start start time of reservation
     * @param end   end time of reservation
     * @return true or false depending on whether or not the date, start and end fields are filled.
     */
    public boolean checkEmpty(DatePicker date, ComboBox<String> start, ComboBox<String> end) {
        if (date.getValue() == null
            || start.getSelectionModel().isEmpty()
            || end.getSelectionModel().isEmpty()
        ) {
            showAlert(Alert.AlertType.ERROR, "", "Please fill in all the fields.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Retrieves times when room is booked filtered on date.
     *
     * @param date selected date
     * @return List of integers
     * @throws JsonProcessingException when there is something wrong with processing
     */
    public List<Integer> getBookedTimes(String date) throws JsonProcessingException {
        String bookings = ServerCommunication.getBookings((int) room.getId(), date);
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
     * Initiates the timeslots for the ComboBoxes to load.
     *
     * @return a list with LocalTimes from 00:00 to 23:00
     */
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
     * Updates the end timeslots based on selected date and from time.
     *
     * @param date selected date
     * @param time selected time
     * @throws JsonProcessingException when there is something wrong with processing
     */
    public void updateEndSlots(String date, String time) throws JsonProcessingException {
        int timeAsInt = Integer.parseInt(time.split(":")[0]);
        List<Integer> bookedTimes = getBookedTimes(date);
        toTime.getItems().clear();
        int closed = Integer.parseInt(building.getClosed().split(":")[0]);
        List<Integer> times = new ArrayList<>();
        for (int i = timeAsInt + 1; i < timeAsInt + 5; i++) {
            if (bookedTimes.size() != 0) {
                int smallest = bookedTimes.get(0);
                int biggest = bookedTimes.get(1);
                for (int j = 0; j < bookedTimes.size(); j = j + 2) {
                    if (bookedTimes.get(j) < smallest) {
                        smallest = bookedTimes.get(j);
                    }
                    if (bookedTimes.get(j + 1) > biggest) {
                        biggest = bookedTimes.get(j + 1);
                    }
                }
                if (i <= smallest && i <= closed) {
                    times.add(i);
                } else if (i > biggest && i <= closed) {
                    times.add(i);
                }
            } else {
                if (i <= closed) {
                    times.add(i);
                }
            }
        }
        for (int t = timeAsInt + 2; t < timeAsInt + 5; t++) {
            if (!times.contains(t - 1)) {
                Integer remove = t;
                times.remove(remove);
            }
        }
        for (int i : times) {
            String timeSlot;
            if (i <= closed) {
                if (i < 10) {
                    timeSlot = "0" + i + ":00";
                } else {
                    timeSlot = i + ":00";
                }
                toTime.getItems().add(timeSlot);
            }
        }
    }

    public void updateStartSlots(String date) throws JsonProcessingException {
        fromTime.getItems().clear();
        fromTime.getItems().addAll(initiateTimeslots(date));
    }


    /**
     * Loads the room into the VBox containing the room information.
     */
    private void loadRoom(Room room) {
        String name = room.getName();
        String roomDescription = room.getDescription();
        int roomCapacity = room.getCapacity();
        String roomFacilities = room.facilitiesToString();

        roomName.setText(name);

        roomDetails.setText(roomDescription
            + "\n" + "Capacity: " + roomCapacity
            + "\n" + "Facilities: " + roomFacilities);
    }

    /**
     * Loads the timeslots into the ComboBoxes.
     */
    private void loadTimeslots() throws JsonProcessingException {
        date.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        date.setValue(LocalDate.now());
        updateStartSlots(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        updateEndSlots(
            date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            fromTime.getItems().get(0));
        date.setOnAction(event -> {
            try {
                updateStartSlots(date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        fromTime.setOnAction(event -> {
            try {
                updateEndSlots(
                    date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    fromTime.getValue()
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }


    /**
     * Switches to login page when the Login button is clicked.
     *
     * @throws IOException throws an IOException
     */
    @FXML
    private void switchLogin() throws IOException {
        goToLoginScene();
    }

    /**
     * Switches to the homepage when the *SomeName* button is clicked.
     *
     * @throws IOException throws an IOException
     */
    @FXML
    private void switchMain() throws IOException {
        goToBuildingScene();
    }

    /**
     * Returns the user back to the room page when the back button is clicked.
     *
     * @throws IOException when there is an io exception
     */
    @FXML
    private void backToRoom() throws IOException {
        goToRoomsScene(building);
    }


}
