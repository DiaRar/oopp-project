package admin.scenes;

import admin.uicomponents.RemoveButtonCell;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    public TableView<Event> tableView;
    @Inject
    public OverviewCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void fillEvents() {
        List<Event> events = serverUtils.getEvents();
        ObservableList<Event> eventList = FXCollections.observableArrayList(events);
        tableView.setItems(eventList);

        TableColumn<Event, String> eventNameColumn = new TableColumn<>("Event Name");
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Event, String> eventIdColumn = new TableColumn<>("Event ID");
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Event, LocalDateTime> creationDateColumn = new TableColumn<>("Creation Date");
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        creationDateColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        TableColumn<Event, LocalDateTime> lastActionColumn = new TableColumn<>("Last Action");
        lastActionColumn.setCellValueFactory(new PropertyValueFactory<>("lastActivityDate"));
        lastActionColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    Duration duration = Duration.between(item, LocalDateTime.now());
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60;
                    setText(hours + "h " + minutes + "m ago");
                }
            }
        });

        TableColumn<Event, Void> removeColumn = new TableColumn<>("Remove");
        removeColumn.setCellFactory(param -> new RemoveButtonCell(tableView));

        tableView.getColumns().addAll(eventNameColumn, eventIdColumn, creationDateColumn, lastActionColumn, removeColumn);
    }

    public void addEvent(Event event) {
        tableView.getItems().add(event);
    }

    public void removeEvent(UUID eventId) {
        tableView.getItems().removeIf(event -> event.getId().equals(eventId));
    }

    public void updateEvent(Event updatedEvent) {
        ObservableList<Event> items = tableView.getItems();
        for (int i = 0; i < items.size(); i++) {
            Event event = items.get(i);
            if (event.getId().equals(updatedEvent.getId())) {
                items.set(i, updatedEvent);
                System.out.println("Event: " + updatedEvent.getName() + " updated");
            }
        }
    }
}