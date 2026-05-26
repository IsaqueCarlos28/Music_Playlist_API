package senac.tsi.Music_Playlist.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.repository.IdempotencyKeyRepository;

import java.time.LocalDateTime;

@Component
public class IdempotencyCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyCleanupScheduler.class);

    private final IdempotencyKeyRepository repository;

    public IdempotencyCleanupScheduler(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000) // hourly — keys expire in 24h
    public void cleanupExpiredKeys() {
        repository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("Chaves de idempotência expiradas removidas");
    }
}
