package server.api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.AdminPasswordService;

@RestController
@RequestMapping("/api/admin/login")
public class AdminLoginController {
    @GetMapping("/{password}")
    public ResponseEntity<Boolean> get(@PathVariable("password") String password) {
        return ResponseEntity.ok(AdminPasswordService.validatePassword(password));
    }
}
