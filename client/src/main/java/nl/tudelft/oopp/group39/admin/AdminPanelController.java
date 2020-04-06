package nl.tudelft.oopp.group39.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.booking.BookingListController;
import nl.tudelft.oopp.group39.admin.building.BuildingListController;
import nl.tudelft.oopp.group39.admin.event.EventListController;
import nl.tudelft.oopp.group39.admin.reservable.BikeListController;
import nl.tudelft.oopp.group39.admin.reservable.FoodListController;
import nl.tudelft.oopp.group39.admin.room.RoomListController;
import nl.tudelft.oopp.group39.admin.user.UserListController;


public class AdminPanelController extends MainAdminController implements Initializable {
    @FXML
    private Button buildingView;

    /**
     * The initializer.
     *
     * @param location  the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Horizontal navigation bar to switch scenes in admin panel.
     */
    public void setNavBar(MenuBar menuBar, Stage currentstage) {
        Label userListLabel = new Label("User list");
        userListLabel.setStyle("-fx-text-fill: black");
        userListLabel.setOnMouseClicked(event -> {
            try {
                switchUserView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton1 = new Menu();
        fileMenuButton1.setGraphic(userListLabel);
        menuBar.getMenus().add(fileMenuButton1);
        Label roomListLabel = new Label("Room list");
        roomListLabel.setStyle("-fx-text-fill: black");
        roomListLabel.setOnMouseClicked(event -> {
            try {
                switchRoomView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton2 = new Menu();
        fileMenuButton2.setGraphic(roomListLabel);
        menuBar.getMenus().add(fileMenuButton2);
        Label eventListLabel = new Label("Event list");
        eventListLabel.setStyle("-fx-text-fill: black");
        eventListLabel.setOnMouseClicked(event -> {
            try {
                switchEventView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton3 = new Menu();
        fileMenuButton3.setGraphic(eventListLabel);
        menuBar.getMenus().add(fileMenuButton3);
        Label buildingListLabel = new Label("Building list");
        buildingListLabel.setStyle("-fx-text-fill: black");
        buildingListLabel.setOnMouseClicked(event -> {
            try {
                switchBuildingView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton4 = new Menu();
        fileMenuButton4.setGraphic(buildingListLabel);
        menuBar.getMenus().add(fileMenuButton4);
        Label reservationListLabel = new Label("Bookings list");
        reservationListLabel.setStyle("-fx-text-fill: black");
        reservationListLabel.setOnMouseClicked(event -> {
            try {
                switchBookingsView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton5 = new Menu();
        fileMenuButton5.setGraphic(reservationListLabel);
        menuBar.getMenus().add(fileMenuButton5);
        Label foodListLabel = new Label("Food list");
        foodListLabel.setStyle("-fx-text-fill: black");
        foodListLabel.setOnMouseClicked(event -> {
            try {
                switchFoodView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton6 = new Menu();
        fileMenuButton6.setGraphic(foodListLabel);
        menuBar.getMenus().add(fileMenuButton6);
        Label bikeListLabel = new Label("Bike list");
        bikeListLabel.setStyle("-fx-text-fill: black");
        bikeListLabel.setOnMouseClicked(event -> {
            try {
                switchBikeView(currentstage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Menu fileMenuButton7 = new Menu();
        fileMenuButton7.setGraphic(bikeListLabel);
        menuBar.getMenus().add(fileMenuButton7);
    }


    /**
     * Used to switch to bookings list.
     *
     * @throws IOException if an error occurs during loading
     */
    public void switchBookingsView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/booking/BookingList.fxml", currentstage);
        BookingListController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Used to switch to booking view.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchBookingsView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchBookingsView(currentStage);
    }

    /**
     * Used to switch to building list.
     *
     * @throws IOException if an error occurs during loading
     */
    public void switchBuildingView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/building/BuildingList.fxml", currentstage);
        BuildingListController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Used to switch to building view.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchBuildingView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchBuildingView(currentStage);
    }

    /**
     * Used to switch to room list.
     *
     * @throws IOException if an error occurs during loading
     */
    public void switchRoomView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/room/RoomList.fxml", currentstage);
        RoomListController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Used to switch to room list.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchRoomView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchRoomView(currentStage);
    }

    /**
     * Used to switch to user list.
     *
     * @throws IOException if an error occurs during loading
     */
    public void switchUserView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/user/UserList.fxml", currentstage);
        UserListController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Used to switch to user list.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchUserView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchUserView(currentStage);
    }

    /**
     * Used to switch to event list.
     *
     * @throws IOException if an error occurs during loading
     */
    public void switchEventView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/event/EventList.fxml", currentstage);
        EventListController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Used to switch to event list.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchEventView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchEventView(currentStage);
    }

    /**
     * Used to switch to food list.
     */

    public void switchFoodView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/reservable/FoodList.fxml", currentstage);
        FoodListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchFoodView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchFoodView(currentStage);
    }

    /**
     * Used to switch to bike list.
     */

    public void switchBikeView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/reservable/BikeList.fxml", currentstage);
        BikeListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchBikeView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchBikeView(currentStage);
    }

    /**
     * Goes back to the building list view.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void getBackFromAdmin() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        mainSwitch("/building/buildingListView.fxml", currentStage);
    }
}

