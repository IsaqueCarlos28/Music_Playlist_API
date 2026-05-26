package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.domains.Playlist;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistInputDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;

import java.util.List;
import java.util.Set;

@Component
public class PlaylistMapper {

    private final MusicaMapper musicaMapper;

    public PlaylistMapper(MusicaMapper musicaMapper){
        this.musicaMapper = musicaMapper;
    }

    public Playlist toEntity(
            PlaylistInputDTO dto,
            Usuario usuario,
            Set<Musica> musicas
    ) {

        return Playlist.builder()
                .nome(dto.nome())
                .usuario(usuario)
                .musicas(musicas)
                .build();
    }

    public PlaylistResponseDTO toResponseDTO(Playlist entity) {

        List<MusicaResponseDTO> musicas = entity.getMusicas()
                .stream()
                .map(musicaMapper::toResponseDTO)
                .toList();

        return new PlaylistResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getUsuario().getId(),
                musicas
        );
    }
}
