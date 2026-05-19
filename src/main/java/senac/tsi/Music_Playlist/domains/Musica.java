package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "musicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Musica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private Integer duracaoSegundos;

    // Many Musicas -> One Artista
    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    // Many-to-Many
    @ManyToMany(mappedBy = "musicas",fetch = FetchType.EAGER)
    private List<Playlist> playlists = new ArrayList<>();
}
