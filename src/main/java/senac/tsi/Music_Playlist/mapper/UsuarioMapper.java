package senac.tsi.Music_Playlist.mapper;

import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;

@Component
public class UsuarioMapper {
    private final PerfilMapper perfilMapper;

    UsuarioMapper(PerfilMapper perfilMapper){
        this.perfilMapper = perfilMapper;
    }

    public Usuario toEntity(UsuarioInputDTO dto, Role role,String senhaHash) {
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .role(role)
                .senhaHash(senhaHash)
                .build();
    }

    public UsuarioResponseDTO toResponseDTO(Usuario entity) {

        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getPerfil()//Pode ser null
        );
    }
}
