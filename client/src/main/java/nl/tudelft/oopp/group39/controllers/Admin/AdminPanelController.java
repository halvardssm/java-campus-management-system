package nl.tudelft.oopp.group39.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.views.AdminPanel;
import nl.tudelft.oopp.group39.views.UsersDisplay;


public class AdminPanelController extends MainAdminController implements Initializable {
    @FXML
    private Button userlistView;
    @FXML
    private Button buildingView;
    @FXML
    private Button roomView;
    @FXML
    private Button eventView;
    @FXML
    private MenuBar NavBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNavBar(NavBar);
    }

    public void setNavBar(MenuBar menuBar) {
        Label userListLabel = new Label("User list");
        userListLabel.setStyle("-fx-text-fill: black");
        userListLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    AdminPanel.sceneHandler("/Admin/User/UserList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Menu fileMenuButton1 = new Menu();
        fileMenuButton1.setGraphic(userListLabel);
        menuBar.getMenus().add(fileMenuButton1);
        Label roomListLabel = new Label("Room list");
        roomListLabel.setStyle("-fx-text-fill: black");
        roomListLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    AdminPanel.sceneHandler("/Admin/Room/RoomList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Menu fileMenuButton2 = new Menu();
        fileMenuButton2.setGraphic(roomListLabel);
        menuBar.getMenus().add(fileMenuButton2);
        Label eventListLabel = new Label("Event list");
        eventListLabel.setStyle("-fx-text-fill: black");
        eventListLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    AdminPanel.sceneHandler("/Admin/Event/EventList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Menu fileMenuButton3 = new Menu();
        fileMenuButton3.setGraphic(eventListLabel);
        menuBar.getMenus().add(fileMenuButton3);
        Label buildingListLabel = new Label("Building list");
        buildingListLabel.setStyle("-fx-text-fill: black");
        buildingListLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    AdminPanel.sceneHandler("/Admin/Building/BuildingList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Menu fileMenuButton4 = new Menu();
        fileMenuButton4.setGraphic(buildingListLabel);
        menuBar.getMenus().add(fileMenuButton4);
    }

}

