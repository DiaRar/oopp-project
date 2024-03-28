package admin.utils;

import com.google.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import java.io.IOException;
import admin.uicomponents.Alerts;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private final String server;
    private final Config config;
    @Inject
    public ServerUtils(Config config) throws IOException {
        this.config = config;
        this.server = config.getServer();
    }

    public void handleConnectionException(Exception ex) {
        if (ex instanceof ProcessingException) {
            Alerts.connectionRefusedAlert();
        } else {
            throw new NotFoundException(ex);
        }
    }

    public Boolean tryPassword(String password) {
        try {
            return ClientBuilder
                    .newClient(new ClientConfig())
                    .target(config.getServer())
                    .path("/api/admin/login/" + password)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(boolean.class);
        } catch (Exception ex) {
            handleConnectionException(ex);
            return null;
        }
    }

}
