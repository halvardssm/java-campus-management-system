package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.User;


public class RoomReservationController extends MainSceneController implements Initializable {
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
    private ComboBox fromTime;
    @FXML
    private ComboBox toTime;
    @FXML
    private Label roomName;
    @FXML
    private Label roomDetails;
    @FXML
    private VBox roomInfo;

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
    private void reserveRoom() throws IOException {
        LocalDate bookingDate = date.getValue();
        LocalTime bookingStart = (LocalTime) fromTime.getValue();
        LocalTime bookingEnd = (LocalTime) toTime.getValue();

        if (checkEmpty(bookingDate, bookingStart, bookingEnd)) {
            if (checkTime(bookingStart, bookingEnd)) {
                String username = MainSceneController.user.getUsername();
                User user = ServerCommunication.getUser(username);
                long roomId = MainSceneController.room.getId();
                //Room room = ServerCommunication.getRoom(roomId);
                //ServerCommunication.addBooking(date, fromTime, toTime, user, )
                //Room room = ServerCommunication.getRoom();
                //
                showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
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
    public boolean checkTime(LocalTime start, LocalTime end) {
        int timeDifference = (end.getHour() - start.getHour());
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
    public boolean checkEmpty(LocalDate date, LocalTime start, LocalTime end) {
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
    private List<LocalTime> initiateTimeslots() {
        List<LocalTime> times = new ArrayList<>();
        LocalTime n = LocalTime.of(0, 0);

        for (int i = 0; i < 24; i++) {
            times.add(n);
            LocalTime add = LocalTime.of(1, 0);
            n = n.plusHours(add.getHour());
        }
        return times;
    }

    /**
     * Loads the room into the VBox containing the room information.
     */
    private void loadRoom() {
        try {
            roomInfo = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));
            String roomName = room.getName();
            String roomDescription = room.getDescription();
            int roomCapacity = room.getCapacity();
            ArrayNode roomFacilities = room.getFacilities();

            Label name = (Label) roomInfo.lookup("#roomName");
            name.setText(roomName);

            Label details = (Label) roomInfo.lookup("#roomDetails");
            details.setText(roomDescription
                + "\n" + "Capacity: " + roomCapacity
                + "\n" + "Facilities: " + roomFacilities);
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }

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
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/roomView.fxml")); //should be room page but I don't this there is one?
        currentStage.setScene(new Scene(root, 700, 600));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTimeslots();
        loadRoom();

    }
}
