package senac.tsi.Music_Playlist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.domains.IdempotencyKey;
import senac.tsi.Music_Playlist.repository.IdempotencyKeyRepository;

import java.time.LocalDateTime;

@Service
public class IdempotencyService {

    private final IdempotencyKeyRepository repository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(
            IdempotencyKeyRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public IdempotencyKey get(String key, String method, String path) {
        return repository.findByIdempotencyKeyAndMethodAndPath(key, method, path)
                .orElse(null);
    }

    public IdempotencyKey save(
            String key,
            String method,
            String path,
            Object response,
            int statusCode
    ) {
        try {
            IdempotencyKey entity = IdempotencyKey.builder()
                    .idempotencyKey(key)
                    .method(method)
                    .path(path)
                    .responseBody(objectMapper.writeValueAsString(response))
                    .statusCode(statusCode)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(1))
                    .build();

            return repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving idempotency key");
        }
    }
}
