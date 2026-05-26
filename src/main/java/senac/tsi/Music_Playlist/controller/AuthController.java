package senac.tsi.Music_Playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import senac.tsi.Music_Playlist.dtos.ErrorResponse;
import senac.tsi.Music_Playlist.dtos.login.LoginRequestDTO;
import senac.tsi.Music_Playlist.dtos.login.LoginResponseDTO;
import senac.tsi.Music_Playlist.service.ApiKeyService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação e geração de chave de API")
public class AuthController {

    private final ApiKeyService apiKeyService;

    public AuthController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Operation(
        summary = "Login e geração de chave de API",
        description = "Autentica o usuário com e-mail e senha. Retorna uma chave de API ativa (X-API-KEY) " +
                      "para ser usada nos demais endpoints protegidos. Se o usuário já possuir uma chave " +
                      "ativa, ela é reutilizada."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Login realizado com sucesso — chave de API retornada",
            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais incorretas",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                apiKeyService.loginAndGenerateKey(request.email(), request.password())
        );
    }
}
