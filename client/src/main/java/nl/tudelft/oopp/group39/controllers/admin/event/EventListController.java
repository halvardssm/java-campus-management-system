package nl.tudelft.oopp.group39.controllers.admin.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nl.tudelft.oopp.group39.communication.ServerCommunication;
import nl.tudelft.oopp.group39.controllers.admin.AdminPanelController;
import nl.tudelft.oopp.group39.models.Event;

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
    private MenuBar navBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadAllEvents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        setNavBar(navBar);
    }
    /**
     * Ensures that all events are put into table view.
     */

    void loadAllEvents() throws JsonProcessingException {

        String events = ServerCommunication.get(ServerCommunication.event);
        loadEvents(events);
    }
    /**
     * Display events and data in tableView named eventTable.
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
        deleteCol.setCellFactory(param -> returnCell("Delete"));
        updateCol.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        updateCol.setCellFactory(param -> returnCell("Update"));
        eventTable.setItems(data);
        eventTable.getColumns().addAll(idCol, typeCol, startCol, endCol, deleteCol, updateCol);
    }
    /**
     * Inserts the update and delete buttons into table.
     */

    public TableCell<Event, Event> returnCell(String button) {
        return new TableCell<>() {
            private final Button updateButton = new Button(button);

            @Override
            protected void updateItem(Event abcEvent, boolean empty) {
                super.updateItem(abcEvent, empty);

                if (abcEvent == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                    event -> {
                        try {
                            switch (button) {
                                case "Update":
                                    editEvent(abcEvent);
                                    break;
                                case "Delete":
                                    deleteEvent(abcEvent);
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                );
            }
        };
    }
    /**
     * Sends user to Create Event page.
     */

    public void createEvent() throws IOException {
        switchFunc("/admin/event/EventCreate.fxml");
    }

    /**
     * Deletes selected event.
     */

    public void deleteEvent(Event event) throws IOException {
        String id = Integer.toString(event.getId());
        ServerCommunication.removeEvent(id);
        createAlert("removed: " + event.getType());
        loadAllEvents();
    }

    /**
     * Sends user to the event edit page.
     */
    public void editEvent(Event event) throws IOException {
        FXMLLoader loader = switchFunc("/admin/event/EventEdit.fxml");
        EventEditController controller = loader.getController();
        controller.initData(event);
    }

    /**
     * Goes back to main admin panel.
     */
    @FXML
    private void switchBack() throws IOException {
        switchFunc("/admin/AdminPanel.fxml");
    }

    /**
     * Used to switch windows.
     */
    private FXMLLoader switchFunc(String resource) throws IOException {
        Stage currentstage = (Stage) backbtn.getScene().getWindow();
        return mainSwitch(resource, currentstage);
    }

}

