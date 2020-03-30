package nl.tudelft.oopp.group39.controllers.Admin.Building;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.AdminPanelController;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.Building;


public class BuildingListController extends AdminPanelController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField locationFilter;
    @FXML
    private TextField descriptionFilter;
    @FXML
    private TableColumn<Building, String> nameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Building, String> idCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Building, String> locationCol = new TableColumn<>("Address");
    @FXML
    private TableColumn<Building, String> descriptionCol = new TableColumn<>("Description");
    @FXML
    private TableColumn<Building, LocalTime> openTimeCol = new TableColumn<>("Opening Time");
    @FXML
    private TableColumn<Building, LocalTime> closingTimeCol = new TableColumn<>("Closing Time");
    @FXML
    private TableColumn<Building, Building> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Building, Building> updateCol = new TableColumn<>("Update");
    @FXML
    private TableView<Building> buildingTable = new TableView<>();


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadAllBuildings();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    void loadAllBuildings() throws JsonProcessingException {
        String buildings = ServerCommunication.get(ServerCommunication.building);
        loadBuildings(buildings);
    }

    public void filterBuildings() throws JsonProcessingException {
        String name = nameFilter.getText();
        name = name == null ? "" : name;
        String description = descriptionFilter.getText();
        description = description == null ? "" : description;
        String location = locationFilter.getText();
        location = location == null ? "" : location;
        String open = getTime("00:00:00", true);
        String closed = getTime("23:59:00", false);
//        String capacity = capacityField.getText();
//        capacity = capacity == null ? "0" : capacity;
        String buildings = ServerCommunication.getFilteredBuildings(name, location, open, closed, description);
        loadBuildings(buildings);
    }

    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }

    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadBuildings(String buildings) throws JsonProcessingException {
        buildingTable.setVisible(true);
        buildingTable.getItems().clear();
        buildingTable.getColumns().clear();
        System.out.println(buildings);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        System.out.println(buildings);
        Building[] list = mapper.readValue(buildings, Building[].class);
        ObservableList<Building> data = FXCollections.observableArrayList(list);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        openTimeCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        closingTimeCol.setCellValueFactory(new PropertyValueFactory<>("closed"));

        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> new TableCell<Building, Building>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Building building, boolean empty) {
                super.updateItem(building, empty);

                if (building == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    event -> {
                        try {
                            deleteBuilding(building);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> new TableCell<Building, Building>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(Building building, boolean empty) {
                super.updateItem(building, empty);

                if (building == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            editBuilding(building);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        buildingTable.setItems(data);
        buildingTable.getColumns().addAll(nameCol, idCol, locationCol, closingTimeCol, openTimeCol, descriptionCol, deleteCol, updateCol);
    }


    public void createBuilding() throws IOException {
        switchFunc("/Admin/Building/BuildingCreate.fxml");
    }

    public void deleteBuilding(Building building) throws IOException {
        String id = Integer.toString(building.getId());
        ServerCommunication.removeBuilding(id);
        createAlert("removed: " + building.getName());
        loadAllBuildings();
    }

    public void editBuilding(Building building) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/Building/BuildingEdit.fxml");
        BuildingEditController controller = loader.getController();
        controller.initData(building);
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        switchFunc("/Admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}
