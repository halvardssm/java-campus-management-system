package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.group39.communication.ServerCommunication;

import java.util.concurrent.atomic.AtomicBoolean;

public class FoodSceneController extends MainSceneController {
    @FXML
    private ComboBox buildingsList;

    @FXML
    private VBox cartlist;

    @FXML
    private Label emptycart;

    @FXML
    private FlowPane foodmenu;

    public int cartItems = 0;

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
        food.setVgap(20);
        food.setHgap(20);
        Label name = new Label("Food");
        Label price = new Label("2,50 $");
        Button addToCart = new Button("+");
        addToCart.setOnAction(event -> addToCart("Food", 2.5));
        food.add(name, 0,0);
        food.add(price, 1,0);
        food.add(addToCart, 2,0);
        foodmenu.getChildren().add(food);
    }

    public void addToCart(String name, double price) {
        if(cartlist.lookup("#" + name) == null){
            GridPane fooditem = new GridPane();
            fooditem.setHgap(20);
            fooditem.setVgap(20);
            Label foodname = new Label(name);
            Spinner<Integer> amount = new Spinner<>();
            amount.setPrefWidth(50);
            amount.setId(name);
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 1);
            amount.setValueFactory(valueFactory);
            Label priceLabel = new Label(price + "$");
            priceLabel.setId(name + "price");
            Button delete = new Button("delete");
            delete.setOnAction(event -> deleteFromCart(name));
            fooditem.add(foodname, 0,0);
            fooditem.add(amount, 1,0);
            fooditem.add(priceLabel,2,0);
            fooditem.add(delete, 3, 0);
            cartlist.getChildren().add(fooditem);
            cartItems++;
            checkEmptyCart();
        }
        else{
            updateCart(name, price);
        }

    }

    public void updateCart(String name, double price) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + name);
        Integer value = amount.getValue() + 1;
        amount.getValueFactory().setValue(value);
        Label priceLabel = (Label) cartlist.lookup("#" + name + "price");
        double newprice = value * price;
        priceLabel.setText(newprice + "$");
    }

    public void deleteFromCart(String name) {
        Spinner<Integer> amount = (Spinner<Integer>) cartlist.lookup("#" + name);
        GridPane fooditem = (GridPane) amount.getParent();
        cartlist.getChildren().remove(fooditem);
        cartItems--;
        checkEmptyCart();
    }

    public void checkEmptyCart() {
        if(cartItems == 0){
            cartlist.getChildren().add(emptycart);
        }
        else {
            cartlist.getChildren().remove(emptycart);
        }
    }
}
