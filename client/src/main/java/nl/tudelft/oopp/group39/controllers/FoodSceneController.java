package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

public class FoodSceneController extends MainSceneController {
    @FXML
    private ComboBox buildingsList;

    @FXML
    private VBox cartlist;

    @FXML
    private Label emptycart;

    @FXML
    private FlowPane foodmenu;

    public double totalprice = 0;

    public int cartItems = 0;
    @FXML
    private Label total;

    public void getBuildingsList() {
        String buildingString = ServerCommunication.getBuildings();
        System.out.println(buildingString);

        JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
        JsonArray buildingArray = body.getAsJsonArray("body");

        for (JsonElement building : buildingArray) {
            String buildingName = ((JsonObject) building).get("name").getAsString();
            buildingsList.getItems().add(buildingName);
        }
    }

    public void getMenu() {
        foodmenu.getChildren().clear();
        GridPane food = new GridPane();
        food.getStyleClass().add("fooditem");
        food.setVgap(20);
        food.setHgap(20);
        food.setPadding(new Insets(0, 0, 0, 20));
        food.setPrefSize(300, 60);
        food.setAlignment(Pos.CENTER_LEFT);
        Label name = new Label("Food");
        Label price = new Label("$2.50");
        Button addToCart = new Button("+");
        addToCart.setOnAction(event -> addToCart("Food", 2.50));
        food.add(name, 0, 0);
        food.add(price, 1, 0);
        food.add(addToCart, 2, 0);
        foodmenu.getChildren().add(food);
    }

    public void addToCart(String name, double price) {
        if (cartlist.lookup("#" + name) == null) {
            GridPane fooditem = new GridPane();
            fooditem.setHgap(20);
            fooditem.setVgap(20);
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(name);
            SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            amount.valueProperty().addListener(
                (obs, oldValue, newValue) -> updateCart(name, price)
            );
            Label priceLabel = new Label("$" + price);
            priceLabel.setId(name + "price");
            Button delete = new Button();
            ImageView deletebtn = new ImageView(new Image("/icons/bin-icon.png"));
            deletebtn.setFitHeight(20);
            deletebtn.setFitWidth(20);
            delete.setGraphic(deletebtn);
            delete.setStyle("-fx-background-color: none");
            delete.setOnAction(event -> deleteFromCart(name));
            Label foodname = new Label(name);
            fooditem.add(foodname, 0, 0);
            fooditem.add(amount, 1, 0);
            fooditem.add(priceLabel, 2, 0);
            fooditem.add(delete, 3, 0);
            cartlist.getChildren().add(fooditem);
            cartItems++;
            totalprice += price;
            total.setText("$" + totalprice);
            checkEmptyCart();
        } else {
            Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + name);
            Integer value = amount.getValue() + 1;
            amount.getValueFactory().setValue(value);
            updateCart(name, price);
        }

    }

    public void updateCart(String name, double price) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + name);
        Integer value = amount.getValue();
        Label priceLabel = (Label) cartlist.lookup("#" + name + "price");
        double oldprice = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
        double newprice = value * price;
        priceLabel.setText("$" + newprice);
        totalprice = totalprice - oldprice + newprice;
        total.setText("$" + totalprice);
    }

    public void deleteFromCart(String name) {
        Label priceLabel = (Label) cartlist.lookup("#" + name + "price");
        double price = Double.parseDouble(priceLabel.getText().split("\\$")[1]);
        totalprice = totalprice - price;
        total.setText("$" + totalprice);
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + name);
        GridPane fooditem = (GridPane) amount.getParent();
        cartlist.getChildren().remove(fooditem);
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
        // to be implemented
    }

}
