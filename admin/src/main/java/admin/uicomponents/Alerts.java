package admin.uicomponents;

import javafx.scene.control.Alert;

public class Alerts {
    private static void alertUser(Alert.AlertType alertType, String title, String header, String context) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }
    public static void configNotSetUpAlert() {
        alertUser(Alert.AlertType.ERROR, "Configuration Not Set Up",
                "Oops! Configuration not found or invalid.",
                "The configuration file is not set up properly or could not be found at the specified path:\n" +
                        "/client/src/main/resources/config/config.properties\n" +
                        "Please ensure that the configuration file exists and is correctly configured.");
    }

    public static void connectionRefusedAlert() {
        alertUser(Alert.AlertType.ERROR, "Connection Refused", "Oops! Unable to connect to the server.",
                "The server may be offline or unreachable. Please try again later.");
    }

    public static void incorrectPasswordAlert() {
        alertUser(Alert.AlertType.ERROR, "Incorrect Password", "Oops! Incorrect Password.",
                "The password you entered is incorrect. Please try again.");
    }

    public static void emptyPasswordAlert() {
        alertUser(Alert.AlertType.ERROR, "Empty Password", "Oops! Password cannot be empty.",
                "Please provide a password.");
    }
}
