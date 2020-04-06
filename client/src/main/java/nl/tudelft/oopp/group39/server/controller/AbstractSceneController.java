package nl.tudelft.oopp.group39.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.reservable.controller.FoodAndBikeSceneController;
import nl.tudelft.oopp.group39.room.controller.RoomReservationController;
import nl.tudelft.oopp.group39.room.controller.RoomSceneController;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.views.UsersDisplay;
import nl.tudelft.oopp.group39.user.controller.CalendarController;
import nl.tudelft.oopp.group39.user.controller.SignupController;
import nl.tudelft.oopp.group39.user.controller.UserPageController;
import nl.tudelft.oopp.group39.user.model.User;
import nl.tudelft.oopp.group39.views.AdminPanel;

public abstract class AbstractSceneController {
    protected ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static boolean loggedIn = false;
    public static String jwt = "";
    public static User user;
    public DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @FXML
    public VBox sidebar;
    @FXML
    public MenuButton myaccount;
    @FXML
    protected Button userButton;
    @FXML
    protected HBox topBar;
    @FXML
    protected VBox userBox;
    @FXML
    protected BorderPane window;

    /**
     * Creates a simple alert that has the content specified.
     *
     * @param content the content to be displayed
     */
    public void createAlert(String content) {
        createAlert(null, content);
    }

    /**
     * Creates an alert that contains a title.
     *
     * @param title   the title of the alert
     * @param content content to be displayed
     */
    public void createAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Method to go to a specific screen.
     *
     * @param location location of the xml file
     * @return the controller for said xml file(for additional purposes)
     * @throws IOException if said xml file does not exist.
     */
    public AbstractSceneController goTo(String location) throws IOException {
        AbstractSceneController controller = UsersDisplay.sceneControllerHandler(location);
        controller.changeUserBox();
        return controller;
    }

    /**
     * Method to go to a specific screen in admin panel.
     *
     * @param location location of the xml file
     * @return the controller for said xml file(for additional purposes)
     * @throws IOException if said xml file does not exist.
     *
     */
    public AbstractSceneController goToAdmin(String location) throws IOException {
        AbstractSceneController controller = AdminPanel.sceneControllerHandler(location);
        controller.changeUserBox();
        return controller;
    }

    /**
    * Switches view to the admin building list scene.
    *
    * @throws IOException if the scene wasn't found
    */

    public void goToAdminFoodAndBikeScene() throws IOException {
    //       goToAdmin("/admin/FoodAndBike/BuildingList.fxml");
    }


    /**
     * Switches view to the admin building list scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToAdminBuildingScene() throws IOException {
        goToAdmin("/admin/building/BuildingList.fxml");
    }

    /**
     * Switches view to the admin room list scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToAdminRoomScene() throws IOException {
        goToAdmin("/admin/room/RoomList.fxml");
    }

    /**
     * Switches view to the admin bookings scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToAdminBookingsScene() throws IOException {
        goToAdmin("/admin/booking/BookingList.fxml");
    }

    /**
     * Switches view to the admin user scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToAdminUserScene() throws IOException {
        goToAdmin("/admin/user/UserList.fxml");
    }

    /**
     * Switches view to the admin Event scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToAdminEventScene() throws IOException {
        goToAdmin("/admin/event/EventList.fxml");
    }

    /**
     * Switches view to the building scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToBuildingScene() throws IOException {
        goTo("/building/buildingListView.fxml");
    }

    /**
     * Switches to the user page scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToUserPageScene() throws IOException {
        UserPageController controller =
            (UserPageController) UsersDisplay.sceneControllerHandler("/user/userPage.fxml");
        controller.changeUserBox();
        controller.showBookings();
    }

    /**
     * Switches view to the login scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToLoginScene() throws IOException {
        goTo("/user/login.fxml");
    }

    /**
     * Switches view to the Signup scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToSignupScene() throws IOException {
        SignupController controller =
            (SignupController) UsersDisplay.sceneControllerHandler("/user/signup.fxml");
        controller.changeUserBox();
    }

    /**
     * Switches view to the room scene, and filters the rooms that are in the building specified.
     *
     * @param building the building that the rooms will be filtered with
     * @throws IOException if the scene wasn't found
     */
    public void goToRoomsScene(Building building) throws IOException {
        RoomSceneController controller = (RoomSceneController) goTo("/room/roomView.fxml");
        controller.setup(building);
    }

    /**
     * Switches view to the rooms scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToRoomsScene() throws IOException {
        RoomSceneController controller = (RoomSceneController) goTo("/room/roomView.fxml");
        controller.setup();
    }

    /**
     * Goes to reservation scene.
     *
     * @param room     chosen room
     * @param building building of chosen room
     * @throws IOException throws an IOException
     */
    public void goToReservationScene(Room room, Building building) throws IOException {
        RoomReservationController controller =
            (RoomReservationController) goTo("/room/roomReservation.fxml");
        controller.setup(room, building);
    }

