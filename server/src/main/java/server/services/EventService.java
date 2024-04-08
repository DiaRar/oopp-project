package server.services;

import commons.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        if (isNullOrEmpty(event.getName()))
            throw new IllegalArgumentException("No name found!");
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(UUID id) throws EntityNotFoundException {
        Optional<Event> oEv = eventRepository.findById(id);
        if (oEv.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return oEv.get();
    }

    public Event add(Event event) {
        isValidEvent(event);
        if (event.getId() != null)
            throw new IllegalArgumentException("Id is auto-generated, should not be given as parameter");
        return eventRepository.saveAndFlush(event);
    }

    public Event update(UUID id, Event event) throws EntityNotFoundException,
            IllegalArgumentException, NullPointerException {
        Event repoEvent = getById(id);
        if (event.getName() != null)
            repoEvent.setName(event.getName());
        eventRepository.flush();
        return repoEvent;
    }
    public Integer delete(UUID id) throws EntityNotFoundException, IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Integer deletedRows = eventRepository.deleteEventById(id);
        if (deletedRows != 1) {
            throw new EntityNotFoundException("Could not find the repo");
        }
        return deletedRows;
    }
}
