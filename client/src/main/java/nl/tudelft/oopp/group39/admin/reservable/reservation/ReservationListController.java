package nl.tudelft.oopp.group39.admin.reservable.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import nl.tudelft.oopp.group39.reservation.model.ReservableNode;
import nl.tudelft.oopp.group39.reservation.model.Reservation;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class ReservationListController extends AdminPanelController {

    private ObjectMapper mapper = new ObjectMapper();
    private String lastSelectedRole;
    private String lastSelectedName;
    private String allRoles = "ALL ROLES";
    @FXML
    private Button backbtn;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, String> idCol;
    @FXML private TableColumn<Reservation, String> timeOfDeliveryCol;
    @FXML private TableColumn<Reservation, String> timeOfPickupCol;
    @FXML private TableColumn<Reservation, String> roomCol;
    @FXML private TableColumn<Reservation, String> userCol;
    @FXML private TableColumn<Reservation, String> reservationAmountsCol;
    @FXML
    private TableColumn<Reservation, Reservation> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Reservation, Reservation> updateCol = new TableColumn<>("Update");
    @FXML
    private TableColumn<Reservation, Reservation> viewItemsCol = new TableColumn<>("View/update items");
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
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
     * Display users and data into tableView named userTable.
     */

    void loadReservations() throws JsonProcessingException {
        reservationTable.setVisible(true);
        reservationTable.getItems().clear();
        reservationTable.getColumns().clear();

        String reservations = ServerCommunication.get(ServerCommunication.reservation);
//        System.out.println(foodItems);
        if (!reservations.contains("\"body\" : null,")) {
            ArrayNode body = (ArrayNode) mapper.readTree(reservations).get("body");
            reservations = mapper.writeValueAsString(body);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            timeOfDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("timeOfDelivery"));
            timeOfPickupCol.setCellValueFactory(new PropertyValueFactory<>("timeOfPickup"));
            roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
            reservationAmountsCol.setCellValueFactory(new PropertyValueFactory<>("reservationAmounts"));
            userCol.setCellValueFactory(new PropertyValueFactory<>("user"));
            deleteCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            deleteCol.setCellFactory(param -> returnCell("Delete"));
            updateCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            updateCol.setCellFactory(param -> returnCell("Update"));
            viewItemsCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            viewItemsCol.setCellFactory(param -> returnCell("View"));
            Reservation[] list = mapper.readValue(reservations, Reservation[].class);
            ObservableList<Reservation> data = FXCollections.observableArrayList(list);
            reservationTable.setItems(data);
            reservationTable.getColumns().addAll(
                idCol,
                timeOfDeliveryCol,
                timeOfPickupCol,
                roomCol,
                reservationAmountsCol,
                deleteCol,
                updateCol,
                viewItemsCol);
        }
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Reservation, Reservation> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (reservation == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editReservation(reservation);
                                    break;
                                case "Delete":
                                    deleteReservation(reservation);
                                    break;
                                case "View": //@@@@@@@@
                                    viewReservationItems(getReservables(reservation.getReservationAmounts()), reservation.getId());
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
     * Switches scene to the createUser one.
     */
    public void createReservation() throws IOException {
//        FXMLLoader loader = switchFunc("/admin/reservable/FoodCreate.fxml");
//        FoodCreateController controller = loader.getController();
//        controller.initData();
    }
    /**
     * Deletes selected user.
     */

    public void deleteReservation(Reservation reservation) throws IOException {
//        String id = Long.toString(food.getId());
//        ServerCommunication.removeFoodItem(id);
//        loadFood();
    }
    /**
     * Sends user to the user edit page.
     */

    public void editReservation(Reservation reservation) throws IOException {
//        FXMLLoader loader = switchFunc("/admin/reservable/FoodEdit.fxml");
//        FoodEditController controller = loader.getController();
//        controller.initData(reservation);
    }

    /**
     * Sends user to the reservation items list page.
     */

    public void viewReservationItems(List<ReservableNode> reservables, long reservation) throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/ReservationItemList.fxml");
        createAlert(reservables.toString());
        ReservationItemListController controller = loader.getController();
        controller.customInit(reservables, reservation);
    }

    public List<ReservableNode> getReservables(ArrayNode reservationAmounts) {
        List<ReservableNode> reservableNodeList = new ArrayList<>();
        ReservableNode rs = new ReservableNode();
        for (JsonNode object : reservationAmounts) {
            Integer id = object.get("id").asInt();
            Integer amount = object.get("amount").asInt();
            Integer reservable = object.get("reservable").asInt();
            System.out.println(id + " : " + amount + " : " + reservable);
            rs = new ReservableNode(id, amount, reservable);
            reservableNodeList.add(rs);
        }
        return reservableNodeList;
    }
    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}
