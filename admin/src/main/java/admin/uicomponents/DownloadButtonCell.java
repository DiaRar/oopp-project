package admin.uicomponents;

import admin.utils.Config;
import admin.utils.ServerUtils;
import commons.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class DownloadButtonCell extends TableCell<Event, Void> {
    private final Button downlodButton;
    private final ServerUtils serverUtils;
    private final Config config;

    public DownloadButtonCell(TableView<Event> tableView, ServerUtils serverUtils, Config config) {
        this.downlodButton = new Button("Download");
        this.serverUtils = serverUtils;
        this.config = config;
        this.downlodButton.setOnAction(event -> {
            Event eventToDownload = getTableView().getItems().get(getIndex());
            try {
                download(eventToDownload.getId());
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    private void createJsonDumpRepo() {
        var dir = new File(config.getJsonPath());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public void download(UUID eventID) {
        var json = serverUtils.getExportEvent(eventID);
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
}
