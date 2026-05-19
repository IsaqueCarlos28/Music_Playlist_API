package senac.tsi.Music_Playlist.dtos.playlist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PlaylistInputDTO(
        @NotBlank(message = "O nome da playlist é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @NotEmpty(message = "A playlist deve possuir pelo menos uma música")
        List<Long> musicasIds) {
}
