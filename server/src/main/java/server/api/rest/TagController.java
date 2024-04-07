package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Tag;
import commons.views.View;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import server.services.TagService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
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
        return ResponseEntity.ok(tagService.add(eventId, tag));
    }

    @PutMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Tag> putTag(@PathVariable UUID tagId, @RequestBody Tag tag) throws IllegalArgumentException {
        return ResponseEntity.ok(tagService.update(tagId, tag));
    }

    @DeleteMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Void> deleteTag(@PathVariable UUID tagId) {
        try {
            tagService.delete(tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
