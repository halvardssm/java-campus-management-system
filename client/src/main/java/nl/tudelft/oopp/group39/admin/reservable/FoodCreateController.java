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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.models.Building;
import nl.tudelft.oopp.group39.reservable.model.Food;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class FoodCreateController extends FoodListController {

    private String building;
    private String description;
    private String name;
    private String priceFirst;
    private String priceSecond;
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Food food;
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
    private TextField nameField;
    @FXML
    private TextField descriptionField;

    /**
     * Initializes the data of a User and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */

    public void initData() throws JsonProcessingException {
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        ObservableList<String> data = initBuildings();
        buildingBox.setItems(data);
        this.building = data.get(0);
        buildingBox.getSelectionModel().select(building);
        String price = Double.toString(0.0);
        String[] priceArray = price.split("\\.");
        this.priceFirst = priceArray[0];
        this.priceSecond = priceArray[1];
        priceFieldFirst.setPromptText(priceFirst);
        priceFieldSecond.setPromptText(priceSecond);
        this.name = "";
        nameField.setPromptText("name");
        this.description = "";
        descriptionField.setPromptText("description");
    }

    public ObservableList<String> initBuildings() throws JsonProcessingException {
        String buildings = ServerCommunication.get(ServerCommunication.building);
        ArrayNode body = (ArrayNode) mapper.readTree(buildings).get("body");
        buildings = mapper.writeValueAsString(body);
        Building[] list = mapper.readValue(buildings, Building[].class);
        List<String> buildingNames = new ArrayList<>();
        for(Building building : list) {
            buildingNames.add(building.getName());
            buildingIdsByName.put(building.getName(), Long.valueOf(building.getId()));
        }
        return FXCollections.observableArrayList(buildingNames);
    }

    /**
     * Goes back to main User panel.
     */

    @FXML
    private void getBack() throws IOException {
        switchFoodView(currentStage);
    }
    /**
     * Edits user values and sends them to database.
     */

    public void createFoodItem() throws IOException {
        String nameInput = nameField.getText();
        nameInput = nameInput.contentEquals("") ? name : nameInput;
        String descriptionInput = descriptionField.getText();
        descriptionInput = descriptionInput.contentEquals("") ? description : descriptionInput;
        Object buildingObj = buildingBox.getValue();
        Long buildingInput = buildingObj == null ?  buildingIdsByName.get(building) :  buildingIdsByName.get(buildingObj.toString());
        String priceInputFirst = priceFieldFirst.getText();
        priceInputFirst = priceInputFirst.contentEquals("") ? priceFirst : priceInputFirst;
        String priceInputSecond = priceFieldFirst.getText();
        priceInputSecond = priceInputSecond.contentEquals("") ? priceSecond : priceInputSecond;
        Double priceInput = getPrice(priceInputFirst, priceInputSecond);
        Food newFoodItem = new Food(1l, nameInput, descriptionInput, priceInput, buildingInput);
        ServerCommunication.addFoodItem(newFoodItem);
        getBack();
    }

    public Double getPrice(String first, String second) {
        return Double.valueOf(first+"."+second);
    }

}
