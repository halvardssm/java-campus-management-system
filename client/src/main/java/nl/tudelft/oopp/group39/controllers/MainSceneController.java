package nl.tudelft.oopp.group39.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

import java.io.IOException;

public class MainSceneController {

    public static boolean loggedIn = false;
    public static String jwt;



    public void createAlert(String content) {
        createAlert(null, content);
    }

    public void createAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToMainScene() throws IOException {
        UsersDisplay.sceneHandler("/mainScene.fxml");
    }

    public void goToBuildingScene() throws IOException {
        UsersDisplay.sceneHandler("/buildingListView.fxml");
    }

    public void goToRoomScene() throws IOException {
        UsersDisplay.sceneHandler("/roomScene.fxml");
    }

    public void goToAddBuilding() throws IOException {
        UsersDisplay.sceneHandler("/buildingModifyScene.fxml");
    }

    public void goToLoginScene() throws IOException {
        UsersDisplay.sceneHandler("/login.fxml");
    }

    public void goToSignupScene() throws IOException {
        UsersDisplay.sceneHandler("/signup.fxml");
    }

    public void logout() throws IOException {
        loggedIn = false;
        jwt = null;
        goToBuildingScene();
    }


    public void getFacilitiesButton() {
        createAlert(null, ServerCommunication.getFacilities());
    }

    public void getUsersButton() {
        createAlert(ServerCommunication.getUsers());
    }

    public void changeBtn(Button btn) {
        System.out.println(btn);
        if (loggedIn){
            btn.setText("Log out");
            btn.setOnAction(e -> {
                try {
                    logout();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
        else {
            btn.setText("Login");
            btn.setOnAction(e -> {
                    try {
                        goToLoginScene();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
        }
    }

}
