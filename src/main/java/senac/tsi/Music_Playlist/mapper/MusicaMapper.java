package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Artista;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTO;
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTOv2;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;

@Component
public class MusicaMapper {

    public Musica toEntity(MusicaInputDTO dto, Artista artista) {
        return Musica.builder()
                .titulo(dto.titulo())
                .duracaoSegundos(dto.duracaoSegundos())
                .artista(artista)
                .link(null)
                .build();
    }

    public Musica toEntityV2(MusicaInputDTOv2 dto, Artista artista) {
        return Musica.builder()
                .titulo(dto.titulo())
                .duracaoSegundos(dto.duracaoSegundos())
                .artista(artista)
                .link(dto.link())
                .build();
    }

    public MusicaResponseDTO toResponseDTO(Musica entity) {
        return new MusicaResponseDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDuracaoSegundos(),
                entity.getArtista().getNome(),
                entity.getLink()
        );
    }
}
