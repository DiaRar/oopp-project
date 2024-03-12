package server.services;

import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantsService {

    private final ParticipantRepository participantRepository;
    public ParticipantsService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
    private static void isValidParticipant(Participant participant) {
        if (isNullOrEmpty(participant.getNickname()))
            throw new IllegalArgumentException("Participant's attributes have null or empty values!");
    }
    private static void isValidParticipant(UUID id, Participant participant) throws IllegalArgumentException {
        isValidParticipant(participant);
        if (!participant.getId().equals(id))
            throw new IllegalArgumentException("Participant's id and path id are not equal!");
    }
    public Participant getById(UUID id) throws EntityNotFoundException {
        Optional<Participant> oPa = participantRepository.findById(id);
        if (oPa.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified participant.");
        }
        return oPa.get();

    }
    public Participant updateParticipant(UUID eventId, UUID id, Participant participant)
            throws IllegalArgumentException, EntityNotFoundException {
        if (participant.getId() == null)
            participant.setId(id);
        isValidParticipant(id, participant);
        if (!participantRepository.existsById(id))
            throw new EntityNotFoundException("Did not find the specified participant!");
        Event event = new Event();
        event.setId(eventId);
        participant.setEvent(event);
        return participantRepository.save(participant);
    }
    public Participant deleteParticipant(UUID id) throws IllegalArgumentException, EntityNotFoundException {
        if (id == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Optional<Participant> oParticipant = participantRepository.deleteParticipantById(id);
        if (oParticipant.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified participant!");
        }
        return oParticipant.get();
    }

    public List<Participant> getParticipants(UUID eventId) {
        return participantRepository.findParticipantsByEventId(eventId);
    }
    public Participant addParticipant(UUID eventId, Participant participant) throws IllegalArgumentException {
        isValidParticipant(participant);
        Event event = new Event();
        event.setId(eventId);
        participant.setEvent(event);
        return participantRepository.save(participant);
    }
}
