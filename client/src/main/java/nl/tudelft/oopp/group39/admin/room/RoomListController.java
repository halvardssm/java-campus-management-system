package nl.tudelft.oopp.group39.admin.room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.AdminPanelController;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.facility.model.Facility;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

@SuppressWarnings("unchecked, MismatchedQueryAndUpdateOfCollection")
public class RoomListController extends AdminPanelController {
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
    private HashMap<String, Long> buildingsByName = new HashMap<>();
    private HashMap<Long, String> buildingsById = new HashMap<>();
    private HashMap<String, Long> facilitiesByName = new HashMap<>();
    private HashMap<Long, String> facilitiesById = new HashMap<>();
    @FXML
    private ComboBox<String> buildingFilter;
    @FXML
    private ComboBox<String> onlyStaffFilter;
    @FXML
    private ListView<String> facilitiesList;
    @FXML
    public TextField nameFilter;
    @FXML
    public TextField descriptionFilter;
    @FXML
    private MenuBar navBar;

    /**
     * Initialize function.
     */
    public void customInit() {
        try {
            loadAllRooms();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Gets the names of buildings.
     * @param b building
     * @return A list of building names
     * @throws JsonProcessingException when there is a processing exception
     */
    public ObservableList<String> getData(String b) throws JsonProcessingException {
        List<Building> buildings = getBuildings(b);
        List<String> buildingNames = new ArrayList<>();
        buildingNames.add("All buildings");
        buildingNames.addAll(getBuildingNames(buildings));
        return FXCollections.observableArrayList(buildingNames);
    }

    /**
     * Gets only the names of facilities.
     *
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
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    public void loadAllRooms() throws JsonProcessingException {
        facilitiesList.getSelectionModel().clearSelection();
        buildingFilter.getSelectionModel().clearAndSelect(0);
        nameFilter.clear();
        descriptionFilter.clear();
        onlyStaffFilter.getSelectionModel().clearAndSelect(0);
        String f = ServerCommunication.get(ServerCommunication.facility);
        String rooms = ServerCommunication.get(ServerCommunication.room);
        loadRooms(rooms);
        loadFiltering(f);
    }

    /**
     * Loads up data needed to set up filtering.
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    public void loadFiltering(String f) throws JsonProcessingException {
        List<String> options = new ArrayList<>();
        options.add("All users");
        options.add("Only staff members");
        ObservableList<String> dataOptions = FXCollections.observableArrayList(options);
        String b = ServerCommunication.get(ServerCommunication.building);
        ObservableList<String> buildingData = getData(b);
        buildingFilter.setItems(buildingData);
        buildingFilter.setPromptText(buildingData.get(0));
        onlyStaffFilter.setItems(dataOptions);
        facilitiesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<String> facData = getFacilityData(f);
        facilitiesList.setItems(facData);
        onlyStaffFilter.setPromptText(dataOptions.get(0));
    }

    /**
     * Loads the values of rooms and puts them into tableView.
     *
     * @throws JsonProcessingException when there is a processing exception.
     */
    void loadRooms(String rooms) throws JsonProcessingException {
        roomTable.setVisible(true);
        roomTable.getItems().clear();
        roomTable.getColumns().clear();

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
        ObservableList<Room> data = FXCollections.observableArrayList(roomList);
        roomTable.setItems(data);
        roomTable.getColumns().addAll(
             roomIdCol,
              buildingIdCol,
              capacityCol,
              onlyStaffCol,
              nameCol,
              deleteCol, updateCol, viewCol);
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
     *
     * @throws IOException if an error occurs during loading
     */
    public void createRoom() throws IOException {
        FXMLLoader loader = switchFunc("/admin/room/RoomCreate.fxml");
        RoomCreateController controller = loader.getController();
        controller.customInit();
    }

    /**
     * Deletes selected room.
     *
     * @throws IOException if an error occurs during loading
     */
    public void deleteRoom(Room room) throws IOException {
        String id = Long.toString(room.getId());
        ServerCommunication.removeRoom(id);
        loadAllRooms();
    }

    /**
     * Sends user to the room edit page.
     *
     * @throws IOException if an error occurs during loading
     */
    public void editRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/admin/room/RoomEdit.fxml");
        RoomEditController controller = loader.getController();
        controller.initData(room);
    }

    /**
     * Sends user to the room view page.
     *
     * @throws IOException if an error occurs during loading
     */
    public void viewRoom(Room room) throws IOException {
        FXMLLoader loader = switchFunc("/admin/room/RoomView.fxml");
        RoomViewController controller = loader.getController();
        controller.initData(room);
    }

    /**
     * Gets a list of buildings.
     *
     * @throws JsonProcessingException when there is a processing exception
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
     *
     * @throws JsonProcessingException when there is a processing exception
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
     * Gets rooms only accessible to staff.
     */
    public String getOnlyStaff(Room room) {
        return room.isOnlyStaff() ? "Only staff" : "All users";
    }

    public String isOnlyStaff(String string) {
        return string.contentEquals("Only staff") ? Boolean.toString(true) : Boolean.toString(
                false);
    }

    /**
     * Goes back to the admin panel.
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

    /**
     * Filters the rooms.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    @FXML
    private void filterRooms() throws JsonProcessingException {
        String filters = "";
        String name = nameFilter.getText();
        Object onlyStaffObj = onlyStaffFilter.getValue();
        String onlyStaff = onlyStaffObj == null ? "" : onlyStaffObj.toString();
        onlyStaff = onlyStaff.contentEquals("All users") ? "" : isOnlyStaff(onlyStaff);
        Object buildingObj = buildingFilter.getValue();
        String description = descriptionFilter.getText();
        String building = buildingObj == null ? "" : buildingObj.toString();
        building = building.contentEquals("All buildings") || building.contentEquals(
                "") ? "" : Long.toString(buildingsByName.get(building));
        filters = addToFilter("name", name, filters);
        filters = addToFilter("description", description, filters);
        filters = addToFilter("onlyStaff", onlyStaff, filters);
        filters = addToFilter("building", building, filters);
        List<String> facilitiesListed = new ArrayList<>();
        ObservableList<String> facilities = facilitiesList.getSelectionModel().getSelectedItems();
        for (String facilityDescription : facilities) {
            facilitiesListed.add(Long.toString(facilitiesByName.get(facilityDescription)));
        }
        StringBuilder faciltiesString = new StringBuilder();
        for (int i = 0; i < facilitiesListed.size(); i++) {
            if (i == facilitiesListed.size() - 1) {
                faciltiesString.append(facilitiesListed.get(i));
            } else {
                faciltiesString.append(facilitiesListed.get(i)).append(",");
            }
        }
        filters = addToFilter("facilities", faciltiesString.toString(), filters);
        String rooms = ServerCommunication.getRooms(filters);
        System.out.println(filters + "\n" + rooms);
        loadRooms(rooms);
    }

    /**
     * Used to add format data in a way so that it can be added to a filter.
     */
    public String addToFilter(String name, String input, String filters) {
        if (!input.contentEquals("")) {
            if (filters.contentEquals("")) {
                return name + "=" + input;
            }
            return filters + "&" + name + "=" + input;
        }
        return filters;
    }
}

