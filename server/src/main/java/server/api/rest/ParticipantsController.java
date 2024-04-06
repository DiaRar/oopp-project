package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Participant;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ParticipantsService;
import server.services.WebSocketUpdateService;

import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping("/api/events/{eventId}/participants")
public class ParticipantsController {
    private final ParticipantsService participantsService;
    private final WebSocketUpdateService updateService;

    public ParticipantsController(ParticipantsService participantsService, WebSocketUpdateService updateService) {
        this.participantsService = participantsService;
        this.updateService = updateService;
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
        Participant response = participantsService.updateParticipant(eventId, id, participant);
        updateService.sendUpdatedParticipant(eventId, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Void> deleteParticipant(@PathVariable UUID eventId, @PathVariable UUID id)
            throws IllegalArgumentException, EntityNotFoundException {
        participantsService.deleteParticipant(id);
        updateService.sendRemovedParticipant(eventId, id);
        return ResponseEntity.ok().build();
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
        Participant response = participantsService.addParticipant(eventId, participant);
        updateService.sendAddedParticipant(eventId, response);
        return ResponseEntity.ok(response);
    }
}
