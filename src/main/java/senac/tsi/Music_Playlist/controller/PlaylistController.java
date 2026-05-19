package senac.tsi.Music_Playlist.controller;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistInputDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;
import senac.tsi.Music_Playlist.service.PlaylistService;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(
            PlaylistService service
    ) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<
            PagedModel<EntityModel<PlaylistResponseDTO>>
            > getAll(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getPage(pageable)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>>
    getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<PlaylistResponseDTO>>
    create(
            @RequestBody
            @Valid
            PlaylistInputDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>>
    update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            PlaylistInputDTO dto
    ) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH BY NAME
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<
            PagedModel<EntityModel<PlaylistResponseDTO>>
            > findByNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByNome(
                        nome,
                        pageable
                )
        );
    }

    // ADD MUSIC TO PLAYLIST
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>>
    addMusica(
            @PathVariable Long playlistId,
            @PathVariable Long musicaId
    ) {

        return ResponseEntity.ok(
                service.addMusica(
                        playlistId,
                        musicaId
                )
        );
    }

    // REMOVE MUSIC FROM PLAYLIST
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<?>
    removeMusica(
            @PathVariable Long playlistId,
            @PathVariable Long musicaId
    ) {

        service.removeMusica(playlistId,musicaId);
        return ResponseEntity.noContent().build();
    }
}