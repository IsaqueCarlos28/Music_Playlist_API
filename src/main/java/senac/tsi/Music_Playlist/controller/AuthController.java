package senac.tsi.Music_Playlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import senac.tsi.Music_Playlist.dtos.login.LoginRequestDTO;
import senac.tsi.Music_Playlist.dtos.login.LoginResponseDTO;
import senac.tsi.Music_Playlist.service.ApiKeyService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ApiKeyService apiKeyService;

    public AuthController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {

        return ResponseEntity.ok(
                apiKeyService.loginAndGenerateKey(
                        request.email(),
                        request.password()
                )
        );
    }
}
