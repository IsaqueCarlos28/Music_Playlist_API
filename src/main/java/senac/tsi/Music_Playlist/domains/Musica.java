package senac.tsi.Music_Playlist.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotNull(message = "A duração é obrigatória")
    @Min(value = 1, message = "A duração deve ser maior que zero")
    @Column(nullable = false)
    private Integer duracaoSegundos;

    // Many Musicas -> One Artista
    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    // Many-to-Many
    @ManyToMany(mappedBy = "musicas", fetch = FetchType.EAGER)
    private List<Playlist> playlists = new ArrayList<>();
}
