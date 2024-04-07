package admin.scenes;

import admin.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class OverviewCtrl {
    public ServerUtils serverUtils;
    @FXML
    public ListView<Event> listView;
    @Inject
    public OverviewCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }
    public void getItems() {
        List<Event> events = serverUtils.getEvents();
        for (Event event : events) {
            System.out.println(event);
        }
    }

    public void fillEvents() {
        List<Event> events = serverUtils.getEvents();
        Platform.runLater(() -> {
            var eventsList = FXCollections.observableArrayList(events);
            listView.setItems(eventsList);

            listView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Event item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        var tile = generateEventTitle(item);
                        setGraphic(tile);
                    }
                }
            });
        });
    }

    public void addEvent(Event event) {
        listView.getItems().add(event);
    }

    public void removeEvent(UUID eventID) {
        listView.getItems().removeIf(event -> event.getId().equals(eventID));
    }

    public void updateEvent(Event event) {
        System.out.println("Event: " + event.getName() + " changed");
    }

    public GridPane generateEventTitle(Event event) {
        // Constants for grid layout parameters
        final int COLUMN_EVENT_NAME_LABEL = 0;
        final int COLUMN_EVENT_NAME_VALUE = 1;
        final int COLUMN_EVENT_ID_LABEL = 2;
        final int COLUMN_EVENT_ID_VALUE = 3;
        final int COLUMN_CREATION_DATE_LABEL = 0;
        final int COLUMN_CREATION_DATE_VALUE = 1;
        final int COLUMN_LAST_ACTION_DATE_LABEL = 2;
        final int COLUMN_LAST_ACTION_DATE_VALUE = 3;
        final int COLUMN_REMOVE_BUTTON = 4;

        final Insets PADDING_INSETS = new Insets(10);
        final int HORIZONTAL_GAP = 10;
        final int VERTICAL_GAP = 5;
        final int FONT_SIZE = 14;
        final int SECONDS_IN_MINUTE = 60;


        // Create a GridPane to hold the event titles
        GridPane eventGrid = new GridPane();
        eventGrid.setAlignment(Pos.CENTER_LEFT);
        eventGrid.setPadding(PADDING_INSETS);
        eventGrid.setHgap(HORIZONTAL_GAP);
        eventGrid.setVgap(VERTICAL_GAP);

        // Create labels for event name, ID, creation date, last action date
        Label eventNameLabel = new Label("Event Name:");
        eventNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        eventNameLabel.setTextFill(Color.BLUE);

        Label eventNameValue = new Label(event.getName());
        eventNameValue.setFont(Font.font("Arial", FONT_SIZE));

        Label eventIdLabel = new Label("Event ID:");
        eventIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        eventIdLabel.setTextFill(Color.BLUE);

        Label eventIdValue = new Label(event.getId().toString());
        eventIdValue.setFont(Font.font("Arial", FONT_SIZE));

        Label creationDateLabel = new Label("Creation Date:");
        creationDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        creationDateLabel.setTextFill(Color.BLUE);

        // Format the creation date to show only the date
        LocalDate creationLocalDate = event.getCreationDate().toLocalDate();
        String creationDateString = creationLocalDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        Label creationDateValue = new Label(creationDateString);
        creationDateValue.setFont(Font.font("Arial", FONT_SIZE));

        Label lastActionDateLabel = new Label("Last Action:");
        lastActionDateLabel.setFont(Font.font("Arial", FontWeight.BOLD, FONT_SIZE));
        lastActionDateLabel.setTextFill(Color.BLUE);

        // Calculate the elapsed time since the last action
        LocalDateTime lastActionDateTime = event.getLastActivityDate();
        Duration timeElapsed = Duration.between(lastActionDateTime, LocalDateTime.now());
        long hours = timeElapsed.toHours();
        long minutes = timeElapsed.toMinutes() % SECONDS_IN_MINUTE;
        String elapsedTimeString = hours + " hours and " + minutes + " minutes ago";
        Label lastActionDateValue = new Label(elapsedTimeString);
        lastActionDateValue.setFont(Font.font("Arial", FONT_SIZE));

        // Create remove button
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            serverUtils.deleteEvent(event.getId());
        });

        // Add labels and remove button to the GridPane
        eventGrid.add(eventNameLabel, COLUMN_EVENT_NAME_LABEL, 0);
        eventGrid.add(eventNameValue, COLUMN_EVENT_NAME_VALUE, 0);
        eventGrid.add(eventIdLabel, COLUMN_EVENT_ID_LABEL, 0);
        eventGrid.add(eventIdValue, COLUMN_EVENT_ID_VALUE, 0);
        eventGrid.add(creationDateLabel, COLUMN_CREATION_DATE_LABEL, 1);
        eventGrid.add(creationDateValue, COLUMN_CREATION_DATE_VALUE, 1);
        eventGrid.add(lastActionDateLabel, COLUMN_LAST_ACTION_DATE_LABEL, 1);
        eventGrid.add(lastActionDateValue, COLUMN_LAST_ACTION_DATE_VALUE, 1);
        eventGrid.add(removeButton, COLUMN_REMOVE_BUTTON, 0);

        return eventGrid;
    }
}
