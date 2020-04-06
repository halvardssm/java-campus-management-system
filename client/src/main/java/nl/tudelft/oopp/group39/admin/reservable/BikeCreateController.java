package nl.tudelft.oopp.group39.admin.reservable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class BikeCreateController extends BikeListController {

    private String building;
    private String rentalDuration;
    private String bikeType;
    private String priceFirst;
    private String priceSecond;
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private HashMap<String, Long> buildingIdsByName = new HashMap<>();
    @FXML
    private Button backbtn;
    @FXML
    private ComboBox<String> buildingBox;
    @FXML
    private TextField priceFieldFirst;
    @FXML
    private TextField priceFieldSecond;
    @FXML
    private ComboBox<String> bikeTypeField;
    @FXML
    private TextField rentalDurationField;
    @FXML
    private MenuBar navBar;


    /**
     * Initializes the data of a User and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */

    public void initData() throws JsonProcessingException {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
        ObservableList<String> data = initBuildings();
        buildingBox.setItems(data);
        this.building = data.get(0);
        buildingBox.getSelectionModel().select(building);
        String price = Double.toString(0.0);
        System.out.println(price);
        String[] priceArray = price.split("\\.");
        this.priceFirst = priceArray[0];
        this.priceSecond = priceArray[1];
        priceFieldFirst.setPromptText(priceFirst);
        priceFieldSecond.setPromptText(priceSecond);
        ObservableList<String> types = initTypes();
        this.bikeType = types.get(0);
        bikeTypeField.setItems(types);
        bikeTypeField.getSelectionModel().select(bikeType);
        this.rentalDuration = "";
        rentalDurationField.setPromptText(rentalDuration);
    }

    /**
     * Initializes the data of all buildings and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */
    public ObservableList<String> initBuildings() throws JsonProcessingException {
        String buildings = ServerCommunication.get(ServerCommunication.building);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        List<String> buildingNames = new ArrayList<>();
        for (Building building : list) {
            buildingNames.add(building.getName());
            buildingIdsByName.put(building.getName(), Long.valueOf(building.getId()));
        }
        return FXCollections.observableArrayList(buildingNames);
    }

    /**
     * Initializes the data of all types and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */
    public ObservableList<String> initTypes() throws JsonProcessingException {
        String types = ServerCommunication.getBikeTypes();
        ArrayNode body = (ArrayNode) mapper.readTree(types).get("body");
        types = mapper.writeValueAsString(body);
        String[] list = mapper.readValue(types, String[].class);
        return FXCollections.observableArrayList(list);
    }

    /**
     * Goes back to main User panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchBikeView(currentStage);
    }
    /**
     * Edits user values and sends them to database.
     */

    public void addBike() throws IOException {
        Object typeObj = bikeTypeField.getValue();
        String rentalDurationInput = rentalDurationField.getText();
        rentalDurationInput = rentalDurationInput.contentEquals("") ? rentalDuration : rentalDurationInput;
        String typeInput = typeObj == null ? bikeType : typeObj.toString();
        Object buildingObj = buildingBox.getValue();
        Long buildingInput = buildingObj == null ?  buildingIdsByName.get(building) :
            buildingIdsByName.get(buildingObj.toString());
        String priceInputFirst = priceFieldFirst.getText();
        priceInputFirst = priceInputFirst.contentEquals("") ? priceFirst : priceInputFirst;
        String priceInputSecond = priceFieldFirst.getText();
        priceInputSecond = priceInputSecond.contentEquals("") ? priceSecond : priceInputSecond;
        Double priceInput = getPrice(priceInputFirst, priceInputSecond);
        Bike newBike = new Bike(-1L,priceInput,buildingInput,typeInput,rentalDurationInput);
        ServerCommunication.addBike(newBike);
        getBack();
    }

    public Double getPrice(String first, String second) {
        return Double.valueOf(first + "." + second);
    }

}
