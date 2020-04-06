package nl.tudelft.oopp.group39.admin.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.booking.model.Booking;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

@SuppressWarnings("unchecked")
public class BookingListController extends AdminPanelController {
    @FXML
    private Button backbtn;
    @FXML
    private MenuBar navBar;
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
    private TableColumn<Booking, String> reservationStartCol = new TableColumn<>(
            "Reservation time");
    @FXML
    private TableColumn<Booking, String> reservationEndCol = new TableColumn<>("Reservation time");
    @FXML
    private TableColumn<Booking, Booking> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Booking, Booking> updateCol = new TableColumn<>("Update");

    /**
     * Initialize data into tableView.
     */
    public void customInit() {
        try {
            loadReservations();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);

    }

    /**
     * Creates observable list containing booking data needed for table.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public ObservableList<Booking> getData() throws JsonProcessingException {
        String b = ServerCommunication.get(ServerCommunication.booking);
        ArrayNode body = (ArrayNode) mapper.readTree(b).get("body");
        b = mapper.writeValueAsString(body);
        Booking[] list = mapper.readValue(b, Booking[].class);
        return FXCollections.observableArrayList(list);
    }

    /**
     * Display and load bookings and data into tableView named reservationTable.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    void loadReservations() throws JsonProcessingException {
        reservationTable.setVisible(true);
        reservationTable.getItems().clear();
        reservationTable.getColumns().clear();

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
        ObservableList<Booking> data = getData();
        reservationTable.setItems(data);
        reservationTable.getColumns().addAll(
              reservationIdCol,
              userIdCol,
              roomIdCol,
              reservationDateCol,
              reservationStartCol,
              reservationEndCol,
              deleteCol,
              updateCol);
    }

    /**
     * Inserts the update and delete buttons into table.
     */
    public TableCell<Booking, Booking> returnCell(String button) {
        return new TableCell<>() {
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
                            switch (button) {
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

    /**
     * Sends user to create booking scene.
     *
     * @throws IOException if an error occurs during loading
     */
    public void createBooking() throws IOException {
        FXMLLoader loader = switchFunc("/admin/booking/BookingCreate.fxml");
        BookingCreateController controller = loader.getController();
        controller.customInit();
        controller.changeUserBox();
    }

    /**
     * Deletes selected booking.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void deleteBooking(Booking booking) throws JsonProcessingException {
        String id = Integer.toString(booking.getId());
        ServerCommunication.removeBooking(id);
        createAlert("Removed the booking");
        loadReservations();
    }

    /**
     * Sends user to the booking edit page.
     *
     * @throws IOException if an error occurs during loading
     */
    public void editBooking(Booking booking) throws IOException {
        FXMLLoader loader = switchFunc("/admin/booking/BookingEdit.fxml");
        BookingEditController controller = loader.getController();
        controller.initData(booking);
        controller.changeUserBox();
    }


    /**
     * Switches back.
     *
     * @throws IOException if an error occurs during loading
     */
    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    /**
     * Switches screen.
     *
     * @param resource     the resource
     * @throws IOException if an error occurs during loading
     */
    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }
}

