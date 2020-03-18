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

    @FXML
    private void reserveRoom() {
        LocalDate localDate = date.getValue();
        if (!checkEmpty(localDate)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setContentText("Reservation successful");
            alert.showAndWait();
        }
        System.out.println(localDate);
    }

    public boolean checkEmpty(LocalDate localDate) {
        if (localDate == null |) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields");
            alert.showAndWait();
            return true;
        } else {
            return false;
        }
    }


}
