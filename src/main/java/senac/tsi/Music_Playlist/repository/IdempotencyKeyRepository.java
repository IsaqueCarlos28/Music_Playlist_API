package senac.tsi.Music_Playlist.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import senac.tsi.Music_Playlist.domains.IdempotencyKey;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {

    Optional<IdempotencyKey> findByIdempotencyKeyAndMethodAndPath(
            String key,
            String method,
            String path
    );

    @Modifying
    @Transactional
    void deleteByExpiresAtBefore(LocalDateTime now);
}
