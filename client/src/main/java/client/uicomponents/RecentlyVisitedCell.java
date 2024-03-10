package client.uicomponents;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class RecentlyVisitedCell extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            Label label = new Label(item);

            Button removeButton = new Button("Remove");
            removeButton.setOnAction(event -> {
                if (getListView() != null) {
                    getListView().getItems().remove(item);
                    //TODO remove page from csv
                }
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox hbox = new HBox(label, spacer, removeButton);
            hbox.setAlignment(Pos.CENTER_LEFT);

            setGraphic(hbox);
        }
    }
}
