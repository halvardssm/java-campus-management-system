package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import javafx.scene.control.Hyperlink;
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

public class BikeSceneController extends MainSceneController {

    @FXML
    private VBox buildinglist;

    @FXML
    private VBox bikelist;

    @FXML
    private VBox cartlist;

    @FXML
    private Label emptycart;

    private int cartItems = 0;

    private List<Integer> bikeIds = new ArrayList<>();

    public void getBuildings() {
        String buildingString = ServerCommunication.getBuildings();
        System.out.println(buildingString);

        JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
        JsonArray buildingArray = body.getAsJsonArray("body");

        for (JsonElement building : buildingArray) {
            String buildingName = ((JsonObject) building).get("name").getAsString();
            Hyperlink buildingLink = new Hyperlink(buildingName);
            buildinglist.getChildren().add(buildingLink);
        }
    }

    public void getBikes() {
        String teststring = "{\"body\": [{\"id\":1, \"name\":\"bike1\", \"price\":\"2.35\"}, {\"id\":2, \"name\":\"bike2\", \"price\":\"2.65\"}], \"error\": null}";
        JsonObject body = ((JsonObject) JsonParser.parseString(teststring));
        JsonArray bikeArray = body.getAsJsonArray("body");
        for (JsonElement bikeElement : bikeArray) {
            String bikename = ((JsonObject) bikeElement).get("name").getAsString();
            Double price = ((JsonObject) bikeElement).get("price").getAsDouble();
            int id = ((JsonObject) bikeElement).get("id").getAsInt();
            HBox bike = new HBox(20);
            bike.getStyleClass().add("fooditem");
            bike.setPadding(new Insets(0, 20, 0, 20));
            bike.setPrefSize(300, 60);
            bike.setAlignment(Pos.CENTER_LEFT);
            Label name = new Label(bikename);
            Label priceLabel = new Label(price.toString() + "/hour");
            Button addToCart = new Button("+");
            addToCart.setOnAction(event -> addToCart(bikename, price, id));
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            bike.getChildren().add(name);
            bike.getChildren().add(region);
            bike.getChildren().add(priceLabel);
            bike.getChildren().add(addToCart);
            bikelist.getChildren().add(bike);
        }

        DatePicker datePicker = new DatePicker();
        datePicker.setId("datePicker");
        cartlist.getChildren().add(datePicker);
        ComboBox timePicker = new ComboBox();
        ComboBox durationPicker = new ComboBox();
        timePicker.setId("timePicker");
        durationPicker.setId("durationPicker");
        for (int i = 9; i < 20; i++) {
            timePicker.getItems().add(i + ":00");
        }
        for (int j = 1; j < 4; j++) {
            durationPicker.getItems().add(j + " hours");
        }
        cartlist.getChildren().add(timePicker);
        cartlist.getChildren().add(durationPicker);
    }

    public void addToCart(String name, double price, int id) {
        if (cartlist.lookup("#" + id) == null) {
            HBox bike = new HBox(10);
            bike.setPadding(new Insets(15, 15, 15, 0));
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(String.valueOf(id));
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            amount.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateCart(id, price)
            );
            Label priceLabel = new Label("$" + price + "/hour");
            priceLabel.setId(id + "price");
            Button delete = new Button();
            ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
            deletebtn.setFitHeight(20);
            deletebtn.setFitWidth(20);
            delete.setGraphic(deletebtn);
            delete.setStyle("-fx-background-color: none");
            delete.setOnAction(event -> deleteFromCart(id));
            Label bikename = new Label(name);
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
            bikeIds.add(id);
        } else {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + id);
            Integer value = amount.getValue() + 1;
            amount.getValueFactory().setValue(value);
            updateCart(id, price);
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
        } else {
            cartlist.getChildren().remove(emptycart);


        }
    }

    public void placeOrder() {
        DatePicker datePicker = (DatePicker) cartlist.lookup("#datePicker");
        LocalDate date = datePicker.getValue();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ComboBox timePicker = (ComboBox) cartlist.lookup("#timePicker");
        String time = timePicker.getValue().toString();
        String dateTime = dateTimeFormatter.format(date) + " " + time;
        System.out.println(dateTime);
        ComboBox durationPicker = (ComboBox) cartlist.lookup("#durationPicker");
        String duration = durationPicker.getValue().toString();
        String bikes = "[";
        for (int i = 0; i < bikeIds.size(); i++) {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + bikeIds.get(i));
            int foodamount = amount.getValue();
            if (i == bikeIds.size() - 1) {
                bikes = bikes + "{" + bikeIds.get(i) + ":" + foodamount + "}]";
            } else {
                bikes = bikes + "{" + bikeIds.get(i) + ":" + foodamount + "}, ";
            }
        }
        System.out.println(bikes);
        String user = MainSceneController.username;
        String result = ServerCommunication.orderBike(dateTime, user, bikes);
        createAlert(result);
    }
}
