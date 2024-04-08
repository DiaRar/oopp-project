package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Event;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.EventService;
import server.services.WebSocketUpdateService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final WebSocketUpdateService updateService;

    public EventController(EventService eventService, WebSocketUpdateService updateService) {
        this.eventService = eventService;
        this.updateService = updateService;
    }

    @GetMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}/basic")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Event> getByIdBasic(@PathVariable("id") UUID id) throws EntityNotFoundException {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @GetMapping("/{id}")
    @JsonView(View.OverviewView.class)
    @Cacheable(value = "events", key = "#id")
    public ResponseEntity<Event> getById(@PathVariable("id") UUID id) throws EntityNotFoundException {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PostMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Event> create(@RequestBody Event event) {
        Event saved = eventService.add(event);
        updateService.sendNewEvent(event);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @CachePut(value = "events", key = "#id")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Event> update(@PathVariable("id") UUID id, @RequestBody Event event)
            throws EntityNotFoundException, IllegalArgumentException, NullPointerException {
        Event updated = eventService.update(id, event);
        updateService.sendUpdatedEvent(updated);
        updateService.sendUpdateEvent(updated);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    @CacheEvict(value = "events", key = "#id")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id)
            throws EntityNotFoundException, IllegalArgumentException {
        eventService.delete(id);
        updateService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
