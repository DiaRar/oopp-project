package admin.uicomponents;

import commons.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

public class RemoveButtonCell extends TableCell<Event, Void> {
    private final Button removeButton;

    public RemoveButtonCell(TableView<Event> tableView) {
        this.removeButton = new Button("Remove");
        this.removeButton.setOnAction(event -> {
            Event eventToRemove = getTableView().getItems().get(getIndex());
            tableView.getItems().remove(eventToRemove);
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(removeButton);
        }
    }
}
