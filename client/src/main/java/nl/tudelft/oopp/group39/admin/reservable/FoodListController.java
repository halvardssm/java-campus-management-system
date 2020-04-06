package nl.tudelft.oopp.group39.admin.reservable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import nl.tudelft.oopp.group39.admin.user.UserCreateController;
import nl.tudelft.oopp.group39.admin.user.UserEditController;
import nl.tudelft.oopp.group39.reservable.model.Food;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.user.model.User;

public class FoodListController extends AdminPanelController {

    private ObjectMapper mapper = new ObjectMapper();
    private String lastSelectedRole;
    private String lastSelectedName;
    private String allRoles = "ALL ROLES";
    @FXML
    private Button backbtn;
    @FXML private TableView<Food> foodTable;
    @FXML private TableColumn<Food, String> idCol;
    @FXML private TableColumn<Food, String> nameCol;
    @FXML private TableColumn<Food, String> descriptionCol;
    @FXML private TableColumn<Food, Double> priceCol;
    @FXML private TableColumn<Food, String> buildingCol;
    @FXML
    private TableColumn<Food, Food> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Food, Food> updateCol = new TableColumn<>("Update");
    @FXML
    private ComboBox<String> roleBox;
    @FXML
    private TextField usernameField;
    @FXML
    private MenuBar navBar;

    /**
     * Initializes scene.
     */
    public void customInit() {
        try {
            loadFood();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Stage currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
    }

    /**
     * Display users and data into tableView named userTable.
     */

    void loadFood() throws JsonProcessingException {
        foodTable.setVisible(true);
        foodTable.getItems().clear();
        foodTable.getColumns().clear();
        String foodItems = ServerCommunication.get(ServerCommunication.food);
        System.out.println(foodItems);
        if (!foodItems.contains("\"body\" : null,")) {
            ArrayNode body = (ArrayNode) mapper.readTree(foodItems).get("body");
            foodItems = mapper.writeValueAsString(body);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            deleteCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            deleteCol.setCellFactory(param -> returnCell("Delete"));
            updateCol.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
            );
            Food[] list = mapper.readValue(foodItems, Food[].class);
            updateCol.setCellFactory(param -> returnCell("Update"));
            ObservableList<Food> data = FXCollections.observableArrayList(list);
            foodTable.setItems(data);
            foodTable.getColumns().addAll(
                idCol,
                nameCol,
                priceCol,
                buildingCol,
                descriptionCol,
                deleteCol,
                updateCol);
        }
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Food, Food> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Food food, boolean empty) {
                super.updateItem(food, empty);

                if (food == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editFoodItem(food);
                                    break;
                                case "Delete":
                                    deleteFoodItem(food);
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
     * Switches scene to the createUser one.
     */
    public void createFoodItem() throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/FoodCreate.fxml");
        FoodCreateController controller = loader.getController();
        controller.initData();
    }
    /**
     * Deletes selected user.
     */

    public void deleteFoodItem(Food food) throws IOException {
        String id = Long.toString(food.getId());
        ServerCommunication.removeFoodItem(id);
        loadFood();
    }
    /**
     * Sends user to the user edit page.
     */

    public void editFoodItem(Food food) throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/FoodEdit.fxml");
        FoodEditController controller = loader.getController();
        controller.initData(food);
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

}
