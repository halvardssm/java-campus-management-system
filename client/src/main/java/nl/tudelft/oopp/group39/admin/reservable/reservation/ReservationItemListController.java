package nl.tudelft.oopp.group39.admin.reservable.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
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

public class ReservationItemListController extends AdminPanelController {

    private ObjectMapper mapper = new ObjectMapper();
    private String lastSelectedRole;
    private String lastSelectedName;
    private String allRoles = "ALL ROLES";
    private long reservation;
    @FXML
    private Button backbtn;
    @FXML private TableView<ReservableNode> reservationTable;
    @FXML private TableColumn<ReservableNode, String> idCol;
    @FXML private TableColumn<ReservableNode, String> amountCol;
    @FXML private TableColumn<ReservableNode, String> reservableCol;
    @FXML
    private TableColumn<ReservableNode, ReservableNode> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<ReservableNode, ReservableNode> updateCol = new TableColumn<>("Update");
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit(List<ReservableNode> reservables, long reservation) throws JsonProcessingException {
        try {
            loadReservationItems(reservables);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.reservation = reservation;
        System.out.println(ServerCommunication.getReservation(reservation).getReservationAmounts().toString());
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Display users and data into tableView named userTable.
     */

    void loadReservationItems(List<ReservableNode> reservables) throws JsonProcessingException {
        reservationTable.setVisible(true);
        reservationTable.getItems().clear();
        reservationTable.getColumns().clear();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        reservableCol.setCellValueFactory(new PropertyValueFactory<>("reservable"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        ObservableList<ReservableNode> data = FXCollections.observableArrayList(reservables);
        reservationTable.setItems(data);
        reservationTable.getColumns().addAll(
            idCol,
            amountCol,
            reservableCol,
            deleteCol,
            updateCol);

    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<ReservableNode, ReservableNode> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(ReservableNode reservableNode, boolean empty) {
                super.updateItem(reservableNode, empty);

                if (reservableNode == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editReservableNode(reservableNode);
                                    break;
                                case "Delete":
                                    deleteReservableNode(reservableNode);
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

    public void deleteReservableNode(ReservableNode reservableNode) throws IOException {
//        String id = Long.toString(food.getId());
//        ServerCommunication.removeFoodItem(id);
//        loadFood();
    }
    /**
     * Sends user to the user edit page.
     */

    public void editReservableNode(ReservableNode reservableNode) throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/ReservationItemEdit.fxml");
        ReservationItemEditController controller = loader.getController();
        controller.initData(reservableNode, reservation);
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
