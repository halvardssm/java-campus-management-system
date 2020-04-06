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

