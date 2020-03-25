package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
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

    public void showBookings() {
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
                String startTime = booking.getStartTime();
                String duration = "TBI";

                newBooking.setOnMouseClicked(e -> {
                    try {
                        goToRoomsScene();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

//                Label name = (Label) newBooking.lookup("#rName");
//                name.setText(roomName);

                Label date = (Label) newBooking.lookup("#rDate");
                date.setText(startTime);

                Label dur = (Label) newBooking.lookup("#rDuration");
                dur.setText(duration);

                flowPane.getChildren().add(newBooking);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }
}
