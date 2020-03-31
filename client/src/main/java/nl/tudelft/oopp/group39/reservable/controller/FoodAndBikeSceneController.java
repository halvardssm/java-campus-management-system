package nl.tudelft.oopp.group39.reservable.controller;

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
import javafx.scene.control.Hyperlink;
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
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.reservable.model.Food;
import nl.tudelft.oopp.group39.reservable.model.Reservable;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;

public class FoodAndBikeSceneController extends AbstractSceneController {

    @FXML
    protected VBox buildingList;
    @FXML
    private FlowPane itemList;
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
    private Label total;
    @FXML
    private Label titleLabel;

    public double totalprice = 0;
    public List<Room> rooms = new ArrayList<>();
    private int cartItems = 0;
    private List<Reservable> cart = new ArrayList<>();
    private String type;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Building building;
    private ComboBox<String> endTimePicker = new ComboBox<>();

    /**
     * Sets up the page for food or bike.
     *
     * @param type can be bike or food
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void setup(String type) throws JsonProcessingException {
        this.type = type;
        getBuildingsList();
        if (type.equals("bike")) {
            titleLabel.setText("BIKE RENTAL");
            orderbtn.setOnAction(event -> {
                try {
                    placeBikeOrder();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } else {
            titleLabel.setText("FOOD ORDER");
            orderbtn.setOnAction(event -> {
                try {
                    placeFoodOrder();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Gets the list of buildings and shows them in a combobox.
     *
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void getBuildingsList() throws JsonProcessingException {

        String buildingString = ServerCommunication.get(ServerCommunication.building);
        System.out.println(buildingString);
        String body = mapper.writeValueAsString(mapper.readTree(buildingString).get("body"));

        for (Building building : mapper.readValue(body, Building[].class)) {
            Hyperlink buildingName = new Hyperlink(building.getName());
            buildingName.getStyleClass().add("buildingList");
            buildingName.setId(building.getId().toString());
            buildingName.setOnAction(event -> {
                if (type.equals("bike")) {
                    try {
                        getBikes(building.getId());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        getMenu(building.getId());
                        getRoomsList(building.getId());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
            buildingName.getStyleClass().add("buildingList");
            buildingName.setId(String.valueOf(building.getId()));
            buildingList.getChildren().add(buildingName);

        }

    }

    /**
     * Shows the food or bike items.
     *
     * @param json String that contains the items in json
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void showItems(String json) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        itemList.getChildren().clear();
        cartlist.getChildren().clear();
        timeselector.getChildren().clear();
        cartItems = 0;
        rooms.clear();
        totalprice = 0.00;
        total.setText("0.00");
        System.out.println(json);
        ArrayNode body = (ArrayNode) mapper.readTree(json).get("body");
        for (JsonNode itemNode : body) {
            String itemAsString = mapper.writeValueAsString(itemNode);
            if (itemAsString.contains("id")) {
                HBox item;
                if (type.equals("bike")) {
                    Bike bike = mapper.readValue(itemAsString, Bike.class);
                    item = createItemBox(bike.getBikeType(), null, bike);
                } else {
                    Food food = mapper.readValue(itemAsString, Food.class);
                    item = createItemBox(food.getName(), food.getDescription(), food);
                }
                itemList.getChildren().add(item);
            } else {
                break;
            }
        }
        checkEmptyCart();
    }

    /**
     * Creates an item HBox.
     *
     * @param name        name of the item
     * @param description description of the item
     * @param reservable  the reservable to be shown in the box
     * @return HBox of the item
     */
    public HBox createItemBox(String name, String description, Reservable reservable) {
        HBox item = new HBox(20);
        item.getStyleClass().add("fooditem");
        item.setPadding(new Insets(0, 20, 0, 20));
        item.setPrefSize(320, 60);
        item.setAlignment(Pos.CENTER_LEFT);
        VBox namedesc = new VBox();
        Label nameLabel = new Label(name);
        namedesc.getChildren().add(nameLabel);
        namedesc.setAlignment(Pos.CENTER_LEFT);
        if (description != null) {
            Label desc = new Label(description);
            namedesc.getChildren().add(desc);
        }
        Image plus = new Image(getClass().getResourceAsStream("/icons/plus-icon.png"));
        ImageView plusicon = new ImageView(plus);
        plusicon.setFitWidth(15);
        plusicon.setFitHeight(15);
        Button addToCart = new Button();
        addToCart.setGraphic(plusicon);
        addToCart.getStyleClass().add("addToCart");
        addToCart.setOnAction(event -> addToCart(name, reservable));
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Label priceLabel = getPriceLabel(reservable);
        item.getChildren().add(namedesc);
        item.getChildren().add(region);
        item.getChildren().add(priceLabel);
        item.getChildren().add(addToCart);
        return item;
    }

