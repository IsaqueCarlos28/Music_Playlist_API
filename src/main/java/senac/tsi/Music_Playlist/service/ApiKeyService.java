package senac.tsi.Music_Playlist.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.domains.ApiKey;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.login.LoginResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.repository.ApiKeyRepository;
import senac.tsi.Music_Playlist.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {


    private final ApiKeyRepository apiKeyRepository;
    private final UsuarioRepository usuarioRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository,
                         UsuarioRepository usuarioRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponseDTO loginAndGenerateKey(String email, String password) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // ⚠️ replace with BCrypt later if you already use it
        if (!usuario.getSenhaHash().equals(password)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // OPTIONAL: reuse active key instead of creating new every login
        Optional<ApiKey> existingKey = apiKeyRepository
                .findByUsuarioAndActiveTrue(usuario);

        ApiKey apiKey;

        if (existingKey.isPresent()) {
            apiKey = existingKey.get();
        } else {
            apiKey = ApiKey.builder()
                    .key(UUID.randomUUID().toString().replace("-", ""))
                    .usuario(usuario)
                    .role(usuario.getRole()) // ROLE_USER / ROLE_ADMIN
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .active(true)
                    .build();

            apiKeyRepository.save(apiKey);
        }

        return new LoginResponseDTO(
                apiKey.getKey(),
                apiKey.getRole(),
                usuario.getId()
        );
    }

    public String generateKey(Long usuarioId, String role) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", usuarioId));

        // OPTIONAL:
        // deactivate previous active keys
        List<ApiKey> activeKeys =
                apiKeyRepository.findAllByUsuarioAndActiveTrue(usuario);

        activeKeys.forEach(key -> key.setActive(false));

        apiKeyRepository.saveAll(activeKeys);

        String generatedKey =
                UUID.randomUUID().toString().replace("-", "");

        ApiKey apiKey = ApiKey.builder()
                .key(generatedKey)
                .usuario(usuario)
                .role(role)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .active(true)
                .build();

        apiKeyRepository.save(apiKey);

        return generatedKey;
    }
}
