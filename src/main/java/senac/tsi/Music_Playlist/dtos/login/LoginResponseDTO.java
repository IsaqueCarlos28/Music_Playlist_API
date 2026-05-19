package senac.tsi.Music_Playlist.dtos.login;

import senac.tsi.Music_Playlist.domains.Enum.Role;

public record LoginResponseDTO(
        String apiKey,
        Role role,
        Long usuarioId
) {
}
