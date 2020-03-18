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
import nl.tudelft.oopp.group39.entities.Food;

public class FoodSceneController extends MainSceneController {
    @FXML
    private ComboBox buildingsList;

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

    @FXML
    private Label total;

    @FXML
    private VBox timeselector;

    @FXML
    private Label errorfield;

    public void getMenu() {
        foodmenu.getChildren().clear();
        String foodString = ServerCommunication.getAllFood();
        System.out.println(foodString);
        // String teststring = "{\"body\": [{\"id\":1, \"name\":\"voedsel\", \"price\":\"2.35\"}, {\"id\":2, \"name\":\"bitterballen\", \"price\":\"2.65\"}], \"error\": null}";
        JsonObject body = ((JsonObject) JsonParser.parseString(foodString));
        JsonArray foodArray = body.getAsJsonArray("body");

        for (JsonElement fooditem : foodArray) {
            int id = ((JsonObject) fooditem).get("id").getAsInt();
            String foodname = ((JsonObject) fooditem).get("name").getAsString();
            String fooddesc = ((JsonObject) fooditem).get("description").getAsString();
            Double price = ((JsonObject) fooditem).get("price").getAsDouble();
            JsonObject building = ((JsonObject) fooditem).get("building").getAsJsonObject();
            System.out.println(building);
            String buildingname = building.get("name").getAsString();
            Food foodobj = new Food(id, foodname, fooddesc, price, building);
            HBox food = new HBox(20);
            food.getStyleClass().add("fooditem");
            food.setPadding(new Insets(0, 15, 0, 15));
            food.setPrefSize(300, 60);
            food.setAlignment(Pos.CENTER_LEFT);
            VBox namedesc = new VBox();
            Label name = new Label(foodname);
            Label desc = new Label(fooddesc);
            namedesc.getChildren().addAll(name, desc);
            Label priceLabel = new Label(price.toString());
            Button addToCart = new Button("+");
            addToCart.setOnAction(event -> addToCart(foodobj));
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            food.getChildren().add(namedesc);
            food.getChildren().add(region);
            food.getChildren().add(priceLabel);
            food.getChildren().add(addToCart);
            foodmenu.getChildren().add(food);
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
            timeselector.getChildren().clear();
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("Select delivery date");
            datePicker.setId("datePicker");
            timeselector.getChildren().add(datePicker);
            ComboBox timePicker = new ComboBox();
            timePicker.setPromptText("Select delivery time");
            timePicker.setId("timePicker");
            for (int i = 9; i < 20; i++) {
                timePicker.getItems().add(i + ":00");
            }
            timeselector.getChildren().add(timePicker);

        }
    }

    public void placeOrder() {
        DatePicker datePicker = (DatePicker) timeselector.lookup("#datePicker");
        ComboBox timePicker = (ComboBox) timeselector.lookup("#timePicker");
        if (datePicker.getValue() == null || timePicker.getSelectionModel().isEmpty()) {
            errorfield.setText("Please select a delivery time and date");
        } else {
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
                foods = foods + "{\"reservation\":null, \"reservable\":{\"id\":" + foodCart.get(i).getId()
                    + ",\"price\":" + foodCart.get(i).getPrice()
                    + ",\"building\":" + foodCart.get(i).getBuilding()
                    + ",\"reservations\":[]"
                    + "},\"amount\":" + foodamount
                    + end;
            }
            System.out.println(foods);
            JsonObject user = MainSceneController.user;
            String result = ServerCommunication.orderFoodBike(dateTime, user, foods);
            createAlert(result);
        }

    }

}
