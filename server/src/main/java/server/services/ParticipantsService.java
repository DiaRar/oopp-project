package server.services;

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
        if (participant.getId() != null)
            throw new IllegalArgumentException("Id is auto-generated, should not be given as parameter");
        if (isNullOrEmpty(participant.getFirstName()) || isNullOrEmpty(participant.getLastName())
                || isNullOrEmpty(participant.getEmail()))
            throw new IllegalArgumentException("Participant's attributes have null or empty values!");
    }
    public Participant getById(UUID id) {
        Optional<Participant> oPa = participantRepository.findById(id);
        if (oPa.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified participant.");
        }
        return oPa.get();

    }
    public Participant updateParticipant(UUID id, Participant participant) {
        Optional<Participant> oPa = participantRepository.findById(id);
        if (oPa.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified participant.");
        }
        isValidParticipant(participant);
        Participant repoParticipant = oPa.get();
        repoParticipant.setEmail(participant.getEmail());
        repoParticipant.setFirstName(participant.getFirstName());
        repoParticipant.setLastName(participant.getLastName());
        return participantRepository.save(repoParticipant);
    }
    public Participant deleteParticipant(UUID id) {
        if (id == null || !participantRepository.existsById(id))
            throw new IllegalArgumentException("The participant you are looking for does not exist!");
        return participantRepository.deleteParticipantById(id);
    }

    public List<Participant> getParticipants() {
        return participantRepository.findAll();
    }
}