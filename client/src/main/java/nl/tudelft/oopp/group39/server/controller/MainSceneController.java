package nl.tudelft.oopp.group39.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.building.controller.BuildingSceneController;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.reservable.controller.FoodAndBikeSceneController;
import nl.tudelft.oopp.group39.room.controller.RoomReservationController;
import nl.tudelft.oopp.group39.room.controller.RoomSceneController;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.views.UsersDisplay;
import nl.tudelft.oopp.group39.user.controller.LoginController;
import nl.tudelft.oopp.group39.user.controller.SignupController;
import nl.tudelft.oopp.group39.user.model.User;

public class MainSceneController {

    protected ObjectMapper mapper = new ObjectMapper();

    public static boolean loggedIn = false;
    public static String jwt;
    public static boolean sidebarShown = false;
    public static User user;

    @FXML
    public VBox sidebar;

    @FXML
    public MenuButton myaccount;

    @FXML
    protected Button topbtn;

    @FXML
    protected HBox topbar;

    @FXML
    protected BorderPane window;

    @FXML
    private MenuItem admin;

    /**
     * Doc. TODO Sven
     */
    public void createAlert(String content) {
        createAlert(null, content);
    }

    /**
     * Doc. TODO Sven
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
     * Doc. TODO Sven
     */
    public void goToMainScene() throws IOException {
        UsersDisplay.sceneHandler("/mainScene.fxml");
        changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToBuildingScene() throws IOException {
        BuildingSceneController controller =
            (BuildingSceneController) UsersDisplay
                .sceneControllerHandler("/building/buildingListView.fxml");
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToAddBuilding() throws IOException {
        UsersDisplay.sceneHandler("/building/buildingModifyScene.fxml");
        changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToLoginScene() throws IOException {
        LoginController controller =
            (LoginController) UsersDisplay.sceneControllerHandler("/user/login.fxml");
        controller.changeTopBtn();
    }


    /**
     * Doc. TODO Sven
     */
    public void goToSignupScene() throws IOException {
        SignupController controller =
            (SignupController) UsersDisplay.sceneControllerHandler("/user/signup.fxml");
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToRoomsScene(Building building) throws IOException {
        RoomSceneController controller =
            (RoomSceneController) UsersDisplay.sceneControllerHandler("/room/roomView.fxml");
        controller.setup(building);
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToRoomsScene() throws IOException {
        RoomSceneController controller =
            (RoomSceneController) UsersDisplay.sceneControllerHandler("/room/roomView.fxml");
        controller.setup();
        controller.changeTopBtn();
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
            (RoomReservationController) UsersDisplay.sceneControllerHandler(
                "/room/roomReservation.fxml");
        controller.setup(room, building);
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToBikeRentalScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) UsersDisplay
                .sceneControllerHandler("/reservable/bikeAndFoodView.fxml");
        controller.changeTopBtn();
        controller.setup("bike");
    }

    /**
     * Doc. TODO Sven
     */
    public void goToFoodOrderScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) UsersDisplay
                .sceneControllerHandler("/reservable/bikeAndFoodView.fxml");
        controller.changeTopBtn();
        controller.setup("food");
    }

    /**
     * Logs the user out.
     */
    public void logout() throws IOException {
        loggedIn = false;
        jwt = null;
        goToBuildingScene();
    }

    /**
     * Doc. TODO Sven
     */
    public void getFacilitiesButton() {
        createAlert(null, ServerCommunication.get(ServerCommunication.facility));
    }

    /**
     * Doc. TODO Sven
     */
    public void getUsersButton() {
        createAlert(ServerCommunication.get(ServerCommunication.user));
    }

    /**
     * Changes the login button when logged in.
     */
    public void changeTopBtn() throws IOException {
        if (loggedIn) {
            myaccount = FXMLLoader.load(getClass().getResource("/menuButton.fxml"));

            if (!user.getRole().equals("ADMIN")) {
                myaccount.getItems().remove(admin);
            }

            myaccount.setText(user.getUsername());
            topbar.getChildren().add(myaccount);
        } else {
            topbtn.setText("Login");
            topbtn.setOnAction(e -> {
                try {
                    goToLoginScene();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Toggles the sidebar.
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
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String eventString = ServerCommunication.get(ServerCommunication.event);
        System.out.println(eventString);
        ArrayNode body = (ArrayNode) mapper.readTree(eventString).get("body");
        Set<Event> events = new HashSet<>();
        for (JsonNode eventJson : body) {
            String eventAsString = mapper.writeValueAsString(eventJson);
            Event event = mapper.readValue(eventAsString, Event.class);
            events.add(event);
        }
        return events;
    }

    /**
     * Checks if the given date is not during an event.
     *
     * @param date the selected date
     * @return boolean true if date is not an event, false if date is on an event
     * @throws JsonProcessingException when there is a processing exception
     */
    public boolean checkDate(String date) throws JsonProcessingException {
        Set<Event> events = getEventList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Event event : events) {
            LocalDate start = LocalDate.parse(event.getStartDate(), formatter);
            LocalDate end = LocalDate.parse(event.getEndDate(), formatter);
            LocalDate check = LocalDate.parse(date, formatter);
            if ((check.isBefore(end) && check.isAfter(start))
                || check.isEqual(start)
                || check.isEqual(end)) {
                createAlert("There is an event on this date");
                return false;
            }
        }
        return true;
    }

}
