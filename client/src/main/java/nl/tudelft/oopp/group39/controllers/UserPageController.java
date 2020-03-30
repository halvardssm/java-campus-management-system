package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.BookingDto;
import nl.tudelft.oopp.group39.views.UsersDisplay;


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

    /**
     * Updates all the information on the user page.
     */
    public void showBookings() {
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
            BookingDto[] bookingList = mapper.readValue(bookingString, BookingDto[].class);

            for (BookingDto booking : bookingList) {
                newBooking = FXMLLoader.load(getClass().getResource("/bookingCell.fxml"));
                Integer roomName2 = Math.toIntExact(booking.getRoom());
                String roomName = ServerCommunication.getRoom(roomName2).getName();
                Label name = (Label) newBooking.lookup("#rName");
                name.setText(roomName);

                String bookingID = Long.toString(booking.getId());
                Label bookID = (Label) newBooking.lookup("#bookingID");
                bookID.setText(bookingID);

                String roomId = Long.toString(booking.getRoom());
                Label bookedRoom = (Label) newBooking.lookup("#roomID");
                bookedRoom.setText(roomId);

                String startTime = booking.getStartTime();
                Label date = (Label) newBooking.lookup("#rDate");
                date.setText("Starting Time: " + startTime);

                String duration = differenceBetweenTwoTimes(
                        LocalTime.parse(booking.getStartTime()),
                        LocalTime.parse(booking.getEndTime()));
                Label bookingDuration = (Label) newBooking.lookup("#rDuration");
                bookingDuration.setText("Duration: " + duration);

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
    private String differenceBetweenTwoTimes(LocalTime l1, LocalTime l2) {
        LocalTime result = l2.minusHours(l1.getHour());
        result = result.minusMinutes(l1.getMinute());
        result = result.minusSeconds(l1.getSecond());
        String duration = result.toString();

        //If the seconds are 0, it omits it. So here we put the 00's anyway.
        if (result.getSecond() == 0) {
            duration = duration + ":00";
        }
        return duration;
    }

    /**
     * Deletes the booking.
     */
    public void deleteBooking() {
        ServerCommunication.removeBooking(bookingID.getText());
    }

    /**
     * Shows the edit fields for editing the booking.
     */
    public void editBooking() throws IOException {
        editStartingTime.setOpacity(1);
        editDuration.setOpacity(1);
        editDate.setOpacity(1);
        doneButton.setOpacity(1);
    }

    /**
     * Edits the booking.
     */
    public void finishEditBooking() {
        editStartingTime.setOpacity(0);
        editDuration.setOpacity(0);
        editDate.setOpacity(0);
        doneButton.setOpacity(0);

        try {
            LocalTime startTime = LocalTime.parse(editStartingTime.getText());
            LocalTime durationTime = LocalTime.parse(editDuration.getText());

            if (startTime.getMinute() != 00 || startTime.getSecond() != 00
                || durationTime.getMinute() != 00 || durationTime.getSecond() != 00) {
                createAlert("You can only book rooms starting at the hour");
                return;
            }
            if (startTime.getHour() < 9) {
                createAlert("You can only book a room after 9");
                return;
            }
            if (durationTime.getHour() > 4) {
                createAlert("You can't book a room longer than 4 hours");
                return;
            }
            if (startTime.getHour() == 17 && durationTime.getHour() == 4
                || startTime.getHour() == 18 && durationTime.getHour() == 3
                || startTime.getHour() == 19 && durationTime.getHour() == 2
                || startTime.getHour() == 20) {
                createAlert("You can only book a room until 8");
                return;
            }

            //Looking whether the user already has a booking at the same time and date
            String bookingString = ServerCommunication.getAllBookings();
            ArrayNode body = (ArrayNode) mapper.readTree(bookingString).get("body");
            bookingString = mapper.writeValueAsString(body);
            BookingDto[] bookingList = mapper.readValue(bookingString, BookingDto[].class);

            for (BookingDto booking : bookingList) {
                if (booking.getStartTime().equals(editStartingTime.getText())
                        && editDate.getValue().toString().equals(booking.getDate())) {
                    createAlert("You can't have 2 bookings at the same time!");
                    return;
                }
            }

        } catch (DateTimeParseException | NullPointerException e) {
            System.out.println("Invalid Time String");
        } catch (IOException e) {
            System.out.println("Error: Wrong IO");
        }

        LocalTime t1 = LocalTime.parse(editStartingTime.getText());
        LocalTime t2 = LocalTime.parse(editDuration.getText());
        int endTimeHour = t1.getHour() + t2.getHour();

        ServerCommunication.addBooking(editDate.getValue().toString(),
                                      t1.toString() + ":00",
                                      endTimeHour + ":00:00",
                                       user.getUsername(),
                                       roomID.getText());
        ServerCommunication.removeBooking(bookingID.getText());
    }

    /**
     * Views the room.
     */
    public void viewRoom() throws IOException {
        goToRoomsScene();
    }

    /**
     * Returns the user back to the building page when the back button is clicked.
     */
    @FXML
    private void backToRoom() throws IOException {
        goToBuildingScene();
    }
}
