package nl.tudelft.oopp.group39.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
    private ComboBox fromTime;
    @FXML
    private ComboBox toTime;

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

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

    public boolean checkEmpty(LocalDate date, LocalTime start, LocalTime end) {
        if (date == null || start == null || end == null) {
            showAlert(Alert.AlertType.ERROR, "", "Please fill in all the fields.");
            return false;
        } else {
            return true;
        }
    }

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

    private void loadTimeslots() {
        fromTime.getItems().addAll(initiateTimeslots());
        toTime.getItems().addAll(initiateTimeslots());
    }

    public void backToRoom() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTimeslots();
    }
}
