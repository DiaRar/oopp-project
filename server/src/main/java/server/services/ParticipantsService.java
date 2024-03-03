package server.services;

import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;

import java.util.UUID;

public class ParticipantsService {

    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;

    public ParticipantsService(ParticipantRepository participantRepository, EventRepository eventRepository) {
        this.participantRepository = participantRepository;
        this.eventRepository = eventRepository;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    private static void isValidParticipant(Participant participant) {
        if(participant.getId() != null)
            throw new IllegalArgumentException("Id is auto-generated, should not be given as parameter");
        if(isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())
                || isNullOrEmpty(participant.getEmail()))
            throw new IllegalArgumentException("Participant's attributes have null or empty values!");
    }
    public Participant getById(UUID id) {
        if(id == null || !participantRepository.existsById(id))
            throw new EntityNotFoundException("The entity with the provided Id does not exist in the database!");
        return participantRepository.findParticipantById(id);
    }
    public Event addParticipant(Participant participant, UUID eventID) {
        if(participant == null)
            throw new IllegalArgumentException("The participant parameter is null");
        isValidParticipant(participant);
        if(eventID == null || !eventRepository.existsById(eventID))
            throw new IllegalArgumentException("The event you are looking for does not exist!");
        Event event = eventRepository.findEventById(eventID);
        participant.setEvent(event);
        participantRepository.save(participant);
        event.addParticipant(participant);
        return eventRepository.save(event);
    }
    public Participant updateParticipant(UUID id, Participant participant) {
        if(id == null || !participantRepository.existsById(id))
            throw new IllegalArgumentException("The participant you are looking for does not exist!");
        isValidParticipant(participant);
        Participant repoParticipant = participantRepository.findParticipantById(id);
        repoParticipant.setEmail(participant.getEmail());
        repoParticipant.setFirstName(participant.getFirstName());
        repoParticipant.setLastName(participant.getLastName());
        return participantRepository.save(repoParticipant);
    }
    public Participant deleteParticipant(UUID id) {
        if(id == null || !participantRepository.existsById(id))
            throw new IllegalArgumentException("The participant you are looking for does not exist!");
        return participantRepository.deleteParticipantById(id);
    }
}
