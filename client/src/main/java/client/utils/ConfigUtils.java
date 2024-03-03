package client.utils;

import commons.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.UUID;

public class ConfigUtils {
    public ArrayList<Event> readResents(Reader resentsFile) {
        try(BufferedReader reader = new BufferedReader(resentsFile)) {
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
            return null;
        }
    }
}
