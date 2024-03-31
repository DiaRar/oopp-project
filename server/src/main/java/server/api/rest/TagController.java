package server.api.rest;

import commons.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<Tag>> getTags(@PathVariable UUID eventId) {
        try {
            return ResponseEntity.ok(tagService.getAllByEvent(eventId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable UUID tagId) {
        try {
            return ResponseEntity.ok(tagService.getById(tagId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping({"", "/"})
    public ResponseEntity<Tag> postTag(@PathVariable UUID eventId, @RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(tagService.add(eventId, tag));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<Tag> putTag(@PathVariable UUID tagId, @RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(tagService.update(tagId, tag));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID tagId) {
        try {
            tagService.delete(tagId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
