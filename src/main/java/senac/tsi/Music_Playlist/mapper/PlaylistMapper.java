package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.domains.Playlist;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistInputDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;

import java.util.List;

@Component
public class PlaylistMapper {

    public Playlist toEntity(
            PlaylistInputDTO dto,
            Usuario usuario,
            List<Musica> musicas
    ) {

        return Playlist.builder()
                .nome(dto.nome())
                .usuario(usuario)
                .musicas(musicas)
                .build();
    }

    public PlaylistResponseDTO toResponseDTO(Playlist entity) {

        List<String> musicas = entity.getMusicas()
                .stream()
                .map(Musica::getTitulo)
                .toList();

        return new PlaylistResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getUsuario().getId(),
                musicas
        );
    }
}
