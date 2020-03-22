package nl.tudelft.oopp.group39.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.swing.text.html.ImageView;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;


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

    private Building building;
    private Room room;

    public void setup(Room room, Building building) {
        this.building = building;
        this.room = room;
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
     * Generates an alert when called.
     *
     * @param alertType
     * @param title
     * @param content
     */
    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Reserves a room and shows a confirmed alert if the action was successful
     *
     * @return
     */
    @FXML
    private void reserveRoom(Room room) throws IOException {
        LocalDate bookingDate = date.getValue();
        String bookingStart = fromTime.getValue() + ":00";
        String bookingEnd = toTime.getValue() + ":00";

        if (checkEmpty(bookingDate, bookingStart, bookingEnd)) {
            if (checkTime(bookingStart, bookingEnd)) {
                try {
                    String username = MainSceneController.user.getUsername();
                    User user = ServerCommunication.getUser(username);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "", "Please log in if you want to reserve a room.");
                    goToLoginScene();
                    return;
                }
                String dateString = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long roomId = room.getId();
                String roomIdString = "" + roomId;
                String username = MainSceneController.user.getUsername();
                ServerCommunication.addBooking(dateString, bookingStart, bookingEnd, username, roomIdString);

                showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
                System.out.println(dateString + bookingStart + bookingEnd + username + roomIdString);

                System.out.println(bookingDate);
                System.out.println(bookingStart + "\n" + bookingEnd);
                backToRoom();
            }
        } else {
            showAlert(Alert.AlertType.INFORMATION, "", "Reservation failed.");
        }

    }

    /**
     * Checks whether or not the chosen times meet the requirements (at most 4 hours)
     *
     * @param start
     * @param end
     * @return
     */
    public boolean checkTime(String start, String end) {
        int timeDifference = (Integer.parseInt(end.split(":")[0]) - Integer.parseInt(start.split(":")[0]));
        if (timeDifference > 4) {
            showAlert(Alert.AlertType.ERROR, "", "Please make sure that the duration of the reservation is at most 4 hours.");
            return false;
        } else if (timeDifference <= 0) {
            if (timeDifference > -20) {
                showAlert(Alert.AlertType.ERROR, "", "Please pick a real duration of the reservation.");
                return false;
            } else {
                showAlert(Alert.AlertType.ERROR, "", "You cannot reserve past midnight!");
                return false;
                //return true; for now you cant i guess
            }
        } else {
            return true;
        }
    }

    /**
     * Checks if the date, start and end fields are empty.
     * If that's the case, the method will generate an error message and return false.
     *
     * @param date
     * @param start
     * @param end
     * @return true or false depending on whether or not the date, start and end fields are filled in.
     */
    public boolean checkEmpty(LocalDate date, String start, String end) {
        if (date == null || start == null || end == null) {
            showAlert(Alert.AlertType.ERROR, "", "Please fill in all the fields.");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Initiates the timeslots for the ComboBoxes to load.
     *
     * @return a list with LocalTimes from 00:00 to 23:00
     */
    private List<String> initiateTimeslots() {
        String bookings = ServerCommunication.getBookings((int) room.getId(), "2020-03-22");
        int bookedStart = 13;
        int bookedEnd = 15;
        System.out.println(bookings);
        List<String> times = new ArrayList<>();
        int open = Integer.parseInt(building.getOpen().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().split(":")[0]);

        for (int i = open; i < closed; i++) {
            if (i < bookedStart || i >= bookedEnd) {
                String time;
                if (i < 10) {
                    time = "0" + i + ":00";
                } else {
                    time = i + ":00";
                }
                times.add(time);
            }
        }
        return times;
    }


    /**
     * Loads the room into the VBox containing the room information.
     */
    private void loadRoom(Room room) {
        String roomName = room.getName();
        String roomDescription = room.getDescription();
        int roomCapacity = room.getCapacity();
        String roomFacilities = room.facilitiesToString();

        Label name = (Label) roomInfo.lookup("#roomName");
        name.setText(roomName);

        Label details = (Label) roomInfo.lookup("#roomDetails");
        details.setText(roomDescription
            + "\n" + "Capacity: " + roomCapacity
            + "\n" + "Facilities: " + roomFacilities);
        System.out.println(details);

    }

    /**
     * Loads the timeslots into the ComboBoxes
     */
    private void loadTimeslots() {
        fromTime.getItems().addAll(initiateTimeslots());
        toTime.getItems().addAll(initiateTimeslots());
    }


    /**
     * Switches to login page when the Login button is clicked
     *
     * @throws IOException
     */
    @FXML
    private void switchLogin() throws IOException {
        goToLoginScene();
    }

    /**
     * Switches to the homepage when the *SomeName* button is clicked
     *
     * @throws IOException
     */
    @FXML
    private void switchMain() throws IOException {
        goToMainScene();
    }

    /**
     * Returns the user back to the room page when the back button is clicked
     *
     * @throws IOException
     */
    @FXML
    private void backToRoom() throws IOException {
        goToRoomsScene(building);
    }


}
