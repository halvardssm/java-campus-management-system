package nl.tudelft.oopp.group39.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.views.UsersDisplay;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class BuildingSceneController extends MainSceneController implements Initializable {

    @FXML
    private FlowPane flowPane;

    @FXML
    private GridPane newBuilding;

    @FXML
    private Button topbtn;


    public void refreshBuildings() {
//        System.out.println(flowPane);
//        System.out.println(newBuilding);
        flowPane.getChildren().clear();
        try {
            String buildingString = ServerCommunication.getBuildings();

            JsonObject body = ((JsonObject) JsonParser.parseString(buildingString));
            JsonArray buildingArray = body.getAsJsonArray("body");

//            JsonArray buildingArray = (JsonArray) JsonParser.parseString(room);
//            System.out.println(buildingString);
            for (JsonElement building : buildingArray) {
                newBuilding = FXMLLoader.load(getClass().getResource("/buildingCell.fxml"));

                Label name = (Label) newBuilding.lookup("#bname");
                name.setText(((JsonObject) building).get("name").getAsString());

                String bDetails = ((JsonObject) building).get("location").getAsString()
                        + "\n" + ((JsonObject) building).get("description").getAsString()
                        + "\n" + "Max. Capacity"
                        + "\n" + "Opening times: " + ((JsonObject) building).get("open").getAsString()
                        + " - " + ((JsonObject) building).get("closed").getAsString();

                Label details = (Label) newBuilding.lookup("#bdetails");
                details.setText(bDetails);

                flowPane.getChildren().add(newBuilding);
                System.out.println("zou goed moeten gaan");
//                System.out.println(newBuilding);
            }
        } catch (IOException e) {
            createAlert("Error: Wrong IO");
        }
    }

    public void alertAllBuildings() {
        try {
            createAlert("Users shown.",ServerCommunication.getBuildings());
        } catch (Exception e) {
            createAlert("Error Occurred.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializing");
//        System.out.println(flowPane);
//        System.out.println(newBuilding);
        refreshBuildings();
        changeBtn(topbtn);
    }

}
