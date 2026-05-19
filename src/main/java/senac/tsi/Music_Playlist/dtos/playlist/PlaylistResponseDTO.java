package senac.tsi.Music_Playlist.dtos.playlist;

import java.util.List;

public record PlaylistResponseDTO(
        Long id,
        String nome,
        Long usuarioId,
        List<String> musicas
) {
}
