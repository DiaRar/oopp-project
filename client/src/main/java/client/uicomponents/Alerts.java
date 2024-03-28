package client.uicomponents;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
    private static void alertUser(AlertType alertType, String title, String header, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    public static void eventDeletedAlert() {
        alertUser(AlertType.WARNING, "Event Status", "Attention! Event might have been deleted.",
                "The event you are trying to access might have been deleted or is no longer available.");
    }

    public static void invalidUUIDAlert() {
        alertUser(AlertType.WARNING, "Invalid UUID Format", "Oops! Invalid UUID format.",
                "Please ensure your UUID follows the correct format:\n" +
                        "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
    }

    public static void emptyNameAlert() {
        alertUser(AlertType.WARNING, "Empty Name", "Oops! Name cannot be empty.",
                "Please provide a valid name for the event.");
    }

    public static void connectionRefusedAlert() {
        alertUser(AlertType.ERROR, "Connection Refused", "Oops! Unable to connect to the server.",
                "The server may be offline or unreachable. Please try again later.");
    }
}
