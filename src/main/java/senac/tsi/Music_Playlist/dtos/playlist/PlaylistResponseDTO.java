package senac.tsi.Music_Playlist.dtos.playlist;

import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;

import java.util.List;
import java.util.Set;

public record PlaylistResponseDTO(
        Long id,
        String nome,
        Long usuarioId,
        List<MusicaResponseDTO> musicas
) {
}
