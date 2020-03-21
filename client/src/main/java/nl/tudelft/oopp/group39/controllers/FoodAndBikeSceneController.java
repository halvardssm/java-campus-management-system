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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Bike;
import nl.tudelft.oopp.group39.models.Food;
import nl.tudelft.oopp.group39.models.Reservable;
import nl.tudelft.oopp.group39.models.Room;

public class FoodAndBikeSceneController extends MainSceneController {

    public double totalprice = 0;
    public List<Room> rooms = new ArrayList<>();
    @FXML
    protected ComboBox<Label> buildinglist;
    @FXML
    private VBox itemlist;
    @FXML
    private VBox cartlist;
    @FXML
    private Label emptycart;
    @FXML
    private VBox timeselector;
    @FXML
    private Label errorfield;
    @FXML
    private Button orderbtn;
    @FXML
    private Button getBtn;
    @FXML
    private Label total;
    @FXML
    private Label titleLabel;
    private int cartItems = 0;
    private List<Reservable> cart = new ArrayList<>();
    private String type;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public void setup(String type) throws JsonProcessingException {
        this.type = type;
        getBuildingsList();
        if (type.equals("bike")) {
            titleLabel.setText("BIKE RENTAL");
            getBtn.setText("Get bikes");
            getBtn.setOnAction(event -> {
                try {
                    getBikes();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            orderbtn.setOnAction(event -> placeBikeOrder());
        } else {
            titleLabel.setText("FOOD ORDER");
            getBtn.setText("Get Menu");
            getBtn.setOnAction(event -> {
                try {
                    getMenu();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            orderbtn.setOnAction(event -> placeFoodOrder());
        }
    }

    public void getBuildingsList() throws JsonProcessingException {
        String buildingString = ServerCommunication.getBuildings();
        System.out.println(buildingString);

        ArrayNode body = (ArrayNode) mapper.readTree(buildingString).get("body");

        for (JsonNode building : body) {
            Label buildingName = new Label(building.get("name").asText());
            buildingName.setId(building.get("id").asText());
            buildinglist.getItems().add(buildingName);
        }
    }

    public void showItems(String json) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        itemlist.getChildren().clear();
        Label building = buildinglist.getValue();
        // int id = Integer.parseInt(building.getId());
        getRoomsList();
        System.out.println(rooms);
        System.out.println(json);
        ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
        for (JsonNode itemNode : body) {
            String itemAsString = mapper.writeValueAsString(itemNode);
            Reservable reservable;
            HBox item;
            if (type.equals("bike")) {
                Bike bike = mapper.readValue(itemAsString, Bike.class);
                item = createItemBox(bike.getBikeType(), null, bike);
            } else {
                Food food = mapper.readValue(itemAsString, Food.class);
                item = createItemBox(food.getName(), food.getDescription(), food);
            }
            itemlist.getChildren().add(item);
        }
    }

    public HBox createItemBox(String name, String description, Reservable reservable) {
        HBox item = new HBox(20);
        item.getStyleClass().add("fooditem");
        item.setPadding(new Insets(0, 20, 0, 20));
        item.setPrefSize(300, 60);
        item.setAlignment(Pos.CENTER_LEFT);
        VBox namedesc = new VBox();
        Label nameLabel = new Label(name);
        namedesc.getChildren().add(nameLabel);
        namedesc.setAlignment(Pos.CENTER_LEFT);
        if (description != null) {
            Label desc = new Label(description);
            namedesc.getChildren().add(desc);
        }
        Label priceLabel = getPriceLabel(reservable);
        Button addToCart = new Button("+");
        addToCart.setOnAction(event -> addToCart(name, reservable));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        item.getChildren().add(namedesc);
        item.getChildren().add(region);
        item.getChildren().add(priceLabel);
        item.getChildren().add(addToCart);
        return item;
    }

    public void getBikes() throws JsonProcessingException {
        showItems(ServerCommunication.getAllBikes());
    }

    public void getMenu() throws JsonProcessingException {
        showItems(ServerCommunication.getAllFood());
    }

    public void addToCart(String name, Reservable reservable) {
        if (cartlist.lookup("#" + reservable.getId()) == null) {
            HBox fooditem = new HBox(10);
            fooditem.setAlignment(Pos.CENTER_LEFT);
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(String.valueOf(reservable.getId()));
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            amount.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateCart(reservable)
            );
            Label priceLabel = getPriceLabel(reservable);
            priceLabel.setId(reservable.getId() + "price");
            Button delete = new Button();
            ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
            deletebtn.setFitHeight(20);
            deletebtn.setFitWidth(20);
            delete.setGraphic(deletebtn);
            delete.setStyle("-fx-background-color: none");
            delete.setOnAction(event -> deleteFromCart(reservable.getId()));
            Label foodname = new Label(name);
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            fooditem.getChildren().add(foodname);
            fooditem.getChildren().add(region);
            fooditem.getChildren().add(amount);
            fooditem.getChildren().add(priceLabel);
            fooditem.getChildren().add(delete);
            cartlist.getChildren().add(fooditem);
            cartItems++;
            totalprice += reservable.getPrice();
            total.setText("$" + totalprice);
            checkEmptyCart();
            cart.add(reservable);
        } else {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + reservable.getId());
            Integer value = amount.getValue() + 1;
            amount.getValueFactory().setValue(value);
            updateCart(reservable);
        }

    }

    public void updateCart(Reservable reservable) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + reservable.getId());
        Integer value = amount.getValue();
        Label priceLabel = (Label) cartlist.lookup("#" + reservable.getId() + "price");
        double newprice = value * reservable.getPrice();
        double oldprice;
        if (type.equals("bike")) {
            oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1].split("/")[0]);
            priceLabel.setText("$" + newprice + "/hour");
            total.setText("$" + totalprice + "/hour");
        } else {
            oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
            priceLabel.setText("$" + newprice);
            total.setText("$" + totalprice);
        }
        totalprice = totalprice - oldprice + newprice;
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
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        timeselector.getChildren().add(datePicker);
        ComboBox<String> timePicker = new ComboBox<>();
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

