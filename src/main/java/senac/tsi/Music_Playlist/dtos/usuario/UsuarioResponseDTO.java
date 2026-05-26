package senac.tsi.Music_Playlist.dtos.usuario;

import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Role role,
        Object perfil
) {
}
