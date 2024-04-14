package admin.scenes;

import admin.uicomponents.DownloadButtonCell;
import admin.uicomponents.RemoveButtonCell;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OverviewCtrl implements Initializable {
    private static final Executor executor = Executors.newVirtualThreadPerTaskExecutor();
    private ServerUtils serverUtils;
    @FXML
    private TableView<Event> tableView;

    public static Executor getExecutor() {
        return executor;
    }

    @Inject
    public OverviewCtrl(ServerUtils serverUtils) {
        this.serverUtils = serverUtils;
    }

    public void fillEvents() {
        List<Event> events = serverUtils.getEvents();
        ObservableList<Event> eventList = FXCollections.observableArrayList(events);
        tableView.setItems(eventList);
    }

    public void download() {
            var json = serverUtils.getExportResult();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a path to download to:");
            String home = System.getProperty("user.home");
            fileChooser.setInitialDirectory(new File(home + "/Downloads/"));
            fileChooser.setInitialFileName("full.json");
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            executor.execute(() -> {
                try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public static File uploadSelector() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a JSON file to upload to the database");
        String home = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(home + "/Downloads/"));
        return fileChooser.showOpenDialog(null);
    }
    public void upload() {
        File selectedFile = uploadSelector();
        if (selectedFile != null) {
            try {
                String jsonData = Files.readString(selectedFile.toPath());
                serverUtils.importDatabase(jsonData);
            } catch (Exception e) {
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
                return;
            }
        }
    }
    public void uploadOne() {
        File selectedFile = uploadSelector();
        if (selectedFile == null) {
            return;
        }
        try {
            String jsonData = Files.readString(selectedFile.toPath());
            serverUtils.importEvent(jsonData);
        } catch (Exception e) {
            throw new RuntimeException("Error reading the selected file", e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final int secondsInAMinute = 60;
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
        removeColumn.setCellFactory(param -> new RemoveButtonCell(tableView, serverUtils));

        TableColumn<Event, Void> downloadColumn = new TableColumn<>("Download");
        downloadColumn.setCellFactory(param -> new DownloadButtonCell(tableView, serverUtils));

        tableView.getColumns().addAll(eventNameColumn, eventIdColumn, creationDateColumn, lastActionColumn,
                removeColumn, downloadColumn);

    }
}