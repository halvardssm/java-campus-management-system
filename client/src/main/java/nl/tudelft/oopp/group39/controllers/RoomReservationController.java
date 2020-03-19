package nl.tudelft.oopp.group39.controllers;

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
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;


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
    private void reserveRoom() {
        LocalDate bookingDate = date.getValue();
        LocalTime bookingStart = (LocalTime) fromTime.getValue();
        LocalTime bookingEnd = (LocalTime) toTime.getValue();

        if (!checkEmpty(bookingDate, bookingStart, bookingEnd)) {
            showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
        }
        System.out.println(bookingDate);
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
     * Initiates the timeslots for the ComboBoxes to load
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
     * Loads the timeslots into the ComboBoxes
     */
    private void loadTimeslots() {
        fromTime.getItems().addAll(initiateTimeslots());
        toTime.getItems().addAll(initiateTimeslots());
    }

    /**
     * Returns the user back to the room page when clicked on.
     */
    public void backToRoom() {

    }

    /**
     * Switches to login page when the Login button is clicked?
     *
     * @throws IOException
     */
    @FXML
    private void switchLogin() throws IOException {
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        currentStage.setScene(new Scene(root, 700, 600));
    }

    /**
     * Switches to the homepage when the *SomeName* button is clicked?
     *
     * @throws IOException
     */
    @FXML
    private void switchMain() throws IOException {
        Stage currentStage = (Stage) homeButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/mainScene.fxml"));
        currentStage.setScene(new Scene(root, 700, 600));
    }

    /**
     * Switches to room page when the back button is clicked?
     *
     * @throws IOException
     */
    @FXML
    private void switchRoom() throws IOException {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/roomScene.fxml"));
        currentStage.setScene(new Scene(root, 700, 600));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTimeslots();
    }
}
