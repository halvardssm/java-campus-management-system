package nl.tudelft.oopp.group39.controllers;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javax.swing.text.html.ImageView;


public class RoomReservationController extends MainSceneController {
    @FXML
    private DatePicker date;
    @FXML
    private ImageView image;
    @FXML
    private Button reserveButton;
    @FXML
    private Button backButton;

    public static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void reserveRoom() {
        LocalDate localDate = date.getValue();
        if (!checkEmpty(localDate)) {
            showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
        }
        System.out.println(localDate);
    }

    public boolean checkEmpty(LocalDate localDate) {
        if (localDate == null ||) {
            showAlert(Alert.AlertType.ERROR, "", "Please fill in all the fields.");
            return true;
        } else {
            return false;
        }
    }
}
