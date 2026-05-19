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
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTO;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;
import senac.tsi.Music_Playlist.service.MusicaService;

@RestController
@RequestMapping("/musicas")
public class MusicaController {

    private final MusicaService service;

    public MusicaController(
            MusicaService service
    ) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<
            PagedModel<EntityModel<MusicaResponseDTO>>
            > getAll(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getPage(pageable)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MusicaResponseDTO>>
    getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<MusicaResponseDTO>>
    create(
            @RequestBody
            @Valid
            MusicaInputDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MusicaResponseDTO>>
    update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            MusicaInputDTO dto
    ) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH BY TITLE
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/titulo")
    public ResponseEntity<
            PagedModel<EntityModel<MusicaResponseDTO>>
            > findByTitulo(
            @RequestParam String titulo,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByTitulo(
                        titulo,
                        pageable
                )
        );
    }

    // SEARCH BY ARTIST
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/artista")
    public ResponseEntity<
            PagedModel<EntityModel<MusicaResponseDTO>>
            > findByArtista(
            @RequestParam String artista,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByArtista(
                        artista,
                        pageable
                )
        );
    }

    // SEARCH BY GENRE
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/genero")
    public ResponseEntity<
            PagedModel<EntityModel<MusicaResponseDTO>>
            > findByGenero(
            @RequestParam String genero,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByGenero(
                        genero,
                        pageable
                )
        );
    }
}
