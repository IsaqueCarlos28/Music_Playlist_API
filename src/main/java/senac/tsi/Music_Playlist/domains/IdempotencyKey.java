package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "idempotency_keys",
        indexes = {
                @Index(name = "idx_expires_at", columnList = "expiresAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String path;

    @Lob
    @Column(nullable = false)
    private String responseBody;

    @Column(nullable = false)
    private Integer statusCode;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}