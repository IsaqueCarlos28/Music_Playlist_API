package senac.tsi.Music_Playlist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.service.ApiKeyService;

import java.util.Map;

@RestController
@RequestMapping("/api-keys")
public class ApiKeyController {

    private final ApiKeyService service;

    public ApiKeyController(ApiKeyService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate/{usuarioId}")
    public ResponseEntity<Map<String, String>> generate(
            @PathVariable Long usuarioId,
            @RequestParam String role
    ) {

        String key = service.generateKey(usuarioId, role);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("apiKey", key));
    }
}
