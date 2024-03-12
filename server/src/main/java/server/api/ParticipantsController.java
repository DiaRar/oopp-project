package server.api;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Participant;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ParticipantsService;

import java.util.*;

@RestController
@RequestMapping("/api/events/{eventId}/participants")
public class ParticipantsController {
    private final ParticipantsService participantsService;

    public ParticipantsController(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }
    @GetMapping("/{id}")
    @JsonView(View.ParticipantView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<Participant> getById(@PathVariable("id") UUID id) throws EntityNotFoundException {
        return ResponseEntity.ok(participantsService.getById(id));
    }

    @PutMapping("/{id}")
    @JsonView(View.ParticipantView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Participant> updateParticipant(@PathVariable UUID eventId, @PathVariable UUID id,
                                                         @RequestBody Participant participant)
            throws IllegalArgumentException, EntityNotFoundException {
        return ResponseEntity.ok(participantsService.updateParticipant(eventId, id, participant));
    }

    @DeleteMapping("/{id}")
    @JsonView(View.ParticipantView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable UUID eventId, @PathVariable UUID id)
            throws IllegalArgumentException, EntityNotFoundException {
        return ResponseEntity.ok(participantsService.deleteParticipant(id));
    }

    @GetMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<List<Participant>> getParticipants(@PathVariable UUID eventId) {
        return ResponseEntity.ok(participantsService.getParticipants(eventId));
    }
    @PostMapping(path = {"", "/"})
    @JsonView(View.ParticipantView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Participant> addParticipant(@PathVariable UUID eventId, @RequestBody Participant participant)
            throws IllegalArgumentException {
        return ResponseEntity.ok(participantsService.addParticipant(eventId, participant));
    }
}
