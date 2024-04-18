package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Tag;
import commons.views.View;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import server.services.TagService;
import server.services.WebSocketUpdateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events/{eventId}/tags")
public class TagController {

    private final TagService tagService;
    private final WebSocketUpdateService updateService;

    private Map<Object, Consumer<Tag>> taglistners = new HashMap<>();

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
        updateService.sendAddedTag(eventId, savedTag);
        return ResponseEntity.ok(savedTag);
    }

    @PutMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    public ResponseEntity<Tag> putTag(@PathVariable UUID eventId, @PathVariable UUID tagId,
                                      @RequestBody Tag tag) throws IllegalArgumentException {
        Tag updatedTag = tagService.update(tagId, tag);

        //taglistners.forEach((k, v) -> v.accept(updatedTag));

        updateService.sendUpdatedTag(eventId, updatedTag);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{tagId}")
    @JsonView(View.CommonsView.class)
    @Transactional
    public ResponseEntity<Void> deleteTag(@PathVariable UUID eventId, @PathVariable UUID tagId) {
            tagService.delete(tagId);
            updateService.sendRemovedTag(eventId, tagId);
            return ResponseEntity.ok().build();
    }
    private final long timeout = 5000L;
    @GetMapping("/updates")
    @JsonView(View.StatisticsView.class)
    public DeferredResult<ResponseEntity<Tag>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Tag>>(timeout, noContent);
        var key = new Object();
        taglistners.put(key, t -> {
            result.setResult(ResponseEntity.ok(t));
        });
        result.onCompletion(() -> {
            taglistners.remove(key);
        });
        return result;
    }
}
