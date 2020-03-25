package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Building;

public class BuildingSceneController extends MainSceneController implements Initializable {

    private boolean filterBarShown;
    private int selectedCapacity = 0;
    private String selectedOpenTime;
    private String selectedCloseTime;
    private boolean filtered = false;

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    @FXML
    private VBox filterBar;

    @FXML
    private VBox filterBarTemplate;

    @FXML
    private Slider capacityPicker;

    @FXML
    private ComboBox<String> timeOpenPicker;

    @FXML
    private ComboBox<String> timeClosedPicker;

    @FXML
    private Label capacity;

    @FXML
    private Button filterBtn;

    @FXML
    private Hyperlink removeFilters;
    @FXML
    private TextField searchField;
    @FXML
    private GridPane search;

    /**
     * Retrieves buildings from the server and shows them.
     */
    public void showBuildings(String json, int capacity) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        flowPane.getChildren().clear();
        try {
            ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
            if (body.isEmpty()) {
                Label label = new Label("No results found");
                flowPane.getChildren().add(label);
            } else {
                String buildingString = mapper.writeValueAsString(body);
                Building[] buildingsArray = mapper.readValue(buildingString, Building[].class);
                for (Building building : buildingsArray) {
                    if (building.getMaxCapacity() >= capacity) {
                        newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));
                        newBuilding.setOnMouseClicked(e -> {
                            try {
                                goToRoomsScene(building);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        });

                        Label name = (Label) newBuilding.lookup("#bname");
                        name.setText(building.getName());

                        String newDetails = (building.getLocation()
                            + "\n" + building.getDescription()
                            + "\n" + "Max. Capacity: " + building.getMaxCapacity()
                            + "\n" + "Opening times: " + building.getOpen()
                            + " - " + building.getClosed());

                        Label details = (Label) newBuilding.lookup("#bdetails");
                        details.setText(newDetails);

                        flowPane.getChildren().add(newBuilding);
                    }
                }
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    public void getAllBuildings() {
        String buildingString = ServerCommunication.get(ServerCommunication.building);
        showBuildings(buildingString, 0);
    }

    public void toggleFilterBar() throws IOException {
        if (!filterBarShown) {
            filterBarTemplate = FXMLLoader.load(getClass().getResource("/buildingFilterBar.fxml"));
            capacityPicker = (Slider) filterBarTemplate.lookup("#capacityPicker");
            setCapacityPicker(capacityPicker, getMaxCapacity());
            capacityPicker.valueProperty().addListener((ov, oldVal, newVal) -> {
                capacity = (Label) filterBarTemplate.lookup("#capacity");
                capacity.setText("Max. Capacity: " + (int) capacityPicker.getValue());
                checkFiltersSelected();
            });
            setTimePickers();
            if (filtered) {
                addRemoveFilters();
                capacityPicker.setValue(selectedCapacity);
                timeOpenPicker.setValue(selectedOpenTime);
                timeClosedPicker.setValue(selectedCloseTime);
            }
            filterBar.getChildren().add(filterBarTemplate);
            filterBarShown = true;
        } else {
            filterBar.getChildren().clear();
            filterBarShown = false;
        }
    }

    public void checkFiltersSelected() {
        if ((int) capacityPicker.getValue() != 0
            || !timeOpenPicker.getSelectionModel().isEmpty()
            || !timeClosedPicker.getSelectionModel().isEmpty()
        ) {
            filterBtn = (Button) filterBarTemplate.lookup("#filterBtn");
            filterBtn.setDisable(false);
            filterBtn.setOnAction(event -> {
                filterBuildings();
            });
        } else {
            filterBtn.setDisable(true);
        }
    }

    public int getMaxCapacity() throws JsonProcessingException {
        String json = ServerCommunication.get(ServerCommunication.building);
        ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
        String buildingString = mapper.writeValueAsString(body);
        Building[] buildingsArray = mapper.readValue(buildingString, Building[].class);
        int maxCapacity = buildingsArray[0].getMaxCapacity();
        for (Building building : buildingsArray) {
            if (building.getMaxCapacity() > maxCapacity) {
                maxCapacity = building.getMaxCapacity();
            }
        }
        return maxCapacity;
    }

    public void setTimePickers() {
        timeOpenPicker = (ComboBox<String>) filterBarTemplate.lookup("#timeOpenPicker");
        timeClosedPicker = (ComboBox<String>) filterBarTemplate.lookup("#timeClosedPicker");
        timeOpenPicker.getItems().clear();
        timeClosedPicker.getItems().clear();
        timeOpenPicker.setOnAction(event -> checkFiltersSelected());
        timeClosedPicker.setOnAction(event -> checkFiltersSelected());
        for (int i = 7; i < 12; i++) {
            if (i < 10) {
                timeOpenPicker.getItems().add("0" + i + ":00");
            } else {
                timeOpenPicker.getItems().add(i + ":00");
            }
        }
        for (int j = 18; j < 24; j++) {
            timeClosedPicker.getItems().add(j + ":00");
        }
    }

    public void filterBuildings() {
        String request = "";
        int capacity = (int) capacityPicker.getValue();
        selectedCapacity = capacity;
        if (!timeOpenPicker.getSelectionModel().isEmpty()) {
            String open = timeOpenPicker.getValue() + ":00";
            selectedOpenTime = timeOpenPicker.getValue();
            request = request + "open=" + open;
        }
        if (!timeClosedPicker.getSelectionModel().isEmpty()) {
            String closed = timeClosedPicker.getValue() + ":00";
            selectedCloseTime = timeClosedPicker.getValue();
            request = request + "&closed=" + closed;
        }
        String json = ServerCommunication.getFilteredBuildings(request);
        System.out.println(json);
        showBuildings(json, capacity);
        filtered = true;
        addRemoveFilters();
    }

    public void addRemoveFilters() {
        removeFilters = (Hyperlink) filterBarTemplate.lookup("#removeFilters");
        removeFilters.setText("Remove filters");
        removeFilters.setOnAction(event -> {
            getAllBuildings();
            removeFilters.setText(null);
            removeFilters.setVisited(false);
            filtered = false;
            capacityPicker.setValue(0);
            timeClosedPicker.getSelectionModel().clearSelection();
            timeOpenPicker.getSelectionModel().clearSelection();
            checkFiltersSelected();
            try {
                toggleFilterBar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void searchBuildings() {
        if (searchField.getText() != null) {
            String request = "name=" + searchField.getText();
            String json = ServerCommunication.getFilteredBuildings(request);
            showBuildings(json, 0);
        }
    }

    /**
     * Doc. TODO Sven
     */
    public void alertAllBuildings() {
        try {
            createAlert("Users shown.", ServerCommunication.get(ServerCommunication.building));
        } catch (Exception e) {
            createAlert("Error Occurred.");
        }
    }

    /**
     * Retrieves the buildings when page is loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllBuildings();
    }


}
