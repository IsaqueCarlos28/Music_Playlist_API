package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilInputDTO;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;

@Component
public class PerfilMapper {

    public Perfil toEntity(PerfilInputDTO dto) {
        return Perfil.builder()
                .fotoUrl(dto.fotoUrl())
                .biografia(dto.biografia())
                .build();
    }

    public PerfilResponseDTO toResponseDTO(Perfil entity) {
        return new PerfilResponseDTO(
                entity.getId(),
                entity.getFotoUrl(),
                entity.getBiografia()
        );
    }
}