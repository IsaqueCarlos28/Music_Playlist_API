package senac.tsi.Music_Playlist.infrastructure;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.repository.IdempotencyKeyRepository;

import java.time.LocalDateTime;

@Component
public class IdempotencyCleanupScheduler {

    private final IdempotencyKeyRepository repository;

    public IdempotencyCleanupScheduler(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }


    @Scheduled(fixedRate = 60 * 1000)
    public void cleanupExpiredKeys() {

        repository.deleteByExpiresAtBefore(LocalDateTime.now());

        System.out.println("Expired idempotency keys cleaned up");
    }
}
