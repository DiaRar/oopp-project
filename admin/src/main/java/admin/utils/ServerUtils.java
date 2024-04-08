package admin.utils;

import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ProcessingException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import admin.uicomponents.Alerts;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
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

    public List<Event> getEvents() {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/events/")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<List<Event>>() {});
        } catch (Exception ex) {
            handleConnectionException(ex);
            return null;
        }
    }

    public void deleteEvent(UUID id) {
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/events/" + id.toString())
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .delete();
        } catch (Exception ex) {
            handleConnectionException(ex);
        }
    }

    public String getExportResult() {
        try {
            return ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/json/export")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<String>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importDatabase(String json) {
        try {
            ClientBuilder.newClient(new ClientConfig())
                    .target(server)
                    .path("/api/json/import")
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .post(Entity.json(json));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
