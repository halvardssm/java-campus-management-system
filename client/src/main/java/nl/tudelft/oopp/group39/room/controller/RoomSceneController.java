package nl.tudelft.oopp.group39.room.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.facility.model.Facility;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class RoomSceneController extends AbstractSceneController {

    private Building building;
    private boolean filterBarShown = false;
    private boolean filtered = false;
    private VBox filterBarTemplate;
    private List<CheckBox> facilityBoxes = new ArrayList<>();
    private int maxCapacity;
    private List<Facility> facilities = new ArrayList<>();
    private List<Integer> selectedFacilities = new ArrayList<>();
    private int selectedCapacity = 0;
    private ToggleGroup availability;
    private String selectedAvailability = "none";

    @FXML
    public TextField roomBuildingIdField;

    @FXML
    public TextField roomCapacityField;

    @FXML
    public TextField roomDescriptionField;

    @FXML
    public TextField updateRoomField;

    @FXML
    private FlowPane rooms;

    @FXML
    private GridPane newRoom;

    @FXML
    private VBox buildingInfo;

    @FXML
    private VBox filterBar;

    @FXML
    private Slider capacityPicker;

    @FXML
    private VBox facilitiesPicker;

    @FXML
    private Label capacity;

    @FXML
    private Button filterBtn;

    @FXML
    private Hyperlink removeFilters;

    @FXML
    private VBox goBack;

    @FXML
    private Button backButton;
    @FXML
    private TextField searchField;
    @FXML
    private VBox availabilityPicker;


    /**
     * Sets up the page to show rooms for selected building.
     *
     * @param building where the rooms are located
     * @throws JsonProcessingException when there is a processing exception
     */
    public void setup(Building building) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.building = building;
        setBuildingDetails(building.getName(), building.getLocation());
        getRooms(building.getId());
        maxCapacity = building.getMaxCapacity();
        getFaclities();
    }

    /**
     * Sets up the page to show all rooms.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void setup() throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        getAllRooms();
        maxCapacity = getMaxCapacity();
        getFaclities();
        goBack.getChildren().remove(backButton);
    }

    /**
     * Finds the max capacity of all the rooms.
     *
     * @return int of max capacity of all the rooms
     * @throws JsonProcessingException when there is a processing exception
     */
    public int getMaxCapacity() throws JsonProcessingException {
        String roomString = ServerCommunication.get(ServerCommunication.room);
        ArrayNode body = (ArrayNode) mapper.readTree(roomString).get("body");
        roomString = mapper.writeValueAsString(body);
        Room[] roomList = mapper.readValue(roomString, Room[].class);
        int maxCap = roomList[0].getCapacity();
        for (Room room : roomList) {
            if (room.getCapacity() > maxCap) {
                maxCap = room.getCapacity();
            }
        }
        return maxCap;
    }

    /**
     * Searches the rooms.
     */
    public void searchRooms() {
        if (searchField.getText().equals("") || searchField.getText() == null) {
            if (building != null) {
                getRooms(building.getId());
            } else {
                getAllRooms();
            }
        } else {
            String request = "name=" + searchField.getText();
            if (building != null) {
                request = request + "&building=" + building.getId();
            }
            String json = ServerCommunication.getRooms(request);
            showRooms(json);
        }
    }

    /**
     * Creates a new room.
     * TODO
     */
    public void newRoom() {
        String buildingId = roomBuildingIdField.getText();

        String roomCapacity = roomCapacityField.getText();

        String roomDescription = roomDescriptionField.getText();

        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
    }

    /**
     * Doc. TODO Sven
     */
    public void updateRoom() {
        String buildingId = roomBuildingIdField.getText();

        String roomCap = roomCapacityField.getText();

        String roomDesc = roomDescriptionField.getText();

        String roomID = updateRoomField.getText();

        roomID = roomID.contentEquals("") ? "1" : roomID;

        String roomReservations = updateRoomField.getText();

        createAlert(ServerCommunication.updateRoom(
            buildingId,
            roomCap,
            roomDesc,
            roomID,
            roomReservations
        ));
    }

    /**
     * Doc. TODO Sven
     */
    public void deleteRoom() {
        String id = updateRoomField.getText();

        id = id.contentEquals("") ? "1" : id;

        ServerCommunication.removeRoom(id);

        createAlert(ServerCommunication.get(ServerCommunication.room));
    }

    /**
     * Shows the rooms.
     *
     * @param json json string that holds the rooms that needs to be parsed
     */
    public void showRooms(String json) {
        System.out.println(json);
        rooms.getChildren().clear();
        try {
            ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
            String roomAsString = mapper.writeValueAsString(body);

            Room[] list = mapper.readValue(roomAsString, Room[].class);
            if (list.length == 0) {
                rooms.getChildren().add(new Label("No Results Found."));
                return;
            }
            for (Room room : list) {
                FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/room/roomCell.fxml"));
                GridPane newRoom = loader.load();
                RoomCellController controller = loader.getController();
                controller.createPane(room, room.getBuildingObject());
                rooms.getChildren().add(newRoom);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }

    }

    /**
     * Gets rooms for a selected building.
     *
     * @param buildingId id of the selected building
     */
    public void getRooms(Long buildingId) {
        String roomString = ServerCommunication.getRooms("building=" + buildingId);
        showRooms(roomString);
    }

    /**
     * Gets all rooms.
     */
    public void getAllRooms() {
        String roomsString = ServerCommunication.get(ServerCommunication.room);
        showRooms(roomsString);
    }

    /**
     * Sets the building details in the header of the page.
     *
     * @param name    name of the building
     * @param address address of the building
     */
    public void setBuildingDetails(String name, String address) {
        Label buildingName = new Label(name);
        buildingName.getStyleClass().add("buildingName");
        Label buildingAddress = new Label(address);
        buildingAddress.getStyleClass().add("buildingAddress");
        buildingInfo.getChildren().addAll(buildingName, buildingAddress);
    }

    /**
     * Toggles the filter bar.
     *
     * @throws IOException when there is an io exception
     */
    public void toggleFilterBar() throws IOException {
        if (!filterBarShown) {
            filterBarTemplate = FXMLLoader.load(getClass().getResource("/room/roomFilterBar.fxml"));
            capacityPicker = (Slider) filterBarTemplate.lookup("#capacityPicker");
            setCapacityPicker(capacityPicker, maxCapacity);
            capacityPicker.valueProperty().addListener((ov, oldVal, newVal) -> {
                capacity = (Label) filterBarTemplate.lookup("#capacity");
                capacity.setText("Capacity: " + (int) capacityPicker.getValue());
                checkFiltersSelected();
            });
            createFacilityBoxes();
            if (filtered) {
                addRemoveFilters();
                capacityPicker.setValue(selectedCapacity);
                for (int selectedFacility : selectedFacilities) {
                    CheckBox facilityBox =
                        (CheckBox) filterBarTemplate.lookup("#" + selectedFacility);
                    facilityBox.setSelected(true);
                }
            }
            createStaffOnly();
            filterBar.getChildren().add(filterBarTemplate);
            filterBarShown = true;
        } else {
            filterBar.getChildren().clear();
            filterBarShown = false;
        }
    }

    /**
     * Retrieves all facilities.
     *
     * @throws JsonProcessingException when there is a processing exception
     */
    public void getFaclities() throws JsonProcessingException {
        String facilitiesString = ServerCommunication.get(ServerCommunication.facility);
        ArrayNode body = (ArrayNode) mapper.readTree(facilitiesString).get("body");
        for (JsonNode facilityNode : body) {
            String facilityAsString = mapper.writeValueAsString(facilityNode);
            Facility facility = mapper.readValue(facilityAsString, Facility.class);
            facilities.add(facility);
        }
    }

    /**
     * Creates a CheckBox for each facility.
     */
    public void createFacilityBoxes() {
        facilityBoxes.clear();
        for (Facility facility : facilities) {
            CheckBox facilityBox = new CheckBox(facility.getDescription());
            facilityBox.setId(String.valueOf(facility.getId()));
            System.out.println(facilityBox.getId());
            facilityBox.setOnAction(event -> checkFiltersSelected());
            facilityBoxes.add(facilityBox);
        }
        facilitiesPicker = (VBox) filterBarTemplate.lookup("#facilitiesPicker");
        facilitiesPicker.getChildren().clear();
        facilitiesPicker.getChildren().addAll(facilityBoxes);
    }

    /**
     * Creates RadioButtons for choosing availability of rooms.
     */
    public void createStaffOnly() {
        availabilityPicker = (VBox) filterBarTemplate.lookup("#availabilityPicker");
        availability = new ToggleGroup();
        RadioButton staffOnly = new RadioButton("Staff only");
        staffOnly.setId("true");
        staffOnly.setOnAction(event -> checkFiltersSelected());
        RadioButton everyone = new RadioButton("Students and staff");
        everyone.setId("false");
        everyone.setOnAction(event -> checkFiltersSelected());
        RadioButton showAll = new RadioButton("Show all");
        showAll.setId("none");
        showAll.setOnAction(event -> checkFiltersSelected());
        staffOnly.setToggleGroup(availability);
        everyone.setToggleGroup(availability);
        showAll.setToggleGroup(availability);
        if (!filtered) {
            showAll.setSelected(true);
        } else {
            if (selectedAvailability.equals("true")) {
                staffOnly.setSelected(true);
            } else if (selectedAvailability.equals("false")) {
                everyone.setSelected(true);
            } else {
                showAll.setSelected(true);
            }
        }

        availabilityPicker.getChildren().addAll(staffOnly, everyone, showAll);
    }

    /**
     * Filters the rooms and shows result.
     */
    public void filterRooms() {
        StringBuilder faciltiesString = new StringBuilder();
        selectedFacilities.clear();
        for (CheckBox facilityBox : facilityBoxes) {
            if (facilityBox.isSelected()) {
                selectedFacilities.add(Integer.parseInt(facilityBox.getId()));
            }
        }
        for (int i = 0; i < selectedFacilities.size(); i++) {
            if (i == selectedFacilities.size() - 1) {
                faciltiesString.append(selectedFacilities.get(i));
            } else {
                faciltiesString.append(selectedFacilities.get(i)).append(",");
            }
        }
        int capacity = (int) capacityPicker.getValue();
        String request = "capacity=" + capacity;
        if (!faciltiesString.toString().equals("")) {
            request = request + "&facilities=" + faciltiesString;
        }
        RadioButton selected = (RadioButton) availability.getSelectedToggle();
        String onlyStaff = selected.getId();
        if (!onlyStaff.equals("none")) {
            request = request + "&onlyStaff=" + onlyStaff;
        }
        if (building != null) {
            request = request + "&building=" + building.getId();
        }
        String json = ServerCommunication.getRooms(request);
        showRooms(json);
        selectedCapacity = capacity;
        selectedAvailability = onlyStaff;
        filtered = true;
        addRemoveFilters();
    }

    /**
     * Checks if there are any filters selected.
     */
    public void checkFiltersSelected() {
        boolean facilitySelected = false;
        for (CheckBox facilityBox : facilityBoxes) {
            if (facilityBox.isSelected()) {
                facilitySelected = true;
            }
        }
        RadioButton selected = (RadioButton) availability.getSelectedToggle();
        if ((int) capacityPicker.getValue() != 0
            || facilitySelected
            || !selected.getId().equals(selectedAvailability)
        ) {
            filterBtn = (Button) filterBarTemplate.lookup("#filterBtn");
            filterBtn.setDisable(false);
            filterBtn.setOnAction(event -> filterRooms());
        } else {
            filterBtn.setDisable(true);
        }
    }

    /**
     * Adds the remove filters button.
     */
    public void addRemoveFilters() {
        removeFilters = (Hyperlink) filterBarTemplate.lookup("#removeFilters");
        removeFilters.setText("Remove filters");
        removeFilters.setOnAction(event -> {
            if (building != null) {
                getRooms(building.getId());
            } else {
                getAllRooms();
            }
            filtered = false;
            removeFilters.setText(null);
            removeFilters.setVisited(false);
            capacityPicker.setValue(0);
            for (CheckBox facilityBox : facilityBoxes) {
                facilityBox.setSelected(false);
            }
            checkFiltersSelected();
            try {
                toggleFilterBar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
