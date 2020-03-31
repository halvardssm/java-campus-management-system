package nl.tudelft.oopp.group39.controllers.Admin.Booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.AdminPanelController;
import nl.tudelft.oopp.group39.models.Booking;
import nl.tudelft.oopp.group39.models.Room;

public class BookingListController extends AdminPanelController implements Initializable {
    @FXML
    private Button backbtn;
    @FXML
    private TableView<Booking> reservationTable = new TableView<>();
    @FXML
    private TableColumn<Booking, String> reservationIdCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Booking, String> userIdCol = new TableColumn<>("User ID");
    @FXML
    private TableColumn<Booking, String> roomIdCol = new TableColumn<>("Room ID");
    @FXML
    private TableColumn<Booking, String> reservationDateCol = new TableColumn<>("Reservation time");
    @FXML
    private TableColumn<Booking, String> reservationStartCol = new TableColumn<>("Reservation time");
    @FXML
    private TableColumn<Booking, String> reservationEndCol = new TableColumn<>("Reservation time");
    @FXML
    private TableColumn<Booking, Booking> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Booking, Booking> updateCol = new TableColumn<>("Update");
    @FXML
    private MenuBar navBar;

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadReservations();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }

    public ObservableList<Booking> getData() throws JsonProcessingException {
        String b = ServerCommunication.get(ServerCommunication.booking);
        ArrayNode body = (ArrayNode) mapper.readTree(b).get("body");
        b = mapper.writeValueAsString(body);
        Booking[] list = mapper.readValue(b, Booking[].class);
        return FXCollections.observableArrayList(list);
    }
    /**
     * Display rooms and data in tableView buildingTable. -- Likely doesn't work yet. Need to add nameCol.
     */
    void loadReservations() throws JsonProcessingException {
        reservationTable.setVisible(true);
        reservationTable.getItems().clear();
        reservationTable.getColumns().clear();

        ObservableList<Booking> data = getData();
        reservationIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        reservationDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        reservationStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        reservationEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        reservationTable.setItems(data);
        reservationTable.getColumns().addAll(reservationIdCol, userIdCol, roomIdCol, reservationDateCol, reservationStartCol, reservationEndCol, deleteCol, updateCol);
    }

    public TableCell<Booking, Booking> returnCell(String button) {
        return new TableCell<Booking, Booking>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);

                if (booking == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch(button){
                                case "Update":
                                    editBooking(booking);
                                    break;
                                case "Delete":
                                    deleteBooking(booking);
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        };
    }

    public void createBooking() throws IOException {
        switchFunc("/Admin/Booking/BookingCreate.fxml");
    }

    public void deleteBooking(Booking booking) throws IOException {
        String id = Integer.toString(booking.getId());
        ServerCommunication.removeBooking(id);
        createAlert("Removed the booking");
        loadReservations();
    }

    public void editBooking(Booking booking) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/Booking/BookingEdit.fxml");
        BookingEditController controller = loader.getController();
        controller.initData(booking);
    }

    public void viewRoom(Room room) throws IOException {
//        FXMLLoader loader = switchFunc("/Admin/Room/RoomView.fxml");
//        RoomViewController controller = loader.getController();
//        controller.initData(room);
    }

    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        switchFunc("/Admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }
}

