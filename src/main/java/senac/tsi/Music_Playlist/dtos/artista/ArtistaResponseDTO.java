package senac.tsi.Music_Playlist.dtos.artista;

import java.util.List;

public record ArtistaResponseDTO(
        Long id,
        String nome,
        String genero,
        List<String> musicas
) {
}
