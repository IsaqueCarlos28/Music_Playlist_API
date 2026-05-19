package senac.tsi.Music_Playlist.dtos.artista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistaInputDTO(
        @NotBlank(message = "O nome do artista é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "O gênero é obrigatório")
        @Size(min = 3, max = 50, message = "O gênero deve ter entre 3 e 50 caracteres")
        String genero) {
}
