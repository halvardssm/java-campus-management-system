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
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.event.model.Event;
import nl.tudelft.oopp.group39.reservable.controller.FoodAndBikeSceneController;
import nl.tudelft.oopp.group39.room.controller.RoomReservationController;
import nl.tudelft.oopp.group39.room.controller.RoomSceneController;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.views.UsersDisplay;
import nl.tudelft.oopp.group39.user.controller.SignupController;
import nl.tudelft.oopp.group39.user.model.User;

public abstract class AbstractSceneController {

    protected ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static boolean loggedIn = false;
    public static String jwt;
    public static User user;

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
     * @param title the title of the alert
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
     * Switches view to the building scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToBuildingScene() throws IOException {
        goTo("/building/buildingListView.fxml");
    }

    /**
     * Switches view to the userpage scene.
     *
     * @throws IOException if the scene wasn't found
     */
    public void goToUserPageScene() throws IOException {
        UsersDisplay.sceneHandler("/userPage.fxml");
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
     * Logs the user out.
     *
     * @throws IOException if view wasn't found.
     */
    public void logout() throws IOException {
        loggedIn = false;
        jwt = null;
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
     * Retrieves the events in a set.
     *
     * @return Set representation of all the events
     * @throws JsonProcessingException when there is a processing exception
     */
    public Set<Event> getEventList() throws JsonProcessingException {
        String json = ServerCommunication.get(ServerCommunication.event);
        ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
        json = mapper.writeValueAsString(body);

        return new HashSet<>(Arrays.asList(mapper.readValue(json, Event[].class)));
    }

    /**
     * Checks if the given date is not during an event.
     *
     * @param date the selected date
     * @return boolean true if date is not an event, false if date is on an event
     */
    public boolean checkDate(String date) throws JsonProcessingException {
        LocalDate check = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        for (Event event : getEventList()) {
            LocalDate start = event.getStartDate();
            LocalDate end = event.getEndDate() == null ? start : event.getEndDate();
            int checkInt = check.compareTo(start) + check.compareTo(end);
            if (-1 >= checkInt & checkInt <= 1) {
                createAlert("There is an event on this date");
                return false;
            }
        }
        return true;
    }
}
