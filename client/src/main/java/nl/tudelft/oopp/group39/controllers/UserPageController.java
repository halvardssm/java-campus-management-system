package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public void editBooking() {
        ServerCommunication.updateBooking("2020-04-03", "17:00:00", "22:30:00", "admin", "3", "1");
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
