package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Tag;
import commons.views.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.TagService;
import server.services.WebSocketUpdateService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/tags")
public class TagController {

    private final TagService tagService;
    private final WebSocketUpdateService updateService;

    public TagController(TagService tagService, WebSocketUpdateService updateService) {
        this.tagService = tagService;
        this.updateService = updateService;
    }
    @GetMapping({"", "/"})
    @JsonView(View.CommonsView.class)
    public ResponseEntity<List<Tag>> getTags(@PathVariable UUID eventId) throws ResponseStatusException {
        return ResponseEntity.ok(tagService.getAllByEvent(eventId));
    }

    @GetMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Tag> getTagById(@PathVariable UUID tagId) throws ResponseStatusException {
        return ResponseEntity.ok(tagService.getById(tagId));
    }
    @PostMapping({"", "/"})
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Tag> postTag(@PathVariable UUID eventId, @RequestBody Tag tag)
            throws IllegalArgumentException {
        Tag savedTag = tagService.add(eventId, tag);
        System.out.println("added smth");
        updateService.sendAddedTag(eventId, savedTag);
        return ResponseEntity.ok(savedTag);
    }

    @PutMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Tag> putTag(@PathVariable UUID eventId, @PathVariable UUID tagId, @RequestBody Tag tag) throws IllegalArgumentException {
        Tag updatedTag = tagService.update(tagId, tag);
        updateService.sendUpdatedTag(eventId, updatedTag);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Void> deleteTag(@PathVariable UUID eventId, @PathVariable UUID tagId) {
            tagService.delete(tagId);
            updateService.sendRemovedTag(eventId, tagId);
            return ResponseEntity.ok().build();
    }
}
