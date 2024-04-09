package admin.scenes;

import admin.uicomponents.RemoveButtonCell;
import admin.utils.Config;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
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
        createJsonDumpRepo();
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

    private void createJsonDumpRepo() {
        var dir = new File(config.getJsonPath());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void upload() {
        // Create the UI components for uploading (e.g., FileChooser)
        createJsonDumpRepo();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a JSON file to upload to the database");
        fileChooser.setInitialDirectory(new File(config.getJsonPath()));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String jsonData = Files.readString(selectedFile.toPath());

                serverUtils.importDatabase(jsonData);
                this.tableView.setItems(null);
                this.fillEvents();
            } catch (IOException e) {
                throw new RuntimeException("Error reading the selected file", e);
            }
        } else {
            // User cancelled the file selection
            System.out.println("No file selected.");
        }
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