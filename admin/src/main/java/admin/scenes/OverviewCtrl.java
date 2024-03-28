package admin.scenes;

import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class OverviewCtrl {
    private static final double FLOW_PANE_MARGIN = 5;
    @FXML
    public ListView<Event> listView;

    public void refreshOverview() {
        List<Event> recentEventsList = createMockEvents();
        ObservableList<Event> events = FXCollections.observableArrayList(recentEventsList);
        listView.setItems(events);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    BorderPane borderPane = generateEventWithRemoveButton(item);
                    setGraphic(borderPane);
                }
            }
        });
    }

    private BorderPane generateEventWithRemoveButton(Event event) {
        BorderPane borderPane = new BorderPane();

        // Event Title
        Text eventTitle = new Text(event.getName());
        TextFlow content = new TextFlow(eventTitle);
        HBox description = new HBox(content);
        HBox.setMargin(description, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));

        // Remove button
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            //TODO implement
        });

        // Combine event title and remove button
        VBox eventBox = new VBox(description, removeButton);
        borderPane.setCenter(eventBox);

        return borderPane;
    }

    //TODO REMOVE!!!
    private List<Event> createMockEvents() {
        List<Event> mockEvents = new ArrayList<>();

        // Adding mock events to the list
        mockEvents.add(new Event("Mock Event 1"));
        mockEvents.add(new Event("Mock Event 2"));
        // Add more mock events as needed

        return mockEvents;
    }
}
