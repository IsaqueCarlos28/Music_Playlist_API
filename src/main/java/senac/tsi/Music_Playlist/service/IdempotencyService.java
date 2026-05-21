package senac.tsi.Music_Playlist.service;

import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.domains.IdempotencyKey;
import senac.tsi.Music_Playlist.repository.IdempotencyKeyRepository;

import java.time.LocalDateTime;

@Service
public class IdempotencyService {

    private final IdempotencyKeyRepository repository;

    public IdempotencyService(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    public IdempotencyKey get(String key, String method, String path) {
        return repository.findByIdempotencyKeyAndMethodAndPath(key, method, path)
                .orElse(null);
    }

    public IdempotencyKey save(
            String key,
            String method,
            String path,
            String responseBody,
            int statusCode
    ) {
        IdempotencyKey entity = IdempotencyKey.builder()
                .idempotencyKey(key)
                .method(method)
                .path(path)
                .responseBody(responseBody)
                .statusCode(statusCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(1))
                .build();

        return repository.save(entity);
    }
}
