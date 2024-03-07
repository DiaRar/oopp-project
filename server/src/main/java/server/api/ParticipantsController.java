package server.api;

import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ParticipantsService;

import java.util.*;

@RestController
@RequestMapping("api/participants")
public class ParticipantsController {
    private final ParticipantsService participantsService;

    public ParticipantsController(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(participantsService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable UUID id, @RequestBody Participant participant) {
        try {
            return ResponseEntity.ok(participantsService.updateParticipant(id, participant));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(participantsService.deleteParticipant(id));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Participant>> getParticipants() {
        try {
            return ResponseEntity.ok(participantsService.getParticipants());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
