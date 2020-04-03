package nl.tudelft.oopp.group39.controllers;

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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Event;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.models.User;
import nl.tudelft.oopp.group39.views.UsersDisplay;

public class MainSceneController {

    protected ObjectMapper mapper = new ObjectMapper();

    public static boolean loggedIn = false;
    public static String jwt;
    public static boolean sidebarShown = false;
    public static User user;

    @FXML
    public VBox sidebar;

    @FXML
    protected Button topbtn;

    @FXML
    protected VBox topBox;


    public void createAlert(String content) {
        createAlert(null, content);
    }

    /**
     * Doc. TODO SVEN
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
            (BuildingSceneController) UsersDisplay.sceneControllerHandler("/buildingListView.fxml");
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
        changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToAddBuilding() throws IOException {
        UsersDisplay.sceneHandler("/buildingModifyScene.fxml");
        changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToLoginScene() throws IOException {
        LoginController controller =
            (LoginController) UsersDisplay.sceneControllerHandler("/login.fxml");
        controller.changeTopBtn();
    }


    /**
     * Doc. TODO Sven
     */
    public void goToSignupScene() throws IOException {
        SignupController controller =
            (SignupController) UsersDisplay.sceneControllerHandler("/signup.fxml");
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToRoomsScene(Building building) throws IOException {
        RoomSceneController controller =
            (RoomSceneController) UsersDisplay.sceneControllerHandler("/roomView.fxml");
        controller.setup(building);
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToRoomsScene() throws IOException {
        RoomSceneController controller =
            (RoomSceneController) UsersDisplay.sceneControllerHandler("/roomView.fxml");
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
    public void goToReservationScene(
        Room room,
        Building building,
        Scene previous
    ) throws IOException {
        RoomReservationController controller =
            (RoomReservationController) UsersDisplay.sceneControllerHandler(
                "/roomReservation.fxml");
        controller.setup(room, building, previous);
        controller.changeTopBtn();
    }

    /**
     * Doc. TODO Sven
     */
    public void goToBikeRentalScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) UsersDisplay
                .sceneControllerHandler("/bikeAndFoodView.fxml");
        controller.changeTopBtn();
        controller.setup("bike");
    }

    /**
     * Doc. TODO Sven
     */
    public void goToFoodOrderScene() throws IOException {
        FoodAndBikeSceneController controller =
            (FoodAndBikeSceneController) UsersDisplay
                .sceneControllerHandler("/bikeAndFoodView.fxml");
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
        changeTopBtn();
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
    public void changeTopBtn() {
        if (loggedIn) {
            MenuButton myaccount = new MenuButton(user.getUsername());
            MenuItem myres = new MenuItem("My Reservations");
            MenuItem myacc = new MenuItem("My Account");
            MenuItem logout = new MenuItem("Logout");
            MenuItem admin = new MenuItem("Admin panel");
            logout.setOnAction(event -> {
                try {
                    logout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            myaccount.getItems().addAll(myres, myacc, logout);
            if (user.getRole().equals("ADMIN")) {
                myaccount.getItems().add(admin);
            }
            topBox.getChildren().add(myaccount);
            topBox.getChildren().remove(topbtn);
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
    public void toggleSidebar() {
        if (!sidebarShown) {
            Hyperlink buildings = new Hyperlink("Buildings");
            buildings.getStyleClass().add("sidebar-item");
            buildings.setOnAction(event -> {
                try {
                    goToBuildingScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink rooms = new Hyperlink("Rooms");
            rooms.getStyleClass().add("sidebar-item");
            rooms.setOnAction(event -> {
                try {
                    goToRoomsScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink bikerental = new Hyperlink("Bike rental");
            bikerental.getStyleClass().add("sidebar-item");
            bikerental.setOnAction(event -> {
                try {
                    goToBikeRentalScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Hyperlink foodorder = new Hyperlink("Order food");
            foodorder.getStyleClass().add("sidebar-item");
            foodorder.setOnAction(event -> {
                try {
                    goToFoodOrderScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            sidebar.getChildren().addAll(buildings, rooms, bikerental, foodorder);
            sidebarShown = true;
        } else {
            sidebar.getChildren().clear();
            sidebarShown = false;
        }
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
