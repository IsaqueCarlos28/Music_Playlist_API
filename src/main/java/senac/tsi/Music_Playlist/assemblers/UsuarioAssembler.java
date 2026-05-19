package senac.tsi.Music_Playlist.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.controller.UsuarioController;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<UsuarioResponseDTO, EntityModel<UsuarioResponseDTO>> {

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(UsuarioController.class)
                        .getById(dto.id()))
                        .withSelfRel(),

                linkTo(methodOn(UsuarioController.class)
                        .getAll(null))
                        .withRel("usuarios"),

                linkTo(methodOn(UsuarioController.class)
                        .findByNome(dto.nome(), null))
                        .withRel("search")
        );
    }
}
