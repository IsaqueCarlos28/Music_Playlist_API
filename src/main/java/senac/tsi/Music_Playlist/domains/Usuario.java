package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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


    @Column(nullable = false, length = 100)
    private String nome;

    @Email(message = "Email inválido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Column(nullable = false)
    private String senhaHash;

    @NotNull(message = "O role é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // One-to-One
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    // One-to-Many
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    public void setPerfil(Perfil perfil){
        this.perfil = perfil;

        if (perfil != null) {
            perfil.setUsuario(this);
        }
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.setUsuario(this);
    }
}
