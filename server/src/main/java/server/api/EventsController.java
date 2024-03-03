package server.api;

import commons.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventsController {
    private final EventRepository eventRepository;

    public EventsController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventRepository.findEventById(id));
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepository.save(event));
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Collection<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }
}
