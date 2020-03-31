package nl.tudelft.oopp.group39.controllers.Admin.Room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.AdminPanelController;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Facility;
import nl.tudelft.oopp.group39.models.Room;

public class RoomListController extends AdminPanelController implements Initializable {
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
    private HashMap<String, Integer> facilitiesByName = new HashMap();
    private HashMap<Integer, String> facilitiesById = new HashMap();
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

    /**
     * Initialize data into tableView.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadRooms();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }

    public ObservableList<String> getData(String b) throws JsonProcessingException {
        List<Building> buildings = getBuildings(b);
        List<String> buildingNames = getBuildingNames(buildings);
        return FXCollections.observableArrayList(buildingNames);
    }

    public ObservableList<String> getFacilityData(String b) throws JsonProcessingException {
        List<Facility> facilities = getFacility(b);
        List<String> facilityNames = getFacilityNames(facilities);
        return FXCollections.observableArrayList(facilityNames);
    }
    /**
     * Display rooms and data in tableView buildingTable. -- Likely doesn't work yet. Need to add nameCol.
     */
    void loadRooms() throws JsonProcessingException {
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> nData = getData(b);
        String f = ServerCommunication.get(ServerCommunication.facility);
        System.out.println("tset: " + f);
        ObservableList<String> fData = getFacilityData(f);
        List<String> options = new ArrayList<>();
        options.add("All users"); options.add("Only staff members");
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        roomBuildingIdField.setItems(nData);
        roomBuildingIdField.setPromptText(nData.get(0));
        roomOnlyStaffField.setItems(dataOptions);
        facilitiesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        facilitiesList.setItems(fData);
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

    public TableCell<Room, Room> returnCell(String button) {
        return new TableCell<Room, Room>() {
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
                            switch(button){
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

    public void createRoom() throws IOException {
        switchFunc("/Admin/Room/RoomCreate.fxml");
    }

    public void deleteRoom(Room room) throws IOException {
        String id = Integer.toString((int) room.getId());
        ServerCommunication.removeRoom(id);
        createAlert("removed: " + room.getName());
        loadRooms();
    }

    public void editRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/Room/RoomEdit.fxml");
        RoomEditController controller = loader.getController();
        controller.initData(room);
    }

    public void viewRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/Room/RoomView.fxml");
        RoomViewController controller = loader.getController();
        controller.initData(room);
    }

    public List<Building> getBuildings(String buildings) throws JsonProcessingException {
        System.out.println(buildings);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        return Arrays.asList(list);
    }

    public List<String> getBuildingNames(List<Building> buildings) {
        List<String> a = new ArrayList<>();
        for(Building building : buildings) {
            this.buildingsByName.put(building.getName(), building.getId());
            this.buildingsById.put(building.getId(), building.getName());
            a.add(building.getName());
        }
        return a;
    }

    public List<Facility> getFacility(String facilities) throws JsonProcessingException {
        ArrayNode body = (ArrayNode) mapper.readTree(facilities).get("body");
        facilities = mapper.writeValueAsString(body);
        Facility[] list = mapper.readValue(facilities, Facility[].class);
        return Arrays.asList(list);
    }

    public List<String> getFacilityNames(List<Facility> facilities) {
        List<String> a = new ArrayList<>();
        for(Facility facility : facilities) {
            this.facilitiesByName.put(facility.getDescription(), (int) facility.getId());
            this.facilitiesById.put((int) facility.getId(), facility.getDescription());
            a.add(facility.getDescription());
            System.out.println("------------news: " + facility.getDescription());
        }
        return a;
    }

    public String getOnlyStaff(Room room) {
        return room.isOnlyStaff() ? "Only staff" :"All users";
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

