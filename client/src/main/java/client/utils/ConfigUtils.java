package client.utils;

import commons.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class ConfigUtils {
    private Reader recentsFile;

    public ArrayList<Event> readRecents() {
        try(BufferedReader reader = new BufferedReader(recentsFile)) {
            ArrayList<Event> events = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                Event newEvent = new Event();
                newEvent.setName(parts[0]);
                newEvent.setId(UUID.fromString(parts[1]));
                events.add(newEvent);
            }
            return events;
        }
        catch (IOException e) {
            //TODO Log error and handle
            throw new RuntimeException(e);
        }
    }

    public void setRecentsFile(Reader recentsFile) {
        this.recentsFile = recentsFile;
    }
}
