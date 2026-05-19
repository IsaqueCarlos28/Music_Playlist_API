package senac.tsi.Music_Playlist.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.controller.PerfilController;
import senac.tsi.Music_Playlist.controller.UsuarioController;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PerfilAssembler implements RepresentationModelAssembler<PerfilResponseDTO, EntityModel<PerfilResponseDTO>> {

    @Override
    public EntityModel<PerfilResponseDTO> toModel(PerfilResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(PerfilController.class)
                        .getById(dto.id()))
                        .withSelfRel(),

                linkTo(methodOn(PerfilController.class)
                        .getAll(null))
                        .withRel("perfis"),

                linkTo(methodOn(UsuarioController.class)
                        .getById(dto.id()))
                        .withRel("usuario")
        );
    }
}
