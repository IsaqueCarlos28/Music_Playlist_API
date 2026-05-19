package senac.tsi.Music_Playlist.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import senac.tsi.Music_Playlist.controller.PlaylistController;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class PlaylistAssembler implements RepresentationModelAssembler<PlaylistResponseDTO, EntityModel<PlaylistResponseDTO>> {

    @Override
    public EntityModel<PlaylistResponseDTO> toModel(PlaylistResponseDTO dto) {

        return EntityModel.of(
                dto,

                linkTo(methodOn(PlaylistController.class)
                        .getById(dto.id()))
                        .withSelfRel(),

                linkTo(methodOn(PlaylistController.class)
                        .getAll(null))
                        .withRel("playlists"),

                linkTo(methodOn(PlaylistController.class)
                        .addMusica(dto.id(), null))
                        .withRel("add-musica"),

                linkTo(methodOn(PlaylistController.class)
                        .removeMusica(dto.id(), null))
                        .withRel("remove-musica")
        );
    }
}
