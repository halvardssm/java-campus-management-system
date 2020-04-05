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
import nl.tudelft.oopp.group39.admin.room.RoomListController;
import nl.tudelft.oopp.group39.admin.user.UserListController;


public class AdminPanelController extends MainAdminController implements Initializable {

    @FXML
    private Button buildingView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /**
     * Used to switch to bookings list.
     */

    public void switchBookingsView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/booking/BookingList.fxml", currentstage);
        BookingListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchBookingsView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchBookingsView(currentStage);
    }
    /**
     * Used to switch to building list.
     */

    public void switchBuildingView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/building/BuildingList.fxml", currentstage);
        BuildingListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchBuildingView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchBuildingView(currentStage);
    }
    /**
     * Used to switch to room list.
     */

    public void switchRoomView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/room/RoomList.fxml", currentstage);
        RoomListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchRoomView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchRoomView(currentStage);
    }
    /**
     * Used to switch to user list.
     */

    public void switchUserView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/user/UserList.fxml", currentstage);
        UserListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchUserView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchUserView(currentStage);
    }

    /**
     * Used to switch to event list.
     */

    public void switchEventView(Stage currentstage) throws IOException {
        FXMLLoader loader = mainSwitch("/admin/event/EventList.fxml", currentstage);
        EventListController controller = loader.getController();
        controller.customInit();
    }

    @FXML
    private void switchEventView() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        switchEventView(currentStage);
    }

    @FXML
    private void getBackFromAdmin() throws IOException {
        Stage currentStage = (Stage) buildingView.getScene().getWindow();
        mainSwitch("/building/buildingListView.fxml", currentStage);
    }
}

