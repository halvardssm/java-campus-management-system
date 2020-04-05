package nl.tudelft.oopp.group39.reservable.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.time.LocalDate;
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
//import nl.tudelft.oopp.group39.communication.ServerCommunication;
//import nl.tudelft.oopp.group39.models.Bike;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.reservable.model.Food;
import nl.tudelft.oopp.group39.reservable.model.Reservable;
import nl.tudelft.oopp.group39.reservation.model.Reservation;
import nl.tudelft.oopp.group39.room.model.Room;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;
import nl.tudelft.oopp.group39.server.controller.AbstractSceneController;
//import nl.tudelft.oopp.group39.models.Food;
//import nl.tudelft.oopp.group39.models.Reservable;
//import nl.tudelft.oopp.group39.models.Room;

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
    private Building building;
    private ComboBox<String> endTimePicker = new ComboBox<>();
    private ComboBox<String> startTimePicker = new ComboBox<>();
    private List<Integer> bookedBikeTimes = new ArrayList<>();


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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            titleLabel.setText("FOOD ORDER");
            orderbtn.setOnAction(event -> {
                try {
                    placeFoodOrder();
                } catch (IOException e) {
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
                this.building = building;
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
        if (body.isEmpty()) {
            Label label = new Label("No results found");
            itemList.getChildren().add(label);
        } else {
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
        addToCart.setOnAction(event -> {
            try {
                addToCart(name, reservable);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
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
    public void addToCart(String name, Reservable reservable) throws JsonProcessingException {
        if (cartlist.lookup("#cart" + reservable.getId()) == null) {
            if (type.equals("bike") && cartItems > 0) {
                createAlert("You can only order 1 bike per person");
            } else {
                HBox fooditem = new HBox(10);
                fooditem.setId("cart" + reservable.getId());
                fooditem.setAlignment(Pos.CENTER_LEFT);
                Label priceLabel = getPriceLabel(reservable);
                priceLabel.setId(reservable.getId() + "price");
                Button delete = new Button();
                ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
                deletebtn.setFitHeight(20);
                deletebtn.setFitWidth(20);
                delete.setGraphic(deletebtn);
                delete.setStyle("-fx-background-color: none");
                delete.setOnAction(event -> {
                    try {
                        deleteFromCart(reservable);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                Label foodname = new Label(name);
                Region region = new Region();
                HBox.setHgrow(region, Priority.ALWAYS);
                fooditem.getChildren().add(foodname);
                fooditem.getChildren().add(region);
                if (type.equals("food")) {
                    Spinner<Integer> amount = new Spinner<>();
                    amount.setPrefWidth(50);
                    amount.setId(String.valueOf(reservable.getId()));
                    SpinnerValueFactory<Integer> valueFactory =
                        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
                    amount.setValueFactory(valueFactory);
                    amount.valueProperty().addListener(
                        (obs, oldValue, newValue) -> {
                            try {
                                updateCart(reservable);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    );
                    fooditem.getChildren().add(amount);
                }
                fooditem.getChildren().add(priceLabel);
                fooditem.getChildren().add(delete);
                cartlist.getChildren().add(fooditem);
                cartItems++;
                totalprice += reservable.getPrice();
                if (type.equals("bike")) {
                    total.setText("$" + totalprice + "/hour");
                } else {
                    total.setText("$" + totalprice);
                }
                cart.add(reservable);
                checkEmptyCart();

            }
        } else {
            if (type.equals("food")) {
                Spinner<Integer> amount =
                    (Spinner<Integer>) cartlist.lookup("#" + reservable.getId());
                Integer value = amount.getValue() + 1;
                amount.getValueFactory().setValue(value);
                updateCart(reservable);
            }
        }
    }

    /**
     * Updates the cart details of a reservable.
     *
     * @param reservable the reservable of which the cart details need to be updated
     */
    public void updateCart(Reservable reservable) throws JsonProcessingException {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + reservable.getId());
        Integer value = amount.getValue();
        if (value == 0) {
            deleteFromCart(reservable);
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
     * @param reservable the item that needs to be deleted
     */
    public void deleteFromCart(Reservable reservable) throws JsonProcessingException {
        Label priceLabel = (Label) cartlist.lookup("#" + reservable.getId() + "price");
        if (type.equals("bike")) {
            double price = Double.parseDouble(priceLabel.getText().split("\\$")[1].split("/")[0]);
            totalprice = totalprice - price;
            total.setText("$" + totalprice + "/hour");
            bookedBikeTimes.clear();
        } else {
            double price = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
            totalprice = totalprice - price;
            total.setText("$" + totalprice);
        }
        HBox fooditem = (HBox) cartlist.lookup("#cart" + reservable.getId());
        cartlist.getChildren().remove(fooditem);
        cart.remove(reservable);
        cartItems--;
        checkEmptyCart();
    }

    /**
     * Checks if the cart is empty.
     */
    public void checkEmptyCart() throws JsonProcessingException {
        if (cartItems == 0) {
            cartlist.getChildren().add(emptycart);
            timeselector.getChildren().clear();
            orderbtn.setDisable(true);
            total.setText("$0.00");
            errorfield.setText("");
        } else {
            cartlist.getChildren().remove(emptycart);
            orderbtn.setDisable(false);
            setDeliveryDetails();
        }
    }

    /**
     * Creates the interface for the user to select delivery time, date and room or duration.
     */
    public void setDeliveryDetails() throws JsonProcessingException {
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

        if (type.equals("bike")) {
            datePicker.setOnAction(event -> {
                bookedBikeTimes.clear();
                try {
                    updateBikeTimePicker(
                        dateFormatter.format(datePicker.getValue()),
                        cart.get(0).getId().intValue()
                    );
                    updateEndTimePicker(building.getOpen().toString());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            updateBikeTimePicker(dateFormatter.format(
                LocalDate.now()), cart.get(0).getId().intValue());
            startTimePicker.setPromptText("Select start time");
            startTimePicker.setId("timePicker");
            timeselector.getChildren().add(startTimePicker);
            updateEndTimePicker(building.getOpen().toString());
            endTimePicker.setPromptText("Select end time");
            endTimePicker.setId("durationPicker");
            timeselector.getChildren().add(endTimePicker);
        } else {
            ComboBox<String> timePicker = createTimePicker();
            timeselector.getChildren().add(timePicker);
            ComboBox<Label> roomSelector = new ComboBox<>();
            roomSelector.setId("roomSelector");
            roomSelector.setPromptText("Select room");
            for (Room room : rooms) {
                Label roomName = new Label(room.getName());
                roomName.getStyleClass().add("roomSelector");
                roomName.setId(String.valueOf(room.getId()));
                roomSelector.getItems().add(roomName);
            }
            timeselector.getChildren().add(roomSelector);
        }
    }

    /**
     * Retrieves the booked times for the bike on a given date.
     *
     * @param date   the selected date
     * @param bikeId the selected bike
     * @throws JsonProcessingException when there is a processing exception
     */
    public void getBikeTimes(String date, int bikeId) throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String filters = "timeOfPickup=" + date + "T00:00:00" + "&reservable=" + bikeId;
        String reservations = ServerCommunication.getReservation(filters);
        System.out.println(reservations);
        ArrayNode body = (ArrayNode) mapper.readTree(reservations).get("body");
        String reservationString = mapper.writeValueAsString(body);

        System.out.println(bookedBikeTimes);
    }

    /**
     * Updates the start time picker for the bikes based on selected date.
     *
     * @param date   the selected date
     * @param bikeId the selected bike
     * @throws JsonProcessingException when there is a processing exception
     */
    public void updateBikeTimePicker(String date, int bikeId) throws JsonProcessingException {
        startTimePicker.getItems().clear();
        getBikeTimes(date, bikeId);
        List<String> times = new ArrayList<>();
        int open = Integer.parseInt(building.getOpen().toString().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().toString().split(":")[0]);
        for (int i = open; i < closed; i++) {
            String time;
            if (i < 10) {
                time = "0" + i + ":00";
            } else {
                time = i + ":00";
            }
            times.add(time);
        }
        if (bookedBikeTimes.size() != 0) {
            for (int j = 0; j < bookedBikeTimes.size(); j = j + 2) {
                for (int i = open; i < closed; i++) {
                    if (i >= bookedBikeTimes.get(j) && i < bookedBikeTimes.get(j + 1)) {
                        String time;
                        if (i < 10) {
                            time = "0" + i + ":00";
                        } else {
                            time = i + ":00";
                        }
                        times.remove(time);
                    }
                }
            }
        }
        startTimePicker.setOnAction(event -> {
            String fromValue;
            if (startTimePicker.getSelectionModel().isEmpty()) {
                fromValue = building.getOpen().toString();
            } else {
                fromValue = startTimePicker.getValue();
            }
            updateEndTimePicker(fromValue);
        });
        startTimePicker.getItems().addAll(times);
    }

    /**
     * Creates a delivery time picker.
     *
     * @return ComboBox for picking the time
     */
    public ComboBox<String> createTimePicker() {
        int open = Integer.parseInt(building.getOpen().toString().split(":")[0]);
        int closed = Integer.parseInt(building.getClosed().toString().split(":")[0]);
        ComboBox<String> timePicker = new ComboBox<>();
        timePicker.setPromptText("Select delivery time");
        timePicker.setId("timePicker");
        for (int i = open; i <= closed; i++) {
            if (i < 10) {
                timePicker.getItems().add("0" + i + ":00");
            } else {
                timePicker.getItems().add(i + ":00");
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
        int closed = Integer.parseInt(building.getClosed().toString().split(":")[0]);
        int start = Integer.parseInt(selectedTime.split(":")[0]);
        List<Integer> times = new ArrayList<>();
        for (int i = start + 1; i < start + 5; i++) {
            if (i <= closed) {
                times.add(i);
            }
        }
        if (bookedBikeTimes.size() != 0) {
            for (int j = 0; j < bookedBikeTimes.size(); j = j + 2) {
                for (int i = start + 1; i < start + 5; i++) {
                    if (i > bookedBikeTimes.get(j) && i <= bookedBikeTimes.get(j + 1)) {
                        times.remove((Integer) i);
                    }
                }

            }
        }
        for (int t = start + 2; t < start + 5; t++) {
            if (!times.contains(t - 1)) {
                Integer remove = t;
                times.remove(remove);
            }
        }
        for (int i : times) {
            String timeSlot;
            if (i <= closed) {
                if (i < 10) {
                    timeSlot = "0" + i + ":00";
                } else {
                    timeSlot = i + ":00";
                }
                endTimePicker.getItems().add(timeSlot);
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
        String roomsString = ServerCommunication.getRooms("building=" + id);
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
        return dateFormatter.format(date) + " " + time + ":00";
    }

    /**
     * Places a bike order.
     */
    public void placeBikeOrder() throws IOException {
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
            String orderString = "[{\"amount\":1,\"reservable\":" + cart.get(0).getId() + "}]";
            placeOrder(timeOfPickup, timeOfDelivery, null, orderString);
        }
    }

    /**
     * Places a food order.
     */
    public void placeFoodOrder() throws IOException {
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
            Long roomId = Long.parseLong(room.getId());
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
            placeOrder(timeOfPickup, null, roomId, orderString.toString());
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
        Long roomId,
        String orderString
    ) throws IOException {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        if (loggedIn) {
            if (checkDate(dateFormatter.format(datePicker.getValue()), roomId)) {
                System.out.println(orderString);
                String username = AbstractSceneController.user.getUsername();
                String result = ServerCommunication
                    .orderFoodBike(
                        timeOfPickup,
                        timeOfDelivery,
                        username,
                        roomId,
                        orderString);
                createAlert(result);
                if (type.equals("bike")) {
                    goToBikeRentalScene();
                } else {
                    goToFoodOrderScene();
                }
            }
        } else {
            errorfield.setText("Please log in to place an order");
        }
    }
}
