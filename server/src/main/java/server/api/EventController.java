package server.api;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import commons.Event;
import server.services.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(path = {"", "/"})
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(eventService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> add(@RequestBody Event event) {
        try {
            Event saved = eventService.add(event);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable("id") UUID id, @RequestBody Event event) {
        try {
            Event updated = eventService.update(id, event);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
