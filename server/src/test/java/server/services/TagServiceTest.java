package server.services;

import commons.Event;
import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import server.database.TagRepository;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private TagService tagService;
    private Tag tag;
    private Event event;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.event = new Event("test");
        this.event.setId(UUID.fromString("63953720-1c27-4664-8078-1cbbcfba583e"));
        this.tag = new Tag("testTag", Color.BLACK);
        this.tag.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        this.tag.setEvent(event);
    }

    private Tag tagSetIdHelper(Tag tag) {
        tag.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        return tag;
    }
    @Test
    public void addTest() {
        Tag tag1 = new Tag("testTag", Color.BLACK);
        Event event1 = new Event();
        event1.setId(UUID.fromString("63953720-1c27-4664-8078-1cbbcfba583e"));
        tag.setEvent(event1);
        when(tagRepository.save(tag1)).thenReturn(tagSetIdHelper(tag1));
        assertEquals(tagService.add(event.getId(), tag1), tag);
    }
    @Test
    public void getByIdSuccessTest() {
        when(tagRepository.findTagById(tag.getId())).thenReturn(Optional.of(tag));
        assertEquals(tag, tagService.getById(tag.getId()));
    }

    @Test
    public void getByIdFailTest() {
        when(tagRepository.findTagById(tag.getId())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
           tagService.getById(tag.getId());
        });
    }

    @Test
    public void getAllByEventSuccessTest() {
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        when(tagRepository.findTagsByEventId(event.getId())).thenReturn(list);
        assertEquals(list, tagService.getAllByEvent(event.getId()));
    }
    @Test
    public void getAllByEventFailTest() {
        when(tagRepository.findTagsByEventId(event.getId())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> {
           tagService.getAllByEvent(event.getId());
        });
    }

    @Test
    public void updateTest() {
        when(tagRepository.findTagById(tag.getId())).thenReturn(Optional.of(tag));
        Tag tag1 = new Tag("test", Color.BLUE);
        assertEquals("test", tagService.update(tag.getId(), tag1).getName());
        assertEquals(Color.BLUE, tagService.update(tag.getId(),tag1).getColor());
    }

    @Test
    public void deleteSuccessTest() {
        when(tagRepository.deleteTagById(tag.getId())).thenReturn(1);
        assertEquals(1, tagService.delete(tag.getId()));
    }

    @Test
    public void deleteFailTest() {
        when(tagRepository.deleteTagById(tag.getId())).thenReturn(2);
        assertThrows(EntityNotFoundException.class, () -> {
           tagService.delete(tag.getId());
        });
    }

    @Test
    public void deleteFailNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            tagService.delete(null);
        });
    }

    @Test
    public void addTagFailTest() {
        Tag tag1 = new Tag(null, null);
        assertThrows(IllegalArgumentException.class, () -> {
            tagService.add(event.getId(), tag1);
        });
    }
}
