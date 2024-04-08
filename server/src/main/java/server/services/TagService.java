package server.services;

import commons.Event;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import server.database.TagRepository;
import commons.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private static void isValidTag(Tag tag) {
        if (isNullOrEmpty(tag.getName()) || isNullOrEmpty(tag.getColor().toString()))
            throw new IllegalArgumentException("Not a valid tag!");
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public List<Tag> getAllByEvent(UUID eventUuid) {
        List<Tag> list = tagRepository.findTagsByEventId(eventUuid);
        if (list == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        return list;
    }

    public Tag getById(UUID uuid) {
        Optional<Tag> optionalTag = tagRepository.findTagById(uuid);
        if (optionalTag.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        return optionalTag.get();
    }

    public Tag add(UUID eventId, Tag tag) {
        isValidTag(tag);
        Event event = new Event();
        event.setId(eventId);
        tag.setEvent(event);
        return tagRepository.save(tag);
    }

    public Tag update(UUID uuid, Tag tag) {
        isValidTag(tag);
        Tag repoTag = getById(uuid);
        repoTag.setName(tag.getName());
        repoTag.setColor(tag.getColor());
        tagRepository.flush();
        return repoTag;
    }

    public Integer delete(UUID uuid) {
        if (uuid == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Integer deletedRows = tagRepository.deleteTagById(uuid);
        if (deletedRows != 1) {
           throw new EntityNotFoundException("Could not find the Tag!");
        }
        return deletedRows;
    }

}
