package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String genero;

    // One-to-Many
    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Musica> musicas = new ArrayList<>();
}
