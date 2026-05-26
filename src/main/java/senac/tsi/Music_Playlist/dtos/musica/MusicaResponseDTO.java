package senac.tsi.Music_Playlist.dtos.musica;

public record MusicaResponseDTO(
        Long id,
        String titulo,
        Integer duracaoSegundos,
        String artista,
        String link
) {
}
