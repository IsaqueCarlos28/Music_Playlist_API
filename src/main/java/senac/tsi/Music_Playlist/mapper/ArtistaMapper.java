package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Artista;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaInputDTO;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaResponseDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ArtistaMapper {

    public Artista toEntity(ArtistaInputDTO dto) {
        Set<Musica> musicas = new HashSet<>();
        return Artista.builder()
                .nome(dto.nome())
                .genero(dto.genero())
                .musicas(musicas)
                .build();
    }

    public ArtistaResponseDTO toResponseDTO(Artista entity) {

        List<String> musicas = entity.getMusicas()
                .stream()
                .map(Musica::getTitulo)
                .toList();

        return new ArtistaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getGenero(),
                musicas
        );
    }
}
