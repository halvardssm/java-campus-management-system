package nl.tudelft.oopp.group39.views;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;

public class LandingPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Some Name");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.fitToWidthProperty().set(true);
        BorderPane borderPane = new BorderPane();
        HBox topBox = createTop();
        borderPane.setTop(topBox);
        FlowPane centerPane = createFlow();
        // centerPane.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(centerPane);
        borderPane.setCenter(scrollPane);

        primaryStage.setScene(new Scene(borderPane, 700, 600));
        primaryStage.show();

    }

    public GridPane createGridPane(Text title, Text desc) {
        GridPane grid = new GridPane();
        grid.setPrefSize(180, 220);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 10, 0, 10));
        Rectangle picture = new Rectangle(140, 100);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(picture, 1, 0 );
        grid.add(title, 1, 1, 2, 1);
        grid.add(desc, 1, 2,2,1);
        grid.setStyle("-fx-background-color: rgba(0,166,214,0.29);");
        return grid;
    }

    public FlowPane createFlow(){
        FlowPane flowpane = new FlowPane(20, 20);
        for (int i = 0; i < 10; i++) {
            flowpane.getChildren().add(createGridPane(new Text("Test" + (i + 1)), new Text("Some description\nCapacity:\nAddress:")));
        }
        flowpane.setPadding(new Insets(30,0,30,0));
        flowpane.setAlignment(Pos.TOP_CENTER);
        return flowpane;
    }

    public HBox createTop() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #00A6D6;");
        Text title = new Text("Some Title");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE);
        Button login = new Button("Login");
        login.setStyle("-fx-background-color: white; -fx-font-size: 16;");
        login.setPadding(new Insets(4, 10, 4, 10));
        login.setAlignment(Pos.CENTER_RIGHT);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        hbox.getChildren().addAll(title, region, login);
        HBox.setHgrow(login, Priority.ALWAYS);
        return hbox;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