    /**
     * Switches view to the bike rental scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToBikeRentalScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) goTo("/reservable/bikeAndFoodView.fxml");
        controller.setup("bike");
    }

    /**
     * Switches view to the food order scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToFoodOrderScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) goTo("/reservable/bikeAndFoodView.fxml");
        controller.setup("food");
    }

    /**
     * Switches view to the calendar scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToCalendarScene() throws IOException {
        CalendarController controller =
            (CalendarController) goTo("/user/calendarView.fxml");
        controller.createCalendar();
    }

    /**
     * Logs the user out.
     *
     * @throws IOException if view wasn't found.
     */
    public void logout() throws IOException {
        loggedIn = false;
        jwt = "";
        goToBuildingScene();
    }

    /**
     * Changes the login button when logged in.
     *
     * @throws IOException if view wasn't found.
     */
    public void changeUserBox() throws IOException {
        userBox.getChildren().clear();
        if (loggedIn) {
            myaccount = FXMLLoader.load(getClass().getResource("/menuButton.fxml"));

            if (!user.getRole().equals("ADMIN")) {
                myaccount.getItems().remove(3);
            }

            myaccount.setText(user.getUsername());
            userBox.getChildren().add(myaccount);
        } else {
            userButton.setText("Login");
            userButton.setOnAction(e -> {
                try {
                    goToLoginScene();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            userBox.getChildren().add(userButton);
        }
    }

    /**
     * Toggles the sidebar.
     *
     * @throws IOException if view wasn't found.
     */
    public void toggleSidebar() throws IOException {
        if (window.getLeft() == null) {
            sidebar = FXMLLoader.load(
                    getClass().getResource("/sidebar.fxml")
            );
            window.setLeft(sidebar);
            return;
        }
        window.setLeft(null);
    }

    /**
     * Toggles the admin sidebar.
     *
     * @throws IOException if view wasn't found.
     */
    public void toggleAdminSidebar() throws IOException {
        if (window.getLeft() == null) {
            sidebar = FXMLLoader.load(
                    getClass().getResource("/adminSidebar.fxml")
            );
            window.setLeft(sidebar);
            return;
        }
        window.setLeft(null);
    }

    /**
     * Sets the capacity Slider for filtering in rooms and buildings.
     *
     * @param capacityPicker the Slider with which you can select the capacity
     * @param max            the max capacity that can be selected
     */
    public void setCapacityPicker(Slider capacityPicker, int max) {
        capacityPicker.setMin(0);
        capacityPicker.setMax(max);
        capacityPicker.setValue(0);
        capacityPicker.setValue(0);
        capacityPicker.setMajorTickUnit(1);
        capacityPicker.setMinorTickCount(0);
        capacityPicker.setSnapToTicks(true);
    }

    /**
     * Checks if the given date is not during an event.
     *
     * @param date the selected date
     * @return boolean true if date is not an event, false if date is on an event
     */
    public boolean checkDate(String date, Long room) throws JsonProcessingException {
        LocalDate check = LocalDate.parse(date, dateFormatter);
        Set<Event> events = room == null ? getEvents("isGlobal=true") : getEvents("rooms=" + room);
        if (events.size() != 0) {
            for (Event event : events) {
                LocalDate start = event.getStartTime().toLocalDate();
                LocalDate end =
                    event.getEndsAt() == null ? start : event.getEndTime().toLocalDate();
                if (check.compareTo(start) >= 0 && check.compareTo(end) <= 0) {
                    createAlert("There is an event on this date");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retrieves an array of bookings with filters.
     *
     * @param filters String of filters
     * @return array of bookings
     * @throws JsonProcessingException when there is a processing exception
     */
    public Booking[] getBookings(String filters) throws JsonProcessingException {
        String userBookings = ServerCommunication.getBookings(filters);
        ArrayNode bodyBookings = (ArrayNode) mapper.readTree(userBookings).get("body");
        String userBookingString = mapper.writeValueAsString(bodyBookings);
        return mapper.readValue(userBookingString, Booking[].class);
    }

    /**
     * Retrieves set of events with given filters.
     *
     * @param filters String of filters.
     * @return Set of filtered events
     * @throws JsonProcessingException when there is a processing exception
     */
    public Set<Event> getEvents(String filters) throws JsonProcessingException {
        Event[] events = ServerCommunication.getEvents(filters);
        return new HashSet<>(Arrays.asList(events));
    }

    public void goToAdminScene() throws IOException {
        UsersDisplay.sceneHandler("/admin/AdminPanel.fxml");
    }
}
