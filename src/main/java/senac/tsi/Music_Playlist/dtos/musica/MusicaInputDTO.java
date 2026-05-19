package senac.tsi.Music_Playlist.dtos.musica;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MusicaInputDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 2, max = 100, message = "O título deve ter entre 2 e 100 caracteres")
        String titulo,

        @NotNull(message = "A duração é obrigatória")
        @Min(value = 1, message = "A duração deve ser maior que zero")
        Integer duracaoSegundos,

        @NotNull(message = "O artista é obrigatório")
        Long artistaId
) {
}
