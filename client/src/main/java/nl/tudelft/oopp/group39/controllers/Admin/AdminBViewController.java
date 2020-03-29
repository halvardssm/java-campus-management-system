package nl.tudelft.oopp.group39.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.Building;


public class AdminBViewController extends MainSceneController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML
    private TextField nameFieldNew;
    @FXML
    private TextField locationFieldNew;
    @FXML
    private TextField descriptionFieldNew;
    @FXML
    private TextField timeOpenFieldNew;
    @FXML
    private TextField timeClosedFieldNew;
    @FXML
    private TextField capacityField;
    @FXML
    private TableColumn<Building, String> buildingnameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Building, String> buildingidCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Building, String> buildinglocationCol = new TableColumn<>("Address");
    @FXML
    private TableColumn<Building, String> buildingDescriptionCol = new TableColumn<>("Description");
    @FXML
    private TableColumn<Building, LocalTime> buildingOpenTimeCol = new TableColumn<>("Opening Time");
    @FXML
    private TableColumn<Building, LocalTime> buildingCTimeCol = new TableColumn<>("Closing Time");
    @FXML
    private TableColumn<Building, Building> buildingDelCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Building, Building> buildingUpCol = new TableColumn<>("Update");
    @FXML
    private TableView<Building> buildingTable = new TableView<>();


    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadBuildingsStandard();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    void loadBuildingsStandard() throws JsonProcessingException {
        String buildings = ServerCommunication.get(ServerCommunication.building);
        loadBuildings(buildings);
    }

    public void filterBuildings() throws JsonProcessingException {
        String name = nameFieldNew.getText();
        name = name == null ? "" : name;
        String description = descriptionFieldNew.getText();
        description = description == null ? "" : description;
        String location = locationFieldNew.getText();
        location = location == null ? "" : location;
        String open = getTime("00:00:00", true);
        String closed = getTime("23:59:00", false);
//        String capacity = capacityField.getText();
//        capacity = capacity == null ? "0" : capacity;
        String buildings = ServerCommunication.getFilteredBuildings(name, location, open, closed, description);
        System.out.println(buildings);
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
        buildingnameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        buildingidCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        buildinglocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        buildingDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        buildingOpenTimeCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        buildingCTimeCol.setCellValueFactory(new PropertyValueFactory<>("closed"));

        buildingDelCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        buildingDelCol.setCellFactory(param -> new TableCell<Building, Building>() {
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
                            deleteBuildingView(building);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        buildingUpCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        buildingUpCol.setCellFactory(param -> new TableCell<Building, Building>() {
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
                            updateBuildingView(building);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        buildingTable.setItems(data);
        buildingTable.getColumns().addAll(buildingnameCol, buildingidCol, buildinglocationCol, buildingCTimeCol, buildingOpenTimeCol, buildingDescriptionCol, buildingDelCol, buildingUpCol);
    }


    public void adminAddBuilding() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminAddBuilding.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

    public void deleteBuildingView(Building building) throws IOException {
        String id = Integer.toString(building.getId());
        ServerCommunication.removeBuilding(id);
        createAlert("removed: " + building.getName());
        loadBuildingsStandard();
    }

    public void updateBuildingView(Building building) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/AdminUpdateBuilding.fxml"));
        Parent root = loader.load();
        AdminUpdateBuildingController controller = loader.getController();
        controller.initData(building);
        currentstage.setScene(new Scene(root, 700, 600));
    }

    /**
     * Goes back to main admin panel.
     */

    @FXML
    private void switchBack() throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/Admin/AdminPanel.fxml"));
        currentstage.setScene(new Scene(root, 700, 600));
    }

}
