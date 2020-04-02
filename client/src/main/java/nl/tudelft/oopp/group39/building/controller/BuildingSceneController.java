package nl.tudelft.oopp.group39.building.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
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
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.building.model.BuildingCapacityComparator;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class BuildingSceneController extends AbstractSceneController implements Initializable {

    private boolean filterBarShown;
    private int selectedCapacity = 0;
    private String selectedOpenTime;
    private String selectedCloseTime;
    private boolean filtered = false;

    @FXML
    private FlowPane flowPane;

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
    public void showBuildings(String json) {
        flowPane.getChildren().clear();
        try {
            System.out.println(json);

            ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");

            json = mapper.writeValueAsString(body);
            Building[] list = mapper.readValue(json, Building[].class);
            if (list.length == 0) {
                flowPane.getChildren().add(new Label("No Results Found."));
                return;
            }
            FXMLLoader loader;

            for (Building building : list) {
                if (building.getMaxCapacity() < selectedCapacity) {
                    continue;
                }

                loader = new FXMLLoader(getClass()
                    .getResource("/building/buildingCell.fxml"));
                GridPane newBuilding = loader.load();
                BuildingCellController controller = loader.getController();
                controller.createPane(building);

                flowPane.getChildren().add(newBuilding);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    /**
     * Gets all the buildings from the server.
     */
    public void getAllBuildings() {
        showBuildings(ServerCommunication.get(ServerCommunication.building));
    }

    /**
     * Toggles the filter bar.
     *
     * @throws IOException when there is an IO exception
     */
    public void toggleFilterBar() throws IOException {
        if (!filterBarShown) {
            filterBarTemplate = FXMLLoader.load(
                getClass().getResource("/building/buildingFilterBar.fxml")
            );
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

    /**
     * Checks if there are any filters selected.
     */
    public void checkFiltersSelected() {
        if ((int) capacityPicker.getValue() != 0
            || !timeOpenPicker.getSelectionModel().isEmpty()
            || !timeClosedPicker.getSelectionModel().isEmpty()
        ) {
            filterBtn = (Button) filterBarTemplate.lookup("#filterBtn");
            filterBtn.setDisable(false);
            filterBtn.setOnAction(event -> filterBuildings());
        } else {
            filterBtn.setDisable(true);
        }
    }

    /**
     * Find the max capacity of all buildings.
     *
     * @return int of the max capacity of all buildings
     * @throws JsonProcessingException when there is a processing exception
     */
    public int getMaxCapacity() throws JsonProcessingException {
        String json = ServerCommunication.get(ServerCommunication.building);
        ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
        String buildingString = mapper.writeValueAsString(body);
        Building[] buildingsArray = mapper.readValue(buildingString, Building[].class);

        return Collections.max(Arrays.asList(buildingsArray), new BuildingCapacityComparator())
            .getMaxCapacity();
    }

    /**
     * Sets the comboboxes to pick closing and opening time for filtering.
     */
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

    /**
     * Filters the buildings and shows results.
     */
    public void filterBuildings() {
        String request = "";
        selectedCapacity = ((Double) capacityPicker.getValue()).intValue();
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
        showBuildings(json);
        filtered = true;
        addRemoveFilters();
    }

    /**
     * Adds the remove filters button to the filter bar.
     */
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
            selectedCapacity = 0;
            getAllBuildings();
            try {
                toggleFilterBar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Searches the buildings.
     */
    public void searchBuildings() {
        if (searchField.getText() != null) {
            String request = "name=" + searchField.getText();
            String json = ServerCommunication.getFilteredBuildings(request);
            showBuildings(json);
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
