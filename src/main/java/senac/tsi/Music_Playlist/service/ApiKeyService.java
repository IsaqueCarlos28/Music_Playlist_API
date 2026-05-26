package senac.tsi.Music_Playlist.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.domains.ApiKeys;
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
    private final PasswordEncoder passwordEncoder;

    public ApiKeyService(
            ApiKeyRepository apiKeyRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.apiKeyRepository = apiKeyRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO loginAndGenerateKey(String email, String password) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(password, usuario.getSenhaHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Reuse active key instead of creating a new one every login
        Optional<ApiKeys> existingKey = apiKeyRepository
                .findByUsuarioAndActiveTrueAndRole(usuario,usuario.getRole());

        ApiKeys apiKeys;

        if (existingKey.isPresent()) {
            apiKeys = existingKey.get();
        } else {
            apiKeys = ApiKeys.builder()
                    .key(UUID.randomUUID().toString().replace("-", ""))
                    .usuario(usuario)
                    .role(usuario.getRole())
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(30))
                    .active(true)
                    .build();

            apiKeyRepository.save(apiKeys);
        }

        return new LoginResponseDTO(
                apiKeys.getKey(),
                apiKeys.getRole(),
                usuario.getId()
        );
    }
}
