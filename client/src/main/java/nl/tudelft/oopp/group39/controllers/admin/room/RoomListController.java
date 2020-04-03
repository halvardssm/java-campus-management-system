package nl.tudelft.oopp.group39.controllers.admin.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.admin.AdminPanelController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.facility.model.Facility;
import nl.tudelft.oopp.group39.room.model.Room;

public class RoomListController extends AdminPanelController implements Initializable {
    /**
     * TODO Sven
     */
    private ObjectMapper mapper = new ObjectMapper();
    @FXML
    private Button backbtn;
    @FXML
    private TableView<Room> roomTable = new TableView<>();
    @FXML
    private TableColumn<Room, String> roomIdCol = new TableColumn<>("ID");
    @FXML
    private TableColumn<Room, String> buildingIdCol = new TableColumn<>("Building ID");
    @FXML
    private TableColumn<Room, String> capacityCol = new TableColumn<>("Capacity");
    @FXML
    private TableColumn<Room, String> onlyStaffCol = new TableColumn<>("Only staff");
    @FXML
    private TableColumn<Room, String> nameCol = new TableColumn<>("Name");
    @FXML
    private TableColumn<Room, Room> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Room, Room> viewCol = new TableColumn<>("View");
    @FXML
    private TableColumn<Room, Room> updateCol = new TableColumn<>("Update");
    private HashMap<String, Integer> buildingsByName = new HashMap();
    private HashMap<Integer, String> buildingsById = new HashMap();
    private HashMap<String, Long> facilitiesByName = new HashMap();
    private HashMap<Long, String> facilitiesById = new HashMap();
    @FXML
    private ComboBox roomBuildingIdField;
    @FXML
    private ComboBox roomOnlyStaffField;
    @FXML
    private ListView facilitiesList;
    @FXML
    private TextField roomNameField;
    @FXML
    private MenuBar navBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadRooms();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }

    /**
     * Gets the names of buildings.
     * @param b building
     * @return A list of building names
     * @throws JsonProcessingException when there is a processing exception
     */
    public ObservableList<String> getData(String b) throws JsonProcessingException {
        List<Building> buildings = getBuildings(b);
        List<String> buildingNames = getBuildingNames(buildings);
        return FXCollections.observableArrayList(buildingNames);
    }

    /**
     * Gets only the names of facilities.
     * @param b facilities
     * @return list of facility data.
     * @throws JsonProcessingException when there is a processing exception.
     */

    public ObservableList<String> getFacilityData(String b) throws JsonProcessingException {
        List<Facility> facilities = getFacility(b);
        List<String> facilityNames = getFacilityNames(facilities);
        return FXCollections.observableArrayList(facilityNames);
    }

    /**
     * Loads the values of rooms and puts them into tableView.
     * @throws JsonProcessingException when there is a processing exception.
     */

    void loadRooms() throws JsonProcessingException {
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> buildingData = getData(b);
        String f = ServerCommunication.get(ServerCommunication.facility);
        System.out.println("tset: " + f);
        ObservableList<String> facData = getFacilityData(f);
        List<String> options = new ArrayList<>();
        options.add("All users");
        options.add("Only staff members");
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        roomBuildingIdField.setItems(buildingData);
        roomBuildingIdField.setPromptText(buildingData.get(0));
        roomOnlyStaffField.setItems(dataOptions);
        facilitiesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        facilitiesList.setItems(facData);
        roomOnlyStaffField.setPromptText(dataOptions.get(0));
        roomTable.setVisible(true);
        roomTable.getItems().clear();
        roomTable.getColumns().clear();

        String rooms = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(rooms).get("body");
        System.out.println(rooms + "\n" + body.toString());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Room> roomList = new ArrayList<>();
        for (JsonNode roomJson : body) {
            if (roomJson.toString().contains("{") && roomJson.toString().contains("}")) {
                System.out.println(roomJson);
                String roomAsString = mapper.writeValueAsString(roomJson);
                Room room = mapper.readValue(roomAsString, Room.class);
                roomList.add(room);
            }
        }

        ObservableList<Room> data = FXCollections.observableArrayList(roomList);
        buildingIdCol.setCellValueFactory(new PropertyValueFactory<>("building"));
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        onlyStaffCol.setCellValueFactory(new PropertyValueFactory<>("onlyStaff"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        viewCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        viewCol.setCellFactory(param -> returnCell("View"));
        roomTable.setItems(data);
        roomTable.getColumns().addAll(roomIdCol, buildingIdCol, capacityCol, onlyStaffCol, nameCol, deleteCol, updateCol, viewCol);
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Room, Room> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);

                if (room == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editRoom(room);
                                    break;
                                case "Delete":
                                    deleteRoom(room);
                                    break;
                                case "View":
                                    viewRoom(room);
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
     * Sends user to the room create page.
     */

    public void createRoom() throws IOException {
        switchFunc("/admin/room/RoomCreate.fxml");
    }
    /**
     * Deletes selected room.
     */

    public void deleteRoom(Room room) throws IOException {
        String id = Long.toString(room.getId());
        ServerCommunication.removeRoom(id);
//        createAlert("removed: " + room.getName());
        loadRooms();
    }
    /**
     * Sends user to the room edit page.
     */

    public void editRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/admin/room/RoomEdit.fxml");
        RoomEditController controller = loader.getController();
        controller.initData(room);
    }
    /**
     * Sends user to the room view page.
     */

    public void viewRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/admin/room/RoomView.fxml");
        RoomViewController controller = loader.getController();
        controller.initData(room);
    }
    /**
     * Gets a list of buildings.
     */

    public List<Building> getBuildings(String buildings) throws JsonProcessingException {
        System.out.println(buildings);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        return Arrays.asList(list);
    }
    /**
     * Gets a list of building names.
     */

    public List<String> getBuildingNames(List<Building> buildings) {
        List<String> a = new ArrayList<>();
        for (Building building : buildings) {
            this.buildingsByName.put(building.getName(), building.getId());
            this.buildingsById.put(building.getId(), building.getName());
            a.add(building.getName());
        }
        return a;
    }
    /**
     * Gets a list of facilities and their values.
     */

    public List<Facility> getFacility(String facilities) throws JsonProcessingException {
        ArrayNode body = (ArrayNode) mapper.readTree(facilities).get("body");
        facilities = mapper.writeValueAsString(body);
        Facility[] list = mapper.readValue(facilities, Facility[].class);
        return Arrays.asList(list);
    }
    /**
     * Gets a list of facility names.
     */

    public List<String> getFacilityNames(List<Facility> facilities) {
        List<String> a = new ArrayList<>();
        for (Facility facility : facilities) {
            this.facilitiesByName.put(facility.getDescription(), facility.getId());
            this.facilitiesById.put(facility.getId(), facility.getDescription());
            a.add(facility.getDescription());
        }
        return a;
    }
    /**
     * Gets rooms only accesible to staff.
     */

    public String getOnlyStaff(Room room) {
        return room.isOnlyStaff() ? "Only staff" : "All users";
    }

    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }
}

