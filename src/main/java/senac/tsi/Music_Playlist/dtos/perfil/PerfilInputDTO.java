package senac.tsi.Music_Playlist.dtos.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PerfilInputDTO(

        @NotBlank(message = "A foto URL é obrigatória")
        String fotoUrl,

        @NotBlank(message = "A biografia é obrigatória")
        @Size(max = 255, message = "A biografia deve ter no máximo 255 caracteres")
        String biografia

) {
}