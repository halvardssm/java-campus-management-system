package nl.tudelft.oopp.group39.controllers.admin.building;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.controllers.admin.AdminPanelController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

@SuppressWarnings("unchecked")
public class BuildingListController extends AdminPanelController {

    private String start;
    private String end;
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    @FXML
    private ComboBox<String> openingBox;
    @FXML
    private ComboBox<String> closingBox;
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
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit() throws JsonProcessingException {
        try {
            loadAllBuildings();
            loadTimeSlots();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * loads all buildings.
     */

    public void loadAllBuildings() throws JsonProcessingException {
        String buildings = ServerCommunication.get(ServerCommunication.building);
        loadFiltering();
        loadBuildings(buildings);
    }

    public void loadTimeSlots() throws JsonProcessingException {
        List<String> timeSlots = initiateTimeslots();
        this.start = timeSlots.get(0);
        this.end = timeSlots.get(timeSlots.size() - 1);
        ObservableList<String> list = FXCollections.observableList(timeSlots);
        openingBox.setItems(list);
        closingBox.setItems(list);
        openingBox.setPromptText(this.start);
        closingBox.setPromptText(this.end);
    }
    /**
     * Clears filtering boxes.
     */
    public void loadFiltering() {
        nameFilter.clear();
        descriptionFilter.clear();
        locationFilter.clear();
    }
    /**
     * Filters buildings.
     * @throws JsonProcessingException when there is a processing exception
     */

    public void filterBuildings() throws JsonProcessingException {
        String name = nameFilter.getText();
        name = name == null ? "" : name;
        String description = descriptionFilter.getText();
        description = description == null ? "" : description;
        String location = locationFilter.getText();
        location = location == null ? "" : location;
        Object opening = openingBox.getValue();
        String open = opening == null ? start + ":00" : opening.toString() + ":00";
        Object closing = closingBox.getValue();
        String closed = closing == null ? end + ":00" : closing.toString() + ":00";
        String buildings = ServerCommunication.getFilteredBuildings(name, location, open, closed, description);
        loadBuildings(buildings);
    }
    /**
     * Returns time as string.
     */

    public String getTime(String time, boolean open) {
        if (open) {
            return time.contentEquals("") ? LocalTime.MAX.toString() : time;
        }
        return time.contentEquals("") ? LocalTime.MIN.toString() : time;
    }

    /**
     * Display buildings and data in tableView buildingTable.
     * @throws JsonProcessingException when there is a processing exception
     */

    void loadBuildings(String buildings) throws JsonProcessingException {
        buildingTable.setVisible(true);
        buildingTable.getItems().clear();
        buildingTable.getColumns().clear();
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        openTimeCol.setCellValueFactory(new PropertyValueFactory<>("open"));
        closingTimeCol.setCellValueFactory(new PropertyValueFactory<>("closed"));

        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        Building[] list = mapper.readValue(buildings, Building[].class);
        ObservableList<Building> data = FXCollections.observableArrayList(list);
        buildingTable.setItems(data);
        buildingTable.getColumns().addAll(
             idCol,
             nameCol,
             locationCol,
             closingTimeCol,
             openTimeCol,
             descriptionCol,
             deleteCol,
             updateCol);
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Building, Building> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

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
                            switch (button) {
                                case "Update":
                                    editBuilding(building);
                                    break;
                                case "Delete":
                                    deleteBuilding(building);
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
     * Sends user to Building Create scene.
     */
    public void createBuilding() throws IOException {
        FXMLLoader loader = switchFunc("/admin/building/BuildingCreate.fxml");
        BuildingCreateController controller = loader.getController();
        controller.customInit();
    }
    /**
     * Deletes selected building.
     */

    public void deleteBuilding(Building building) throws IOException {
        String id = Integer.toString(building.getId());
        ServerCommunication.removeBuilding(id);
        loadAllBuildings();
    }
    /**
     * Sends user to the building edit page.
     */

    public void editBuilding(Building building) throws IOException {
        FXMLLoader loader = switchFunc("/admin/building/BuildingEdit.fxml");
        BuildingEditController controller = loader.getController();
        controller.initData(building);
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

    public List<String> initiateTimeslots() throws JsonProcessingException {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String time;
            if (i < 10) {
                time = "0" + i + ":00";
            } else {
                time = i + ":00";
            }
            times.add(time);
        }
        return times;
    }

}
