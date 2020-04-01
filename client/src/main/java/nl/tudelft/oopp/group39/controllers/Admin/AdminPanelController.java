package nl.tudelft.oopp.group39.controllers.Admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.views.AdminPanel;


public class AdminPanelController extends MainAdminController implements Initializable {
    @FXML
    private Button userView;
    @FXML
    private Button buildingView;
    @FXML
    private Button roomView;
    @FXML
    private Button eventView;
    @FXML
    private Button bookingsView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        Label reservationListLabel = new Label("Reservation list");
        reservationListLabel.setStyle("-fx-text-fill: black");
        reservationListLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    AdminPanel.sceneHandler("/Admin/Booking/BookingList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Menu fileMenuButton5 = new Menu();
        fileMenuButton5.setGraphic(reservationListLabel);
        menuBar.getMenus().add(fileMenuButton5);
    }

    @FXML
    private void switchBookingsView() throws IOException {
        AdminPanel.sceneHandler("/Admin/Booking/BookingList.fxml");
    }

    @FXML
    private void switchBuildingView() throws IOException {
        AdminPanel.sceneHandler("/Admin/Building/BuildingList.fxml");
    }

    @FXML
    private void switchRoomView() throws IOException {
        AdminPanel.sceneHandler("/Admin/Room/RoomList.fxml");
    }


    @FXML
    private void switchUserView() throws IOException {
        AdminPanel.sceneHandler("/Admin/User/UserList.fxml");
    }


    @FXML
    private void switchEventView() throws IOException {
        AdminPanel.sceneHandler("/Admin/Event/EventList.fxml");
    }


}