        if (type.equals("bike")) {
            ComboBox<String> durationPicker = new ComboBox<>();
            durationPicker.setPromptText("Select duration");
            timePicker.setId("timePicker");
            durationPicker.setId("durationPicker");
            for (int j = 1; j < 5; j++) {
                durationPicker.getItems().add(j + " hours");
            }
            timeselector.getChildren().add(durationPicker);
        } else {
            ComboBox<Label> roomSelector = new ComboBox<>();
            roomSelector.setId("roomSelector");
            roomSelector.setPromptText("Select room");
            for (Room room : rooms) {
                Label roomName = new Label(room.getName());
                roomName.setId(String.valueOf(room.getId()));
                roomSelector.getItems().add(roomName);
            }
            timeselector.getChildren().add(roomSelector);
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

    public Label getPriceLabel(Reservable reservable) {
        Label priceLabel;
        if (type.equals("bike")) {
            priceLabel = new Label("$" + reservable.getPrice() + "/hour");
        } else {
            priceLabel = new Label("$" + reservable.getPrice());
        }
        return priceLabel;
    }

    public String getTimeOfPickup(DatePicker datePicker, ComboBox<String> timePicker) {
        LocalDate date = datePicker.getValue();
        String time = timePicker.getValue();
        return dateTimeFormatter.format(date) + " " + time + ":00";
    }

    public String getTimeOfDelivery(DatePicker datePicker, ComboBox<String> timePicker, ComboBox<String> durationPicker) {
        String time = timePicker.getValue();
        LocalDate date = datePicker.getValue();
        int duration = Integer.parseInt(durationPicker.getValue().split(" ")[0]);
        int start = Integer.parseInt(time.split(":")[0]);
        int end = start + duration;
        String endTime;
        if (end < 10) {
            endTime = "0" + end + ":00:00";
        } else {
            endTime = end + ":00:00";
        }
        return dateTimeFormatter.format(date) + " " + endTime;
    }

    public void placeBikeOrder() {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox<String> timePicker = (ComboBox<String>) timeselector.lookup("#timePicker");
        ComboBox<String> durationPicker = (ComboBox<String>) timeselector.lookup("#durationPicker");
        if (datePicker.getValue() == null || timePicker.getSelectionModel().isEmpty() || durationPicker.getSelectionModel().isEmpty()) {
            errorfield.setText("Please select a date, time and duration");
        } else {
            String timeOfPickup = getTimeOfPickup(datePicker, timePicker);
            String timeOfDelivery = getTimeOfDelivery(datePicker, timePicker, durationPicker);
            placeOrder(timeOfPickup, timeOfDelivery, null);
        }
    }

    public void placeFoodOrder() {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox timePicker = (ComboBox) timeselector.lookup("#timePicker");
        ComboBox roomSelector = (ComboBox) timeselector.lookup("#roomSelector");
        if (datePicker.getValue() == null || timePicker.getSelectionModel().isEmpty() || roomSelector.getSelectionModel().isEmpty()) {
            errorfield.setText("Please select a delivery time, date and room");
        } else {
            String timeOfPickup = getTimeOfPickup(datePicker, timePicker);
            Label room = (Label) roomSelector.getValue();
            int roomId = Integer.parseInt(room.getId());
            placeOrder(timeOfPickup, null, roomId);
        }
    }

    public void placeOrder(String timeOfPickup, String timeOfDelivery, Integer roomId) {
        StringBuilder orderString = new StringBuilder("[");
        for (int i = 0; i < cart.size(); i++) {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + cart.get(i).getId());
            int amountInt = amount.getValue();
            String ending;
            if (i == cart.size() - 1) {
                ending = "}]";
            } else {
                ending = "}, ";
            }
            orderString.append("{\"amount\":").append(amountInt).append(",\"reservable\":").append(cart.get(i).getId()).append(ending);
        }
        System.out.println(orderString);
        String username = MainSceneController.username;
        String result = ServerCommunication.orderFoodBike(timeOfPickup, timeOfDelivery, username, roomId, orderString.toString());
        createAlert(result);
    }
}
