package senac.tsi.Music_Playlist.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.controller.ArtistaController;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ArtistaAssembler implements RepresentationModelAssembler<ArtistaResponseDTO, EntityModel<ArtistaResponseDTO>> {

    @Override
    public EntityModel<ArtistaResponseDTO> toModel(ArtistaResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(ArtistaController.class)
                        .getById(dto.id()))
                        .withSelfRel(),

                linkTo(methodOn(ArtistaController.class)
                        .getAll(null))
                        .withRel("artistas"),

                linkTo(methodOn(ArtistaController.class)
                        .findByGenero(dto.genero(), null))
                        .withRel("genero")
        );
    }
}
