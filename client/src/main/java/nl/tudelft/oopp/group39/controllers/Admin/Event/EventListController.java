package nl.tudelft.oopp.group39.controllers.Admin.Event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.Admin.AdminPanelController;
import nl.tudelft.oopp.group39.controllers.Admin.MainAdminController;
import nl.tudelft.oopp.group39.controllers.MainSceneController;
import nl.tudelft.oopp.group39.models.Event;
import nl.tudelft.oopp.group39.models.User;

public class EventListController extends AdminPanelController implements Initializable {

    @FXML
    private Button backbtn;
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> idCol;
    @FXML private TableColumn<Event, String> typeCol;
    @FXML private TableColumn<Event, String> startCol;
    @FXML private TableColumn<Event, String> endCol;
    @FXML
    private TableColumn<Event, Event> deleteCol = new TableColumn<>("Delete");
    @FXML
    private TableColumn<Event, Event> updateCol = new TableColumn<>("Update");
    @FXML
    private MenuBar NavBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadAllEvents();
            setNavBar(NavBar);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    void loadAllEvents() throws JsonProcessingException {

        String events = ServerCommunication.get(ServerCommunication.event);
        loadEvents(events);
    }
    /**
     * Display buildings and data in tableView buildingTable. -- Likely doesn't work yet.
     */
    void loadEvents(String events) throws JsonProcessingException {
        eventTable.setVisible(true);
        eventTable.getItems().clear();
        eventTable.getColumns().clear();

        ArrayNode body = (ArrayNode) mapper.readTree(events).get("body");
        events = mapper.writeValueAsString(body);
        Event[] list = mapper.readValue(events, Event[].class);
        ObservableList<Event> data = FXCollections.observableArrayList(list);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        deleteCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteCol.setCellFactory(param -> new TableCell<Event, Event>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Event eventItem, boolean empty) {
                super.updateItem(eventItem, empty);

                if (eventItem == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                    event -> {
                        try {
                            deleteEvent(eventItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> new TableCell<Event, Event>() {
            private final Button updateButton = new Button("Update");

            @Override
            protected void updateItem(Event eventItem, boolean empty) {
                super.updateItem(eventItem, empty);

                if (eventItem == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            editEvent(eventItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        });
        eventTable.setItems(data);
        eventTable.getColumns().addAll(idCol, typeCol, startCol, endCol, deleteCol, updateCol);
    }


    public void createEvent() throws IOException {
//        Stage currentstage = (Stage) backbtn.getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("/Admin/Event/EventCreate.fxml"));
//        currentstage.setScene(new Scene(root, 900, 650));
    }

    public void deleteEvent(Event event) throws IOException {
        String id = Integer.toString(event.getId());
        ServerCommunication.removeEvent(id);
        createAlert("removed: " + event.getType());
        loadAllEvents();
    }

    public void editEvent(Event event) throws IOException {
        FXMLLoader loader = switchFunc("/Admin/Event/EventEdit.fxml");
        EventEditController controller = loader.getController();
        controller.initData(event);
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack(ActionEvent actionEvent) throws IOException {
        switchFunc("/Admin/AdminPanel.fxml");
    }

    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}

