package senac.tsi.Music_Playlist.domains;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import senac.tsi.Music_Playlist.domains.Enum.Role;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 64)
    private String key;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Role role; // ROLE_USER / ROLE_ADMIN

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean active;
}