    /**
     * Retrieves bikes filtered on building id.
     *
     * @param buildingId the id of the selected building
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void getBikes(Long buildingId) throws JsonProcessingException {
        showItems(ServerCommunication.getBikes(buildingId));
    }

    /**
     * Retrieves menu filtered on building id.
     *
     * @param buildingId the id of the selected building
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void getMenu(Long buildingId) throws JsonProcessingException {
        showItems(ServerCommunication.getFood(buildingId));
    }

    /**
     * Adds item to cart.
     *
     * @param name       name of the item
     * @param reservable the reservable to be added to the cart
     */
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

    /**
     * Updates the cart details of a reservable.
     *
     * @param reservable the reservable of which the cart details need to be updated
     */
    public void updateCart(Reservable reservable) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + reservable.getId());
        Integer value = amount.getValue();
        if (value == 0) {
            deleteFromCart(reservable.getId());
        } else {
            Label priceLabel = (Label) cartlist.lookup("#" + reservable.getId() + "price");
            double newprice = value * reservable.getPrice();
            double oldprice;
            if (type.equals("bike")) {
                oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1].split("/")[0]);
                priceLabel.setText("$" + newprice + "/hour");
                totalprice = totalprice - oldprice + newprice;
                total.setText("$" + totalprice + "/hour");
            } else {
                oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
                priceLabel.setText("$" + newprice);
                totalprice = totalprice - oldprice + newprice;
                total.setText("$" + totalprice);
            }
        }
    }

    /**
     * Deletes an item from the cart.
     *
     * @param id of the item that needs to be deleted
     */
    public void deleteFromCart(int id) {
        Label priceLabel = (Label) cartlist.lookup("#" + id + "price");
        if (type.equals("bike")) {
            double price = Double.parseDouble(priceLabel.getText().split("\\$")[1].split("/")[0]);
            totalprice = totalprice - price;
            total.setText("$" + totalprice + "/hour");
        } else {
            double price = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
            totalprice = totalprice - price;
            total.setText("$" + totalprice);
        }
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + id);
        HBox fooditem = (HBox) amount.getParent();
        cartlist.getChildren().remove(fooditem);
        cartItems--;
        checkEmptyCart();
    }

    /**
     * Checks if the cart is empty.
     */
    public void checkEmptyCart() {
        if (cartItems == 0) {
            cartlist.getChildren().add(emptycart);
            timeselector.getChildren().clear();
            orderbtn.setDisable(true);
            total.setText("$0.00");
        } else {
            cartlist.getChildren().remove(emptycart);
            orderbtn.setDisable(false);
            setDeliveryDetails();
        }
    }

    /**
     * Creates the interface for the user to select delivery time, date and room or duration.
     */
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
        ComboBox<String> timePicker = createTimePicker();
        timeselector.getChildren().add(timePicker);
        if (type.equals("bike")) {
            updateEndTimePicker(building.getOpen());
            timeselector.getChildren().add(endTimePicker);
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

    /**
     * Creates a start/delivery time picker.
     *
     * @return ComboBox for picking the time
     */
    public ComboBox<String> createTimePicker() {
        int open = Integer.parseInt(building.getOpen().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().split(":")[0]);
        ComboBox<String> timePicker = new ComboBox<>();
        timePicker.setPromptText("Select delivery time");
        timePicker.setId("timePicker");
        if (type.equals("bike")) {
            for (int i = open; i < closed; i++) {
                if (i < 10) {
                    timePicker.getItems().add("0" + i + ":00");
                } else {
                    timePicker.getItems().add(i + ":00");
                }
            }
            timePicker.setOnAction(event -> updateEndTimePicker(timePicker.getValue()));
        } else {
            for (int i = open; i <= closed; i++) {
                if (i < 10) {
                    timePicker.getItems().add("0" + i + ":00");
                } else {
                    timePicker.getItems().add(i + ":00");
                }
            }
        }
        return timePicker;
    }

    /**
     * Updates the end time picker for ordering bikes according to the chosen start time.
     *
     * @param selectedTime the selected start time
     */
    public void updateEndTimePicker(String selectedTime) {
        endTimePicker.getItems().clear();
        int closed = Integer.parseInt(building.getClosed().split(":")[0]);
        int start = Integer.parseInt(selectedTime.split(":")[0]);
        endTimePicker.setPromptText("Select end time");
        endTimePicker.setId("durationPicker");
        for (int i = start + 1; i <= start + 4; i++) {
            if (i <= closed) {
                if (i < 10) {
                    endTimePicker.getItems().add("0" + i + ":00");
                } else {
                    endTimePicker.getItems().add(i + ":00");
                }
            }
        }
    }

    /**
     * Gets a list of the rooms filtered on building id and shows them in a combobox.
     *
     * @param id of the selected building
     * @throws JsonProcessingException when there is a parsing exception
     */
    public void getRoomsList(Long id) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String roomsString = ServerCommunication.getRooms(id.toString());
        System.out.println(roomsString);
        ArrayNode body = (ArrayNode) mapper.readTree(roomsString).get("body");
        for (JsonNode roomJson : body) {
            String roomAsString = mapper.writeValueAsString(roomJson);
            Room room = mapper.readValue(roomAsString, Room.class);
            rooms.add(room);
        }
    }

    /**
     * Retrieves the price label of given reservable.
     *
     * @param reservable of which the price label is needed
     * @return Label containing the price in correct format
     */
    public Label getPriceLabel(Reservable reservable) {
        Label priceLabel;
        if (type.equals("bike")) {
            priceLabel = new Label("$" + reservable.getPrice() + "/hour");
        } else {
            priceLabel = new Label("$" + reservable.getPrice());
        }
        return priceLabel;
    }

    /**
     * Retrieves the time of pickup.
     *
     * @param datePicker in which the date is selected by the user
     * @param timePicker in which the time is selected by the user
     * @return String containing the time of pickup
     */
    public String getTimeOfPickup(DatePicker datePicker, ComboBox<String> timePicker) {
        LocalDate date = datePicker.getValue();
        String time = timePicker.getValue();
        return dateTimeFormatter.format(date) + " " + time + ":00";
    }

    /**
     * Places a bike order.
     */
    public void placeBikeOrder() throws JsonProcessingException {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox<String> timePicker = (ComboBox<String>) timeselector.lookup("#timePicker");
        ComboBox<String> durationPicker = (ComboBox<String>) timeselector.lookup("#durationPicker");
        if (datePicker.getValue() == null
            || timePicker.getSelectionModel().isEmpty()
            || durationPicker.getSelectionModel().isEmpty()
        ) {
            errorfield.setText("Please select a date, time and duration");
        } else {
            String timeOfPickup = getTimeOfPickup(datePicker, timePicker);
            String timeOfDelivery = getTimeOfPickup(datePicker, durationPicker);
            placeOrder(timeOfPickup, timeOfDelivery, null);
        }
    }

    /**
     * Places a food order.
     */
    public void placeFoodOrder() throws JsonProcessingException {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox<String> timePicker = (ComboBox<String>) timeselector.lookup("#timePicker");
        ComboBox<Label> roomSelector = (ComboBox<Label>) timeselector.lookup("#roomSelector");
        if (datePicker.getValue() == null
            || timePicker.getSelectionModel().isEmpty()
            || roomSelector.getSelectionModel().isEmpty()
        ) {
            errorfield.setText("Please select a delivery time, date and room");
        } else {
            String timeOfPickup = getTimeOfPickup(datePicker, timePicker);
            Label room = roomSelector.getValue();
            int roomId = Integer.parseInt(room.getId());
            placeOrder(timeOfPickup, null, roomId);
        }
    }

    /**
     * Places an order.
     *
     * @param timeOfPickup   time of which the food or bike is picked up/delivered
     * @param timeOfDelivery time of which the bike will be returned
     * @param roomId         room the food needs to be delivered to
     */
    public void placeOrder(
        String timeOfPickup,
        String timeOfDelivery,
        Integer roomId
    ) throws JsonProcessingException {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        if (checkDate(dateTimeFormatter.format(datePicker.getValue()))) {
            if (loggedIn) {
                StringBuilder orderString = new StringBuilder("[");
                for (int i = 0; i < cart.size(); i++) {
                    Spinner<Integer> amount =
                        (Spinner<Integer>) cartlist.lookup("#" + cart.get(i).getId());
                    int amountInt = amount.getValue();
                    String ending;
                    if (i == cart.size() - 1) {
                        ending = "}]";
                    } else {
                        ending = "}, ";
                    }
                    orderString
                        .append("{\"amount\":")
                        .append(amountInt).append(",\"reservable\":")
                        .append(cart.get(i).getId())
                        .append(ending);
                }
                System.out.println(orderString);
                String username = AbstractSceneController.user.getUsername();
                String result = ServerCommunication
                    .orderFoodBike(
                        timeOfPickup,
                        timeOfDelivery,
                        username,
                        roomId,
                        orderString.toString());
                createAlert(result);
            } else {
                createAlert("Please log in to place an order");
            }
        }
    }

    public void toggleFilter() {

    }
}
