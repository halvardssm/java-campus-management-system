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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.models.Bike;

public class BikeSceneController extends MainSceneController {

    @FXML
    private VBox bikelist;

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

    private int cartItems = 0;

    private List<Bike> bikes = new ArrayList<>();

    public void getBikes() throws JsonProcessingException {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        bikelist.getChildren().clear();
        String bikeString = ServerCommunication.getAllBikes();
        System.out.println(bikeString);
        //String teststring = "{\"body\": [{\"id\":1, \"name\":\"bike1\", \"price\":\"2.35\"}, {\"id\":2, \"name\":\"bike2\", \"price\":\"2.65\"}], \"error\": null}";
        ArrayNode body = (ArrayNode) mapper.readTree(bikeString).get("body");
        for (JsonNode bikeElement : body) {
            String bikeAsString = mapper.writeValueAsString(bikeElement);
            Bike bikeObj = mapper.readValue(bikeAsString, Bike.class);
            HBox bike = new HBox(20);
            bike.getStyleClass().add("fooditem");
            bike.setPadding(new Insets(0, 20, 0, 20));
            bike.setPrefSize(300, 60);
            bike.setAlignment(Pos.CENTER_LEFT);
            Label name = new Label(bikeObj.getBikeType());
            Label priceLabel = new Label(bikeObj.getPrice() + "/hour");
            Button addToCart = new Button("+");
            addToCart.setOnAction(event -> addToCart(bikeObj));
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            bike.getChildren().add(name);
            bike.getChildren().add(region);
            bike.getChildren().add(priceLabel);
            bike.getChildren().add(addToCart);
            bikelist.getChildren().add(bike);
        }
    }

    public void addToCart(Bike bikeObj) {
        if (cartlist.lookup("#" + bikeObj.getId()) == null) {
            HBox bike = new HBox(10);
            bike.setPadding(new Insets(15, 15, 15, 0));
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(String.valueOf(bikeObj.getId()));
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            amount.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateCart(bikeObj.getId(), bikeObj.getPrice())
            );
            Label priceLabel = new Label("$" + bikeObj.getPrice() + "/hour");
            priceLabel.setId(bikeObj.getId() + "price");
            Button delete = new Button();
            ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
            deletebtn.setFitHeight(20);
            deletebtn.setFitWidth(20);
            delete.setGraphic(deletebtn);
            delete.setStyle("-fx-background-color: none");
            delete.setOnAction(event -> deleteFromCart(bikeObj.getId()));
            Label bikename = new Label(bikeObj.getBikeType());
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            bike.getChildren().add(bikename);
            bike.getChildren().add(region);
            bike.getChildren().add(amount);
            bike.getChildren().add(priceLabel);
            bike.getChildren().add(delete);
            cartlist.getChildren().add(bike);
            cartItems++;
            checkEmptyCart();
            bikes.add(bikeObj);
        } else {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + bikeObj.getId());
            Integer value = amount.getValue() + 1;
            amount.getValueFactory().setValue(value);
            updateCart(bikeObj.getId(), bikeObj.getPrice());
        }
    }

    public void updateCart(int id, double price) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + id);
        Integer value = amount.getValue();
        Label priceLabel = (Label) cartlist.lookup("#" + id + "price");
        double newprice = value * price;
        priceLabel.setText("$" + newprice + "/hour");
    }

    public void deleteFromCart(int id) {
        Label priceLabel = (Label) cartlist.lookup("#" + id + "price");
        //double price = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + id);
        HBox bike = (HBox) amount.getParent();
        cartlist.getChildren().remove(bike);
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
            timeselector.getChildren().clear();
            DatePicker datePicker = new DatePicker();
            datePicker.setId("datePicker");
            timeselector.getChildren().add(datePicker);
            ComboBox timePicker = new ComboBox();
            timePicker.setPromptText("Select start time");
            ComboBox durationPicker = new ComboBox();
            durationPicker.setPromptText("Select duration");
            timePicker.setId("timePicker");
            durationPicker.setId("durationPicker");
            for (int i = 9; i < 20; i++) {
                if (i < 10) {
                    timePicker.getItems().add("0" + i + ":00");
                } else {
                    timePicker.getItems().add(i + ":00");
                }
            }
            for (int j = 1; j < 5; j++) {
                durationPicker.getItems().add(j + " hours");
            }
            timeselector.getChildren().add(timePicker);
            timeselector.getChildren().add(durationPicker);

        }
    }

    public void placeOrder() {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox timePicker = (ComboBox) timeselector.lookup("#timePicker");
        ComboBox durationPicker = (ComboBox) timeselector.lookup("#durationPicker");
        if (datePicker.getValue() == null || timePicker.getSelectionModel().isEmpty() || durationPicker.getSelectionModel().isEmpty()) {
            errorfield.setText("Please select a date, time and duration");
        } else {
            LocalDate date = datePicker.getValue();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String time = timePicker.getValue().toString();
            String dateTime = dateTimeFormatter.format(date) + " " + time + ":00";
            System.out.println(dateTime);
            int duration = Integer.parseInt(durationPicker.getValue().toString().split(" ")[0]);
            int start = Integer.parseInt(time.split(":")[0]);
            int end = start + duration;
            String endTime;
            if (end < 10) {
                endTime = "0" + end + ":00:00";
            } else {
                endTime = end + ":00:00";
            }
            String timeOfDelivery = dateTimeFormatter.format(date) + " " + endTime;
            String bikeString = "[";
            for (int i = 0; i < bikes.size(); i++) {
                Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + bikes.get(i).getId());
                int foodamount = amount.getValue();
                String ending;
                if (i == bikes.size() - 1) {
                    ending = "}]";
                } else {
                    ending = "}, ";
                }
                bikeString = bikeString + "{\"amount\":" + foodamount
                    + ",\"reservable\":" + bikes.get(i).getId()
                    + ending;
            }
            System.out.println(bikes);
            String username = MainSceneController.username;
            String result = ServerCommunication.orderFoodBike(dateTime, timeOfDelivery, username, null, bikeString);
            createAlert(result);
        }
    }
}
