package senac.tsi.Music_Playlist.dtos.artista;

import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;

import java.util.List;

public record ArtistaResponseDTO(
        Long id,
        String nome,
        String genero,
        List<MusicaResponseDTO> musicas
) {
}
