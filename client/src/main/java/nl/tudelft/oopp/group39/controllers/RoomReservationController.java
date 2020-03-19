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
    private Button timeslotButton0;
    @FXML
    private Button timeslotButton1;
    @FXML
    private Button timeslotButton2;
    @FXML
    private Button timeslotButton3;
    @FXML
    private Button timeslotButton4;
    @FXML
    private Button timeslotButton5;
    @FXML
    private Button timeslotButton6;
    @FXML
    private Button timeslotButton7;
    @FXML
    private Button timeslotButton8;
    @FXML
    private Button timeslotButton9;
    @FXML
    private Button timeslotButton10;
    @FXML
    private Button timeslotButton11;
    @FXML
    private Button timeslotButton12;
    @FXML
    private Button timeslotButton13;
    @FXML
    private Button timeslotButton14;
    @FXML
    private Button timeslotButton15;
    @FXML
    private Button timeslotButton16;
    @FXML
    private Button timeslotButton17;
    @FXML
    private Button timeslotButton18;
    @FXML
    private Button timeslotButton19;
    @FXML
    private Button timeslotButton20;
    @FXML
    private Button timeslotButton21;
    @FXML
    private Button timeslotButton22;
    @FXML
    private Button timeslotButton23;

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
        if (!checkEmpty(localDate, timeslotButton0, timeslotButton1, timeslotButton2, timeslotButton3,
            timeslotButton4, timeslotButton5, timeslotButton6, timeslotButton7, timeslotButton8,
            timeslotButton9, timeslotButton10, timeslotButton11, timeslotButton12, timeslotButton13,
            timeslotButton14, timeslotButton15, timeslotButton16, timeslotButton17, timeslotButton18,
            timeslotButton19, timeslotButton20, timeslotButton21, timeslotButton22, timeslotButton23)) {
            showAlert(Alert.AlertType.INFORMATION, "", "Reservation successful.");
        }
        System.out.println(localDate);
    }

    public boolean checkEmpty(LocalDate localDate, Button button0, Button button1, Button button2, Button button3,
                              Button button4, Button button5, Button button6, Button button7, Button button8, Button button9,
                              Button button10, Button button11, Button button12, Button button13, Button button14, Button button15,
                              Button button16, Button button17, Button button18, Button button19, Button button20, Button button21,
                              Button button22, Button button23) {
        if (localDate == null || button0 == null && button1 == null && button2 == null && button3 == null
            && button4 == null && button5 == null && button6 == null && button7 == null && button8 == null && button9 == null
            && button10 == null && button11 == null && button12 == null && button13 == null && button14 == null && button15 == null
            && button16 == null && button17 == null && button18 == null && button19 == null && button20 == null && button21 == null
            && button22 == null && button23 == null) {
            showAlert(Alert.AlertType.ERROR, "", "Please fill in all the fields.");
            return false;
        } else {
            return true;
        }
    }

    public void clickTimeslot() {

    }

    public void backToRoom() {

    }
}
