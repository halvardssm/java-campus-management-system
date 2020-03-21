package nl.tudelft.oopp.group39.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Food;
import nl.tudelft.oopp.group39.models.Room;

public class FoodSceneController extends MainSceneController {

    @FXML
    private VBox cartlist;

    @FXML
    private Label emptycart;

    @FXML
    private FlowPane foodmenu;

    @FXML
    private Button orderbtn;

    public double totalprice = 0;

    public int cartItems = 0;

    public List<Food> foodCart = new ArrayList<>();

    public List<Room> rooms = new ArrayList<>();

    @FXML
    private Label total;

    @FXML
    private VBox timeselector;

    @FXML
    private Label errorfield;


    public void getMenu() throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        foodmenu.getChildren().clear();
        String foodString = ServerCommunication.getAllFood();
        Label building = (Label) buildinglist.getValue();
        // int id = Integer.parseInt(building.getId());
        getRoomsList();
        System.out.println(rooms);
        System.out.println(foodString);
        ArrayNode body = (ArrayNode) mapper.readTree(foodString).get("body");

        for (JsonNode fooditem : body) {
            String foodAsString = mapper.writeValueAsString(fooditem);
            Food foodObj = mapper.readValue(foodAsString, Food.class);
            HBox food = new HBox(20);
            food.getStyleClass().add("fooditem");
            food.setPadding(new Insets(0, 15, 0, 15));
            food.setPrefSize(300, 60);
            food.setAlignment(Pos.CENTER_LEFT);
            VBox namedesc = new VBox();
            Label name = new Label(foodObj.getName());
            Label desc = new Label(foodObj.getDescription());
            namedesc.getChildren().addAll(name, desc);
            Label priceLabel = new Label(String.valueOf(foodObj.getPrice()));
            Button addToCart = new Button("+");
            addToCart.setOnAction(event -> addToCart(foodObj));
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            food.getChildren().add(namedesc);
            food.getChildren().add(region);
            food.getChildren().add(priceLabel);
            food.getChildren().add(addToCart);
            foodmenu.getChildren().add(food);
        }
    }

    public void getRoomsList() throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String roomsString = ServerCommunication.getRooms();
        String testString = "{\"body\":[{\"id\":1,\"capacity\":10,\"name\":\"Ampere\",\"onlyStaff\":true,\"description\":\"test1\",\"facilities\":[],\"events\":[],\"bookings\":[],\"building\":1},{\"id\":2,\"capacity\":6,\"name\":\"test2\",\"onlyStaff\":true,\"description\":\"test2\",\"facilities\":[{\"id\":1,\"description\":\"smartboard\"}],\"events\":[],\"bookings\":[],\"building\":1}],\"error\":null}";
        System.out.println(roomsString);
        ArrayNode body = (ArrayNode) mapper.readTree(testString).get("body");
        for (JsonNode roomJson : body) {
            String roomAsString = mapper.writeValueAsString(roomJson);
            Room room = mapper.readValue(roomAsString, Room.class);
            rooms.add(room);
        }
    }

    public void addToCart(Food food) {
        if (cartlist.lookup("#" + food.getId()) == null) {
            HBox fooditem = new HBox(10);
            fooditem.setAlignment(Pos.CENTER_LEFT);
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(String.valueOf(food.getId()));
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            amount.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateCart(food)
            );
            Label priceLabel = new Label("$" + food.getPrice());
            priceLabel.setId(food.getId() + "price");
            Button delete = new Button();
            ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
            deletebtn.setFitHeight(20);
            deletebtn.setFitWidth(20);
            delete.setGraphic(deletebtn);
            delete.setStyle("-fx-background-color: none");
            delete.setOnAction(event -> deleteFromCart(food.getId()));
            Label foodname = new Label(food.getName());
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            fooditem.getChildren().add(foodname);
            fooditem.getChildren().add(region);
            fooditem.getChildren().add(amount);
            fooditem.getChildren().add(priceLabel);
            fooditem.getChildren().add(delete);
            cartlist.getChildren().add(fooditem);
            cartItems++;
            totalprice += food.getPrice();
            total.setText("$" + totalprice);
            checkEmptyCart();
            foodCart.add(food);
        } else {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + food.getId());
            Integer value = amount.getValue() + 1;
            amount.getValueFactory().setValue(value);
            updateCart(food);
        }

    }

    public void updateCart(Food food) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + food.getId());
        Integer value = amount.getValue();
        Label priceLabel = (Label) cartlist.lookup("#" + food.getId() + "price");
        double oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
        double newprice = value * food.getPrice();
        priceLabel.setText("$" + newprice);
        totalprice = totalprice - oldprice + newprice;
        total.setText("$" + totalprice);
    }

    public void deleteFromCart(int id) {
        Label priceLabel = (Label) cartlist.lookup("#" + id + "price");
        double price = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
        totalprice = totalprice - price;
        total.setText("$" + totalprice);
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + id);
        HBox fooditem = (HBox) amount.getParent();
        cartlist.getChildren().remove(fooditem);
        cartItems--;
        checkEmptyCart();
    }

    public void checkEmptyCart() {
        if (cartItems == 0) {
            cartlist.getChildren().add(emptycart);
            timeselector.getChildren().clear();
            orderbtn.setDisable(true);
        } else {
            cartlist.getChildren().remove(emptycart);
            orderbtn.setDisable(false);
            setDeliveryDetails();
        }
    }

    public void setDeliveryDetails() {
        timeselector.getChildren().clear();
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select delivery date");
        datePicker.setId("datePicker");
        timeselector.getChildren().add(datePicker);
        ComboBox timePicker = new ComboBox();
        timePicker.setPromptText("Select delivery time");
        timePicker.setId("timePicker");
        for (int i = 9; i < 20; i++) {
            if (i < 10) {
                timePicker.getItems().add("0" + i + ":00");
            } else {
                timePicker.getItems().add(i + ":00");
            }
        }
        timeselector.getChildren().add(timePicker);
        ComboBox roomSelector = new ComboBox();
        roomSelector.setId("roomSelector");
        roomSelector.setPromptText("Select room");
        for (Room room : rooms) {
            Label roomName = new Label(room.getName());
            roomName.setId(String.valueOf(room.getId()));
            roomSelector.getItems().add(roomName);
        }
        timeselector.getChildren().add(roomSelector);
    }

    public void placeOrder() {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox timePicker = (ComboBox) timeselector.lookup("#timePicker");
        if (datePicker.getValue() == null || timePicker.getSelectionModel().isEmpty()) {
            errorfield.setText("Please select a delivery time and date");
        } else {
            System.out.println(ServerCommunication.getReservations());
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = datePicker.getValue();
            String time = timePicker.getValue().toString() + ":00";
            String dateTime = dateTimeFormatter.format(date) + " " + time;
            System.out.println(dateTime);
            String foods = "[";
            for (int i = 0; i < foodCart.size(); i++) {
                Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + foodCart.get(i).getId());
                int foodamount = amount.getValue();
                String end;
                if (i == foodCart.size() - 1) {
                    end = "}]";
                } else {
                    end = "}, ";
                }
                foods = foods + "{\"amount\":" + foodamount
                    + ",\"reservable\":" + foodCart.get(i).getId()
                    + end;
            }
            System.out.println(foods);
            ComboBox roomSelector = (ComboBox) timeselector.lookup("#roomSelector");
            Label room = (Label) roomSelector.getValue();
            int roomId = Integer.parseInt(room.getId());
            String username = MainSceneController.username;
            String result = ServerCommunication.orderFoodBike(dateTime, null, username, roomId, foods);
            createAlert(result);
        }

    }

}
