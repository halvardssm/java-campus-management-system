package nl.tudelft.oopp.group39.admin.reservable.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.admin.reservable.food.FoodListController;
import nl.tudelft.oopp.group39.building.model.Building;
import nl.tudelft.oopp.group39.reservable.model.Bike;
import nl.tudelft.oopp.group39.reservable.model.Food;
import nl.tudelft.oopp.group39.reservable.model.Reservable;
import nl.tudelft.oopp.group39.reservation.model.ReservableNode;
import nl.tudelft.oopp.group39.reservation.model.Reservation;
import nl.tudelft.oopp.group39.server.communication.ServerCommunication;

public class ReservationItemEditController extends ReservationItemListController {

    private long reservation;
    private ReservableNode reservableNode;
    private int amount;
    private Stage currentStage;
    private ObjectMapper mapper = new ObjectMapper();
    private Food food;
    private HashMap<Reservable, Long> reservableIdMap = new HashMap<>();
    private HashMap<Long, Reservable> reservableMap = new HashMap<>();
    @FXML
    private Button backbtn;
    @FXML
    private TextField amountField;
    @FXML
    private ComboBox<Integer> reservableBox;
    @FXML
    private MenuBar navBar;



    /**
     * Initializes the data of a User and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */

    public void initData(ReservableNode reservableNode, long reservation) throws JsonProcessingException {
        this.reservation = reservation;
        this.currentStage = (Stage) backbtn.getScene().getWindow();
        setNavBar(navBar, currentStage);
        this.reservableNode = reservableNode;
        ObservableList<Integer> data = initReservables();
        reservableBox.setItems(data);
        reservableBox.getSelectionModel().select(reservableNode.getId());
        this.amount = reservableNode.getAmount();
        amountField.setPromptText(Integer.toString(amount));
    }

    /**
     * Initializes the data of a building and makes it usable.
     * @throws JsonProcessingException when there is a processing exception.
     */
    public ObservableList<Integer> initReservables() throws JsonProcessingException {
        String bikes = ServerCommunication.get(ServerCommunication.bike);
        ArrayNode body = (ArrayNode) mapper.readTree(bikes).get("body");
        bikes = mapper.writeValueAsString(body);
        Bike[] bikeList = mapper.readValue(bikes, Bike[].class);
        List<Integer> reservablesList = new ArrayList<>();
        for (Bike bike : bikeList) {
            reservablesList.add(Integer.valueOf(Long.toString(bike.getId())));
        }
        String foodItems = ServerCommunication.get(ServerCommunication.food);
        body = (ArrayNode) mapper.readTree(foodItems).get("body");
        foodItems = mapper.writeValueAsString(body);
        Food[] foodItemList = mapper.readValue(foodItems, Food[].class);
        for (Food food : foodItemList) {
            reservablesList.add(Integer.valueOf(Long.toString(food.getId())));
        }
        return FXCollections.observableArrayList(reservablesList);
    }

    /**
     * Goes back to main User panel.
     */

    @FXML
    private void getBack() throws IOException {
        FXMLLoader loader = switchFunc("/admin/reservable/ReservationItemList.fxml");
        ReservationItemListController controller = loader.getController();
        controller.customInit(getReservables(ServerCommunication.getReservation(reservation).getReservationAmounts()), reservation);
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

    public List<ReservableNode> getReservables(ArrayNode reservationAmounts) {
        List<ReservableNode> reservableNodeList = new ArrayList<>();
        ReservableNode rs = new ReservableNode();
        for (JsonNode object : reservationAmounts) {
            Integer id = object.get("id").asInt();
            Integer amount = object.get("amount").asInt();
            Integer reservable = object.get("reservable").asInt();
            System.out.println(id + " : " + amount + " : " + reservable);
            rs = new ReservableNode(id, amount, reservable);
            reservableNodeList.add(rs);
        }
        return reservableNodeList;
    }
    /**
     * Edits user values and sends them to database.
     */

    public void editReservation() throws IOException {
        String amountInput = amountField.getText();
        amountInput = amountInput.contentEquals("") ? Integer.toString(amount) : amountInput;
        Integer reservationInput = reservableBox.getValue();
        Reservation res = ServerCommunication.getReservation(reservation);
        ReservableNode resNode = reservableNode;
        String json = "{ \"id\" : " + resNode.getId() + ", \"amount\" : " + Integer.valueOf(amountInput) + ", \"reservable\" : " + reservationInput + "} ";
        JsonNode nNode = mapper.readTree(json);
        res.getReservationAmounts().remove(reservableNode.getId());
        res.setReservationAmounts(res.getReservationAmounts().insert(resNode.getId(),nNode));
        System.out.println(res.getReservationAmounts().toString() + " ");
        ServerCommunication.updateReservation(res, reservation);
        System.out.println(ServerCommunication.getReservation(reservation).getReservationAmounts().toString());
        getBack();
    }

    public Double getPrice(String first, String second) {
        return Double.valueOf(first + "." + second);
    }

}
