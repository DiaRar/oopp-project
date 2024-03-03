package server.api;

import commons.Event;
import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/participant")
public class ParticipantController {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;

    public ParticipantController(EventRepository repo, ParticipantRepository participantRepository) {
        this.eventRepository = repo;
        this.participantRepository = participantRepository;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Collection<Participant>> getAll(@RequestBody UUID id) {

        if(id == null || !eventRepository.existsById(id))
            return ResponseEntity.badRequest().build();
        Event event = eventRepository.findEventById(id);
        return ResponseEntity.ok(participantRepository.findParticipantByEvent(event));
    }
}
