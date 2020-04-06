package nl.tudelft.oopp.group39.user.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;
import nl.tudelft.oopp.group39.server.views.MainDisplay;
import org.apache.commons.codec.DecoderException;

public class UserPageController extends AbstractSceneController {
    @FXML
    private FlowPane flowPane;
    @FXML
    private GridPane newBooking;
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
    private Label bookingDate;
    @FXML
    private TextField editStartingTime;
    @FXML
    private TextField editDuration;
    @FXML
    private DatePicker editDate;
    @FXML
    private Button doneButton;
    @FXML
    private ImageView userImage;

    /**
     * Updates all the information on the user page.
     */
    public void showBookings() throws IOException, DecoderException {
        showUserInfo();
        flowPane.getChildren().clear();
        try {
            String bookingString = ServerCommunication.getBookings("user=" + user.getUsername());
            ArrayNode body = (ArrayNode) mapper.readTree(bookingString).get("body");
            bookingString = mapper.writeValueAsString(body);
            Booking[] bookingList = mapper.readValue(bookingString, Booking[].class);

            for (Booking booking : bookingList) {
                newBooking = FXMLLoader.load(getClass().getResource("/user/bookingCell.fxml"));

                String bookingD = booking.getDate();
                Label theBookingDate = (Label) newBooking.lookup("#bookingDate");
                theBookingDate.setText("Date: " + bookingD);

                Long roomName2 = booking.getRoom();
                String roomName = ServerCommunication.getRoom(roomName2).getName();
                Label name = (Label) newBooking.lookup("#rName");
                name.setText(roomName);

                String bookingID = Long.toString(booking.getId());
                Label bookID = (Label) newBooking.lookup("#bookingID");
                bookID.setText(bookingID);

                Long roomId = booking.getRoom();
                Label bookedRoom = (Label) newBooking.lookup("#roomID");
                bookedRoom.setText(roomId.toString());

                String startTime = booking.getStartTime();
                Label date = (Label) newBooking.lookup("#rDate");
                date.setText("Start time: " + startTime);

                String endTime = booking.getEndTime();
                Label bookingDuration = (Label) newBooking.lookup("#rDuration");
                bookingDuration.setText("End time: " + endTime);

                flowPane.getChildren().add(newBooking);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Shows the user information.
     *
     * @throws IOException      when there is an IO exception
     * @throws DecoderException when there is a decoder exception
     */
    public void showUserInfo() throws IOException, DecoderException {
        accountName.setText(user.getUsername());
        accountRole.setText(user.getRole());
        accountEmail.setText(user.getEmail());
        if (user.getImage() == null) {
            Image standardUser =
                new Image(new FileInputStream("client/src/main/resources/icons/user-icon.png"));
            userImage.setImage(standardUser);
        } else {
            byte[] image = decodeUsingApacheCommons(user.getImage());
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            Image userPhoto = new Image(in);
            userImage.setImage(userPhoto);
        }
    }

    /**
     * Changes the user image.
     *
     * @throws IOException      when there is an IO exception
     * @throws DecoderException when there is a decoder exception
     */
    public void changeUserImage() throws IOException, DecoderException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose picture");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File file = fileChooser.showOpenDialog(MainDisplay.window);
        if (file != null) {
            Image newImage = new Image(file.toURI().toString());
            userImage.setImage(newImage);
            BufferedImage bufferedImage = ImageIO.read(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            System.out.println(file.getName());
            String[] fileName = file.getName().split("\\.");
            String format = fileName[fileName.length - 1];
            ImageIO.write(bufferedImage, format, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            user.setImageString(data);
            System.out.println(ServerCommunication.updateUser(user));
        }
    }

    /**
     * Calculates the difference between two times in HH:MM:SS format.
     *
     * @param l1 the starting time
     * @param l2 the end time
     * @return a string form of the difference
     */
    private int differenceBetweenTwoTimes(LocalTime l1, LocalTime l2) {
        return l1.getHour() - l2.getHour();
    }

    /**
     * Deletes the booking after a 'warning' has been fired.
     */
    public void deleteBooking() throws IOException, DecoderException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure you want to delete the booking?");
        confirmation.setTitle("Delete");
        confirmation.setHeaderText(null);

        Optional<ButtonType> popUpWindow = confirmation.showAndWait();
        if (popUpWindow.isPresent() && popUpWindow.get() == ButtonType.OK) {
            ServerCommunication.removeBooking(bookingID.getText());
            goToUserPageScene();
        }
    }

    /**
     * Shows the edit fields for editing the booking.
     */
    public void editBooking() {
        editStartingTime.setOpacity(1);
        editDuration.setOpacity(1);
        editDate.setOpacity(1);
        doneButton.setOpacity(1);
        bookingDate.setOpacity(0);
    }

    /**
     * Edits the booking.
     */
    public void finishEditBooking() {
        editStartingTime.setOpacity(0);
        editDuration.setOpacity(0);
        editDate.setOpacity(0);
        doneButton.setOpacity(0);
        bookingDate.setOpacity(1);

        try {
            LocalTime startTime = LocalTime.parse(editStartingTime.getText());
            LocalTime endTime = LocalTime.parse(editDuration.getText());

            if (startTime.getMinute() != 0 || startTime.getSecond() != 0
                || endTime.getMinute() != 0 || endTime.getSecond() != 0) {
                createAlert("You can only book rooms starting at the hour");
                return;
            }
            if (differenceBetweenTwoTimes(endTime, startTime) > 4) {
                createAlert("You can't book a room longer than 4 hours");
                return;
            }

            //Looking whether the user already has a booking at the same time and date
            //And retrieving the opening and closing time of the building
            String bookingString = ServerCommunication.getAllBookings();
            ArrayNode body = (ArrayNode) mapper.readTree(bookingString).get("body");
            bookingString = mapper.writeValueAsString(body);
            Booking[] bookingList = mapper.readValue(bookingString, Booking[].class);

            String buildingOpenTimeString = "";
            String buildingCloseTimeString = "";
            for (Booking booking : bookingList) {
                if (booking.getStartTime().equals(editStartingTime.getText())
                        && editDate.getValue().toString().equals(booking.getDate())) {
                    createAlert("You can't have 2 bookings at the same time!");
                    return;
                }

                buildingOpenTimeString = ServerCommunication
                    .getTheBuilding(ServerCommunication
                        .getRoom(booking.getRoom())
                        .getBuilding())
                    .getOpen().toString();
                buildingCloseTimeString = ServerCommunication
                    .getTheBuilding(ServerCommunication
                        .getRoom(booking.getRoom())
                        .getBuilding())
                    .getClosed().toString();
            }

            LocalTime buildingOpenTime = LocalTime.parse(buildingOpenTimeString);
            LocalTime buildingCloseTime = LocalTime.parse(buildingCloseTimeString);

            if (startTime.getHour() < buildingOpenTime.getHour()) {
                createAlert("You can't book a room if the building is closed");
                return;
            }
            if (endTime.getHour() > buildingCloseTime.getHour()) {
                createAlert("Your booking time exceeds the closing time of the building");
                return;
            }

            ServerCommunication.addBooking(editDate.getValue().toString(),
                startTime.toString() + ":00",
                endTime.toString() + ":00",
                user.getUsername(),
                roomID.getText());
            ServerCommunication.removeBooking(bookingID.getText());
            goToUserPageScene();

        } catch (DateTimeParseException | NullPointerException e) {
            createAlert("Invalid Time String");
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * Views the room you have booked.
     *
     * @throws IOException if the room wasn't found
     */
    public void viewRoom() throws IOException {
        Room r1 = ServerCommunication.getRoom(Long.parseLong(roomID.getText()));
        super.goToReservationScene(r1, r1.getBuildingObject());
    }

    /**
     * Returns the user back to the building page when the back button is clicked.
     *
     * @throws IOException if the scene wasn't found
     */
    @FXML
    private void backToRoom() throws IOException {
        goToBuildingScene();
    }
}
