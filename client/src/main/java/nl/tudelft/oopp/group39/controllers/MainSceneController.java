package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController {

    protected ObjectMapper mapper = new ObjectMapper();

    public static boolean loggedIn = false;
    public static String jwt;
    public static boolean sidebarShown = false;
    public static boolean isAdmin = false;
    public static String username;

    @FXML
    public VBox sidebar;

    @FXML
    protected Button topbtn;

    @FXML
    protected HBox topbar;

    public void createAlert(String content) {
        createAlert(null, content);
    }

    /**
     * Doc. TODO Sven
     */
    public void createAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToMainScene() throws IOException {
        UsersDisplay.sceneHandler("/mainScene.fxml");
        changeTopBtn();
    }

    public void goToBuildingScene() throws IOException {
        BuildingSceneController controller = (BuildingSceneController) UsersDisplay.sceneControllerHandler("/buildingListView.fxml");
        controller.changeTopBtn();
    }

    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
        changeTopBtn();
    }

    public void goToAddBuilding() throws IOException {
        UsersDisplay.sceneHandler("/buildingModifyScene.fxml");
        changeTopBtn();
    }

    public void goToLoginScene() throws IOException {
        LoginController controller = (LoginController) UsersDisplay.sceneControllerHandler("/login.fxml");
        controller.changeTopBtn();
    }

    public void goToSignupScene() throws IOException {
        SignupController controller = (SignupController) UsersDisplay.sceneControllerHandler("/signup.fxml");
        controller.changeTopBtn();
    }

    public void goToRoomsScene(long buildingId, String name, String address) throws IOException {
        RoomSceneController controller = (RoomSceneController) UsersDisplay.sceneControllerHandler("/roomView.fxml");
        controller.setup(buildingId, name, address);
        controller.changeTopBtn();
    }

    public void goToRoomsScene() throws IOException {
        RoomSceneController controller = (RoomSceneController) UsersDisplay.sceneControllerHandler("/roomView.fxml");
        controller.getAllRooms();
        controller.changeTopBtn();
    }


    public void logout() throws IOException {
        loggedIn = false;
        jwt = null;
        goToBuildingScene();
        changeTopBtn();
    }


    public void getFacilitiesButton() {
        createAlert(null, ServerCommunication.getFacilities());
    }

    public void getUsersButton() {
        createAlert(ServerCommunication.getUsers());
    }

    public void changeTopBtn() {
        System.out.println(topbtn);
        System.out.println(topbar);
        System.out.println(loggedIn);

        if (loggedIn){
            MenuButton myaccount = new MenuButton("My Account");
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
            topbar.getChildren().add(myaccount);
            topbar.getChildren().remove(topbtn);
        }
        else {
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

    public void toggleSidebar() {
        if(sidebarShown == false){
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
        }
        else {
            sidebar.getChildren().clear();
            sidebarShown = false;
        }
    }

}
