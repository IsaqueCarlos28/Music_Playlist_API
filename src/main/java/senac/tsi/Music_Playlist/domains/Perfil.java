package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fotoUrl;

    private String biografia;

    // One-to-One
    @OneToOne(mappedBy = "perfil",fetch = FetchType.EAGER)
    private Usuario usuario;
}
