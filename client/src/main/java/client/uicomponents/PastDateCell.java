package client.uicomponents;

import javafx.scene.control.DateCell;

import java.time.LocalDate;

public class PastDateCell extends DateCell {

    @Override
    public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        setDisable(empty || date.isAfter(LocalDate.now()));
    }
}
