package admin.scenes;

import admin.uicomponents.RemoveButtonCell;
import admin.utils.Config;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class OverviewCtrl {
    private ServerUtils serverUtils;
    private Config config;
    @FXML
    private TableView<Event> tableView;
    @Inject
    public OverviewCtrl(ServerUtils serverUtils, Config config) {
        this.serverUtils = serverUtils;
        this.config = config;
    }

    public void fillEvents() {
        final int secondsInAMinute = 60;

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
                    long minutes = duration.toMinutes() % secondsInAMinute;
                    setText(hours + "h " + minutes + "m ago");
                }
            }
        });

        TableColumn<Event, Void> removeColumn = new TableColumn<>("Remove");
        removeColumn.setCellFactory(param -> new RemoveButtonCell(tableView));

        tableView.getColumns().addAll(eventNameColumn, eventIdColumn, creationDateColumn, lastActionColumn, removeColumn);
    }

    public void download() {
        var json = serverUtils.getExportResult();
        var file = new File(config.getJsonPath(), "export.json");
        if (file.exists()) file.delete();

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(new File(config.getJsonPath()));
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void upload() {
        System.out.println("importing");
        serverUtils.importDatabase("{\"bankAccounts\":[{\"iban\":\"232\",\"bic\":\"434\"},{\"iban\":\"what eban\",\"bic\":\"bick\"},{\"iban\":\"second iban\",\"bic\":\"second bic\"}],\"events\":[{\"id\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\",\"name\":\"yes\",\"creationDate\":[2024,4,7,19,14,46,982485000],\"lastActivityDate\":[2024,4,7,19,14,46,982485000]},{\"id\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\",\"name\":\"new event\",\"creationDate\":[2024,4,7,19,58,52,516895000],\"lastActivityDate\":[2024,4,7,19,58,52,516895000]}],\"debts\":[{\"debtorId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"payerId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"amount\":0.0,\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\"},{\"debtorId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"amount\":0.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"debtorId\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"amount\":-15.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"debtorId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"payerId\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"amount\":15.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"}],\"expenses\":[{\"id\":\"22659b2e-7253-4f44-99a4-d24924044622\",\"amount\":34.0,\"title\":\"fdw\",\"date\":[2024,4,3,0,0],\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\",\"payerId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\"},{\"id\":\"4c9ecf43-6d57-4d66-bda2-0a06d68ce6a3\",\"amount\":30.0,\"title\":\"beans\",\"date\":[2024,4,3,0,0],\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\"}],\"participants\":[{\"id\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"email\":\"dsajkda\",\"nickname\":\"hello \",\"bankIBAN\":\"232\",\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\"},{\"id\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"email\":\"jon email\",\"nickname\":\"johnavan\",\"bankIBAN\":\"what eban\",\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"id\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"email\":\"second email\",\"nickname\":\"second johnavan\",\"bankIBAN\":\"second iban\",\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"}],\"tags\":[]}\n");

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