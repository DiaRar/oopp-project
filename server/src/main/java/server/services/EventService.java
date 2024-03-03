package server.services;

import commons.Event;
import org.springframework.stereotype.Service;
import server.database.EventRepository;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> getAll() {
        return repo.findAll();
    }

    public Event getById(UUID id) {
        return repo.findById(id).get();
    }

    public Event add(Event event) {
        return repo.save(event);
    }

    public Event update(UUID id, Event event) {
        repo.deleteById(id);
        event.setId(id);
        return repo.save(event);
    }
}
