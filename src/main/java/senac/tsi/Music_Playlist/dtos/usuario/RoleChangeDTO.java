package senac.tsi.Music_Playlist.dtos.usuario;

import jakarta.validation.constraints.NotNull;
import senac.tsi.Music_Playlist.domains.Enum.Role;

public record RoleChangeDTO(
        @NotNull
        Role role
) {
}
