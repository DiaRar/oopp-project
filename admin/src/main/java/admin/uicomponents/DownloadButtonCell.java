package admin.uicomponents;

import admin.scenes.OverviewCtrl;
import admin.utils.Config;
import admin.utils.ServerUtils;
import commons.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

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

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(downlodButton);
        }
    }
    public void download(UUID eventID) {
        var json = serverUtils.getExportEvent(eventID);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a path to download to:");
        String home = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(home + "/Downloads/"));
        fileChooser.setInitialFileName(eventID.toString() + ".json");
        File file = fileChooser.showSaveDialog(null);
        if (file == null)
            return;
        OverviewCtrl.getExecutor().execute(() -> {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        });
    }
}
