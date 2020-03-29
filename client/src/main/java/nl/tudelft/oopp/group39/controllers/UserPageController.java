package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import java.io.IOException;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.models.Booking;
import nl.tudelft.oopp.group39.models.BookingDTO;
import nl.tudelft.oopp.group39.models.Room;

public class UserPageController extends MainSceneController {
    @FXML
    private FlowPane flowPane; //The User Page screen

    @FXML
    private AnchorPane newBooking; //The whole card

    @FXML
    private Label accountName;

    @FXML
    private Label accountRole;

    @FXML
    private Label accountEmail;

    @FXML
    private Label bookingID;

    @FXML
    private Label roomID;

    @FXML
    private TextField editStartingTime;

    @FXML
    private TextField editDuration;

    @FXML
    private DatePicker editDate;

    @FXML
    private Button doneButton;

    public void showBookings() {
        //TODO replace these to other method as soon as I know how to load certain methods immediately when scene is loaded
        accountName.setText(MainSceneController.user.getUsername());
        accountRole.setText(MainSceneController.user.getRole());
        accountEmail.setText(MainSceneController.user.getEmail());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        flowPane.getChildren().clear();
        try {
            String bookingString = ServerCommunication.getAllBookings();
            System.out.println(bookingString); //This is not necessary, just for checking
            ArrayNode body = (ArrayNode) mapper.readTree(bookingString).get("body");
            bookingString = mapper.writeValueAsString(body);
            BookingDTO[] bookingList = mapper.readValue(bookingString, BookingDTO[].class);

            for(BookingDTO booking : bookingList) {
                newBooking = FXMLLoader.load(getClass().getResource("/bookingCell.fxml"));
//                Integer roomName2 = Math.toIntExact(booking.getRoom());
//                String roomName = ServerCommunication.getRoom(roomName2).getName();
                String bookingID = Long.toString(booking.getId());
                String roomId = Long.toString(booking.getRoom());
                String startTime = booking.getStartTime();
                String duration = DifferenceBetweenTwoTimes(
                        LocalTime.parse(booking.getStartTime()),
                        LocalTime.parse(booking.getEndTime()));

                Label name = (Label) newBooking.lookup("#rName");
                name.setText("Booking ID #" + booking.getId());

                Label date = (Label) newBooking.lookup("#rDate");
                date.setText("Starting Time: " + startTime);

                Label bookingDuration = (Label) newBooking.lookup("#rDuration");
                bookingDuration.setText("Duration: " + duration);

                Label bID = (Label) newBooking.lookup("#bookingID");
                bID.setText(bookingID);

                Label bookedRoom = (Label) newBooking.lookup("#roomID");
                bookedRoom.setText(roomId);

                flowPane.getChildren().add(newBooking);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Calculates the difference between two times in HH:MM:SS format.
     *
     * @param l1 the starting time
     * @param l2 the end time
     * @return a string form of the difference
     */
    private String DifferenceBetweenTwoTimes(LocalTime l1, LocalTime l2) {
        LocalTime result = l2.minusHours(l1.getHour());
        result = result.minusMinutes(l1.getMinute());
        result = result.minusSeconds(l1.getSecond());
        String duration = result.toString();

        //If the seconds are 0, it omits it. So here we put the 00's anyway
        if(result.getSecond() == 0) { duration = duration + ":00"; }
        return duration;
    }

    public void deleteBooking() {
        ServerCommunication.removeBooking(bookingID.getText());
    }

    public void editBooking() throws IOException {
        editStartingTime.setOpacity(1);
        editDuration.setOpacity(1);
        editDate.setOpacity(1);
        doneButton.setOpacity(1);
    }

    public void finishEditBooking() {
        editStartingTime.setOpacity(0);
        editDuration.setOpacity(0);
        editDate.setOpacity(0);
        doneButton.setOpacity(0);

        LocalTime t1 = LocalTime.parse(editStartingTime.getText());
        LocalTime t2 = LocalTime.parse(editDuration.getText());
        int endTimeHour = t1.getHour() + t2.getHour();

        ServerCommunication.addBooking(editDate.getValue().toString(), t1.toString() + ":00", endTimeHour + ":00:00", user.getUsername(), roomID.getText());
        ServerCommunication.removeBooking(bookingID.getText());
        //showBookings();
    }

    public void viewRoom() throws IOException {
        goToRoomsScene();
    }

    /**
     * Returns the user back to the room page when the back button is clicked.
     *
     * @throws IOException when there is an io exception
     */
    @FXML
    private void backToRoom() throws IOException {
        goToBuildingScene();
    }
}
