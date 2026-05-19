package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;
import senac.tsi.Music_Playlist.domains.Enum.Role;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senhaHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    // One-to-One
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    // One-to-Many
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Playlist> playlists = new ArrayList<>();
}