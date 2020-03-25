package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.converter.LocalTimeStringConverter;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.oopp.group39.models.Booking;
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
            Booking[] bookingList = mapper.readValue(bookingString, Booking[].class);

            for(Booking booking : bookingList) {
                newBooking = FXMLLoader.load(getClass().getResource("/bookingCell.fxml"));
                //String roomName = booking.getRoom().getName(); //A problem
                String bookingID = Integer.toString(booking.getId());
                String startTime = booking.getStartTime();
                String duration = DifferenceBetweenTwoTimes(
                        LocalTime.parse(booking.getStartTime()),
                        LocalTime.parse(booking.getEndTime()));

//                Label name = (Label) newBooking.lookup("#rName");
//                name.setText(roomName);

                Label date = (Label) newBooking.lookup("#rDate");
                date.setText("Starting Time: " + startTime);

                Label bookingDuration = (Label) newBooking.lookup("#rDuration");
                bookingDuration.setText("Duration: " + duration);

                Label bID = (Label) newBooking.lookup("#bookingID");
                bID.setText(bookingID);

                flowPane.getChildren().add(newBooking);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Calculates the difference between two times.
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
}
