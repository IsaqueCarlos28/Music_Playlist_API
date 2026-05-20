package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;
import senac.tsi.Music_Playlist.domains.Enum.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiKeys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key_value", unique = true, nullable = false, length = 64)
    private String key;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean active;
}