package senac.tsi.Music_Playlist.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.controller.MusicaController;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MusicaAssembler implements RepresentationModelAssembler<MusicaResponseDTO, EntityModel<MusicaResponseDTO>> {

    @Override
    public EntityModel<MusicaResponseDTO> toModel(MusicaResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(MusicaController.class)
                        .getById(dto.id()))
                        .withSelfRel(),

                linkTo(methodOn(MusicaController.class)
                        .getAll(null))
                        .withRel("musicas"),

                linkTo(methodOn(MusicaController.class)
                        .findByArtista(dto.artista(), null))
                        .withRel("artista"),

                linkTo(methodOn(MusicaController.class)
                        .findByGenero(dto.artista(), null))
                        .withRel("genero")
        );
    }
}