package senac.tsi.Music_Playlist.dtos.perfil;

public record PerfilResponseDTO(
        Long usuarioId,
        Long id,
        String fotoUrl,
        String biografia
) {
}
