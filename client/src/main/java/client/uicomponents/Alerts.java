package client.uicomponents;

import commons.Participant;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

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

    public static void configNotSetUpAlert() {
        alertUser(AlertType.ERROR, "Configuration Not Set Up",
                "Oops! Configuration not found or invalid.",
                "The configuration file is not set up properly or could not be found at the specified path:\n" +
                        "/client/src/main/resources/config/config.properties\n" +
                        "Please ensure that the configuration file exists and is correctly configured.");
    }

    public static void invalidParticipantAlert(String message) {
        alertUser(AlertType.WARNING, "Invalid Participant",
                message,
                "The data you have entered is invalid. Please update it and try again.");
    }

    public static boolean deleteParticipantAlert(Participant participant) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Deleting Participant");
        alert.setHeaderText("Are you really sure you want to remove " + participant.getNickname() + " from the event?");
        alert.setContentText("Removing a Participant is PERMANENT, you can't restore them.\n" +
                "This operation will also DELETE ALL EXPENSES made by " + participant.getNickname() + ".");
        Optional<ButtonType> result = alert.showAndWait();
        return (result.filter(buttonType -> buttonType == ButtonType.OK).isPresent());
    }

    public static void invalidExpenseAlert(String message) {
        alertUser(AlertType.WARNING, "Invalid Expense",
                message,
                "The data you have entered is invalid. Please update it and try again.");
    }
}
