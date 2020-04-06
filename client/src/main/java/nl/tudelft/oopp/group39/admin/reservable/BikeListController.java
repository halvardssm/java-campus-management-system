package nl.tudelft.oopp.group39.admin.reservable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class BikeListController extends AdminPanelController {

    private ObjectMapper mapper = new ObjectMapper();
    private String lastSelectedRole;
    private String lastSelectedName;
    private String allRoles = "ALL ROLES";
    @FXML
    private Button backbtn;
    @FXML private TableView<Bike> bikeTable;
    @FXML private TableColumn<Bike, String> idCol;
    @FXML private TableColumn<Bike, String> bikeTypeCol;
    @FXML private TableColumn<Bike, String> rentalDurationCol;
    @FXML private TableColumn<Bike, Double> priceCol;
    @FXML private TableColumn<Bike, String> buildingCol;
    @FXML
    private TableColumn<Bike, Bike> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Bike, Bike> updateCol = new TableColumn<>("Update");
    @FXML
    private ComboBox<String> roleBox;
    @FXML
    private TextField usernameField;
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit() {
        try {
            loadBike();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Display users and data into tableView named userTable.
     */

    @SuppressWarnings("checkstyle:CommentsIndentation")
    void loadBike() throws JsonProcessingException {
        bikeTable.setVisible(true);
        bikeTable.getItems().clear();
        bikeTable.getColumns().clear();
        String bikes = ServerCommunication.get(ServerCommunication.bike);
        System.out.println(bikes);
        if (!bikes.contains("\"body\" : null,")) {
            ArrayNode body = (ArrayNode) mapper.readTree(bikes).get("body");
            bikes = mapper.writeValueAsString(body);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            bikeTypeCol.setCellValueFactory(new PropertyValueFactory<>("bikeType"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
            rentalDurationCol.setCellValueFactory(new PropertyValueFactory<>("rentalDuration"));
            deleteCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            deleteCol.setCellFactory(param -> returnCell("Delete"));
            updateCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            Bike[] list = mapper.readValue(bikes, Bike[].class);
            updateCol.setCellFactory(param -> returnCell("Update"));
            ObservableList<Bike> data = FXCollections.observableArrayList(list);
            bikeTable.setItems(data);
            bikeTable.getColumns().addAll(
                idCol,
                bikeTypeCol,
                priceCol,
                buildingCol,
                rentalDurationCol,
                deleteCol,
                updateCol);
        }
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Bike, Bike> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Bike bike, boolean empty) {
                super.updateItem(bike, empty);

                if (bike == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editBikeItem(bike);
                                    break;
                                case "Delete":
                                    deleteBikeItem(bike);
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
    public void createBike() throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/BikeCreate.fxml");
        BikeCreateController controller = loader.getController();
        controller.initData();
        controller.changeUserBox();
    }
    /**
     * Deletes selected user.
     */

    public void deleteBikeItem(Bike bike) throws IOException {
        String id = Long.toString(bike.getId());
        ServerCommunication.removeBike(id);
        loadBike();
    }
    /**
     * Sends user to the user edit page.
     */

    public void editBikeItem(Bike bike) throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/BikeEdit.fxml");
        BikeEditController controller = loader.getController();
        controller.initData(bike);
        controller.changeUserBox();
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
