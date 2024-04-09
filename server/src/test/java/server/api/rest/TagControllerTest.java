package server.api.rest;

import commons.Event;
import commons.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.services.TagService;
import server.services.WebSocketUpdateService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TagControllerTest {
    @Mock
    private TagService tagService;
    @Mock
    private WebSocketUpdateService webSocketUpdateService;
    @InjectMocks
    private TagController tagController;

    private Tag tag;
    private Event event;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        tag = new Tag("test", "#000000");
        tag.setEvent(event);
        tag.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9929"));
    }

    @Test
    public void getTagsTest() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tag);
        when(tagService.getAllByEvent(event.getId())).thenReturn(tagList);
        assertEquals(tagList, tagController.getTags(event.getId()).getBody());
    }

    @Test
    public void getTagsFail() {
        when(tagService.getAllByEvent(event.getId())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class,() -> tagController.getTags(event.getId()));
    }
    @Test
    public void getTagByIdTest() {
        when(tagService.getById(tag.getId())).thenReturn(tag);
        assertEquals(tag, tagController.getTagById(tag.getId()).getBody());
    }

    @Test
    public void getTagByIdFailTest() {
        when(tagService.getById(tag.getId())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> tagController.getTagById(tag.getId()));
    }

    @Test
    public void postTagTest() {
        when(tagService.add(event.getId(), tag)).thenReturn(tag);
        assertEquals(tag, tagController.postTag(event.getId(), tag).getBody());
    }

    @Test
    public void postTagFailTest() {
        when(tagService.add(event.getId(), tag)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> tagController.postTag(event.getId(), tag));
    }

    @Test
    public void putTagTest() {
        when(tagService.update(tag.getId(), tag)).thenReturn(tag);
        assertEquals(tag, tagController.putTag(event.getId(), tag.getId(), tag).getBody());
    }

    @Test
    public void putTagFailTest() {
        when(tagService.update(tag.getId(), tag)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> tagController.putTag(event.getId(), tag.getId(), tag));
    }

    @Test
    public void deleteTagTest() {
        assertEquals(ResponseEntity.ok().build(), tagController.deleteTag(event.getId(), tag.getId()));
    }

    @Test
    public void deleteTagFail() {
        when(tagService.delete(tag.getId())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> tagController.deleteTag(event.getId(), tag.getId()));
    }
}
