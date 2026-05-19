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
import senac.tsi.Music_Playlist.dtos.artista.ArtistaInputDTO;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaResponseDTO;
import senac.tsi.Music_Playlist.service.ArtistaService;

@RestController
@RequestMapping("/artistas")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(
            ArtistaService service
    ) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<
            PagedModel<EntityModel<ArtistaResponseDTO>>
            > getAll(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getPage(pageable)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ArtistaResponseDTO>>
    getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<ArtistaResponseDTO>>
    create(
            @RequestBody
            @Valid
            ArtistaInputDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ArtistaResponseDTO>>
    update(
            @PathVariable Long id,
            @RequestBody
            @Valid
            ArtistaInputDTO dto
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

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<
            PagedModel<EntityModel<ArtistaResponseDTO>>
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
