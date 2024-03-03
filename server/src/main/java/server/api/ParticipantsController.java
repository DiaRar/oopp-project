package server.api;

import commons.Event;
import commons.Participant;
import jakarta.persistence.Id;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.QuoteRepository;

import java.util.*;

@RestController
@RequestMapping( "api/participants")
public class ParticipantsController {
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    public ParticipantsController(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    private static boolean isValidPariticpant(Participant participant) {
        if(isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())
                || isNullOrEmpty(participant.getEmail()))
            return false;
        return true;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Participant> getById(@PathVariable("id") UUID id) {
        if(id == null || !participantRepository.existsById(id))
            return ResponseEntity.badRequest().build();
        Participant participant = participantRepository.findParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Event> addParticipant(@RequestBody Participant participant, @RequestBody UUID eventID) {
        if(participant == null || eventID == null || !eventRepository.existsById(eventID)
                || !isValidPariticpant(participant))
            return ResponseEntity.badRequest().build();
        Event event = eventRepository.findEventById(eventID);
        participant.setEvent(event);
        participantRepository.save(participant);
        event.addParticipant(participant);
        return ResponseEntity.ok(eventRepository.save(event));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable UUID id, @RequestBody Participant participant) {
        if(id == null || !participantRepository.existsById(id) || !isValidPariticpant(participant))
            return ResponseEntity.badRequest().build();
        Participant repoParticipant = participantRepository.findParticipantById(id);
        repoParticipant.setEmail(participant.getEmail());
        repoParticipant.setFirstName(participant.getFirstName());
        repoParticipant.setLastName(participant.getLastName());
        return ResponseEntity.ok(participantRepository.save(repoParticipant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Participant> deleteParticipant(@PathVariable UUID id) {
        if(id == null || !participantRepository.existsById(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(participantRepository.deleteParticipantById(id));
    }

}
