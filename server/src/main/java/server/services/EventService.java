package server.services;

import commons.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    private static void isValidEvent(Event event) {
        if (event == null)
            throw new NullPointerException("Event is null!");
        if (isNullOrEmpty(event.getName()))
            throw new IllegalArgumentException("No name found!");
    }

    private static void isValidEvent(UUID id, Event event) {
        isValidEvent(event);
        if (!event.getId().equals(id))
            throw new IllegalArgumentException("Id of Event and Id from Path are not equal!");
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(UUID id) throws EntityNotFoundException {
        Optional<Event> oEv = eventRepository.findById(id);
        if (oEv.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified event.");
        }
        return oEv.get();
    }

    public Event add(Event event) {
        isValidEvent(event);
        if (event.getId() != null)
            throw new IllegalArgumentException("Id is auto-generated, should not be given as parameter");
        return eventRepository.save(event);
    }

    public Event update(UUID id, Event event) throws EntityNotFoundException,
            IllegalArgumentException, NullPointerException {
        getById(id);
        isValidEvent(id, event);
        return eventRepository.save(event);
    }
}
