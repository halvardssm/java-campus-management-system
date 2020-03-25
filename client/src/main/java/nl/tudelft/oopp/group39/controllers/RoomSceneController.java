package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.models.Facility;
import nl.tudelft.oopp.group39.models.Room;
import nl.tudelft.oopp.group39.views.UsersDisplay;

public class RoomSceneController extends MainSceneController {

    private Building building;
    private boolean filterBarShown = false;
    private boolean filtered = false;
    private VBox filterBarTemplate;
    private List<CheckBox> facilityBoxes = new ArrayList<>();
    private int maxCapacity;
    private List<Facility> facilities = new ArrayList<>();
    private List<Integer> selectedFacilities = new ArrayList<>();
    private int selectedCapacity = 0;

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

    /**
     * Sets up the page to show rooms for selected building.
     *
     * @param building where the rooms are located
     */
    public void setup(Building building) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.building = building;
        setBuildingDetails(building.getName(), building.getDescription());
        getRooms(building.getId());
        maxCapacity = building.getMaxCapacity();
        getFaclities();
    }

    public void setup() throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        getAllRooms();
        maxCapacity = getMaxCapacity();
        getFaclities();
        goBack.getChildren().remove(backButton);
    }

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
     * Doc. TODO Sven
     */
    public void newRoomButton() {
        String buildingId = roomBuildingIdField.getText();

        String roomCapacity = roomCapacityField.getText();

        String roomDescription = roomDescriptionField.getText();

        createAlert(ServerCommunication.addRoom(buildingId, roomCapacity, roomDescription));
    }

    /**
     * Doc. TODO Sven
     */
    public void updateRoomButton() {
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
    public void getRoomsButton() {
        createAlert(ServerCommunication.get(ServerCommunication.room));
    }

    /**
     * Doc. TODO Sven
     */
    public void deleteRoomButton(ActionEvent actionEvent) {
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
            if (body.isEmpty()) {
                Label label = new Label("No results found");
                rooms.getChildren().add(label);
            } else {
                for (JsonNode roomJson : body) {
                    String roomAsString = mapper.writeValueAsString(roomJson);
                    Room room = mapper.readValue(roomAsString, Room.class);
                    newRoom = FXMLLoader.load(getClass().getResource("/roomCell.fxml"));
                    newRoom.setOnMouseClicked(e -> {
                        try {
                            goToReservationScene(room, room.getBuildingObject(), UsersDisplay.window.getScene());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    });
                    Label name = (Label) newRoom.lookup("#roomname");
                    name.setText(room.getName());
                    String roomDetails = room.getDescription()
                        + "\n" + "Capacity: " + room.getCapacity()
                        + "\n" + "Facilities: " + room.facilitiesToString();

                    Label details = (Label) newRoom.lookup("#roomdetails");
                    details.setText(roomDetails);
                    rooms.getChildren().add(newRoom);
                }
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
    public void getRooms(long buildingId) {
        String roomString = ServerCommunication.getRooms(buildingId);
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

    public void toggleFilterBar() throws IOException {
        if (!filterBarShown) {
            filterBarTemplate = FXMLLoader.load(getClass().getResource("/roomFilterBar.fxml"));
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
                    CheckBox facilityBox = (CheckBox) filterBarTemplate.lookup("#" + selectedFacility);
                    facilityBox.setSelected(true);
                }
            }
            filterBar.getChildren().add(filterBarTemplate);
            filterBarShown = true;
        } else {
            filterBar.getChildren().clear();
            filterBarShown = false;
        }
    }

    public void getFaclities() throws JsonProcessingException {
        String facilitiesString = ServerCommunication.get(ServerCommunication.facility);
        ArrayNode body = (ArrayNode) mapper.readTree(facilitiesString).get("body");
        for (JsonNode facilityNode : body) {
            String facilityAsString = mapper.writeValueAsString(facilityNode);
            Facility facility = mapper.readValue(facilityAsString, Facility.class);
            facilities.add(facility);
        }
    }

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

    public void filterRooms() {
        int capacity = (int) capacityPicker.getValue();
        String faciltiesString = "";
        selectedFacilities.clear();
        for (CheckBox facilityBox : facilityBoxes) {
            if (facilityBox.isSelected()) {
                selectedFacilities.add(Integer.parseInt(facilityBox.getId()));
            }
        }
        for (int i = 0; i < selectedFacilities.size(); i++) {
            if (i == selectedFacilities.size() - 1) {
                faciltiesString = faciltiesString + selectedFacilities.get(i);
            } else {
                faciltiesString = faciltiesString + selectedFacilities.get(i) + ",";
            }
        }
        System.out.println(faciltiesString);
        String request = "capacity=" + capacity;
        if (!faciltiesString.equals("")) {
            request = request + "&facilities=" + faciltiesString;
        }
        if (building != null) {
            request = request + "&buildingId=" + building.getId();
        }
        String json = ServerCommunication.getRooms(request);
        showRooms(json);
        selectedCapacity = capacity;
        filtered = true;
        addRemoveFilters();
    }

    public void checkFiltersSelected() {
        boolean facilitySelected = false;
        for (CheckBox facilityBox : facilityBoxes) {
            if (facilityBox.isSelected()) {
                facilitySelected = true;
            }
        }
        if ((int) capacityPicker.getValue() != 0 || facilitySelected) {
            filterBtn = (Button) filterBarTemplate.lookup("#filterBtn");
            filterBtn.setDisable(false);
            filterBtn.setOnAction(event -> {
                filterRooms();
            });
        } else {
            filterBtn.setDisable(true);
        }
    }

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
