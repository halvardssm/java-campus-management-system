package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;
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
    protected HBox topbar;

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
        controller.getAllRooms();
        controller.changeTopBtn();
    }

    public void goToReservationScene(Room room, Building building) throws IOException {
        RoomReservationController controller = (RoomReservationController) UsersDisplay.sceneControllerHandler("/roomReservation.fxml");
        controller.setup(room, building);
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
        System.out.println(topbtn);
        System.out.println(topbar);
        System.out.println(loggedIn);

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
            topbar.getChildren().add(myaccount);
            topbar.getChildren().remove(topbtn);
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
            sidebar.getChildren().addAll(buildings, rooms, bikerental);
            sidebarShown = true;
        } else {
            sidebar.getChildren().clear();
            sidebarShown = false;
        }
    }

}
