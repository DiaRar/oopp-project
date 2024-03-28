package client.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import commons.Event;

import java.io.*;
import java.util.*;

public class ConfigUtils {
    private File recentsFile;

    public List<Event> readRecents() {
        createFileIfNotExists();
        List<Event> events = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(recentsFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                Event newEvent = new Event();
                newEvent.setName(line[0]);
                newEvent.setId(UUID.fromString(line[1]));
                events.add(newEvent);
            }
        } catch (IOException | CsvValidationException e) {
            // TODO Log error and handle
            throw new RuntimeException(e);
        }
        return events;
    }

    public void addRecent(Event event) {
        createFileIfNotExists();
        List<Event> events = readRecents();

        // Check if the event already exists
        if (events.stream().anyMatch(e -> e.getId().equals(event.getId()))) {
            return;
        }

        // Add the new event to the beginning of the list
        events.add(0, event);

        try (CSVWriter writer = new CSVWriter(new FileWriter(recentsFile))) {
            // Write all events to the file
            for (Event e : events) {
                writer.writeNext(new String[]{e.getName(), e.getId().toString()});
            }
        } catch (IOException e) {
            // TODO Log error and handle
            throw new RuntimeException(e);
        }
    }

    public void removeRecent(UUID uuidToRemove) {
        List<Event> events = readRecents();
        events.removeIf(event -> event.getId().equals(uuidToRemove));
        try (CSVWriter writer = new CSVWriter(new FileWriter(recentsFile))) {
            for (Event event : events) {
                writer.writeNext(new String[]{event.getName(), event.getId().toString()});
            }
        } catch (IOException e) {
            // TODO Log error and handle
            throw new RuntimeException(e);
        }
    }

    private void createFileIfNotExists() {
        try {
            if (!recentsFile.exists()) {
                recentsFile.createNewFile();
            }
        } catch (IOException e) {
            // TODO Log error and handle
            throw new RuntimeException(e);
        }
    }

    public void setRecentsFile(File recentsFile) {
        this.recentsFile = recentsFile;
    }
}
