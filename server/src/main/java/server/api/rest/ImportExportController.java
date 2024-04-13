package server.api.rest;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ImportExportService;

import java.util.UUID;

@RestController
@RequestMapping("/api/json")
public class ImportExportController {

    private final ImportExportService importExportService;

    @Autowired
    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestBody String json) {
        importExportService.importData(json);
        return ResponseEntity.ok("Data imported successfully.");
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportData() {
        try {
            String json = importExportService.exportData();
            return ResponseEntity.ok(json);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during export: " + e.getMessage());
        }
    }

    @GetMapping("/export/{eventId}")
    public ResponseEntity<String> exportData(@PathVariable UUID eventId) {
        try {
            String json = importExportService.exportWithId(eventId);
            return ResponseEntity.ok(json);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during export: " + e.getMessage());
        }
    }
}