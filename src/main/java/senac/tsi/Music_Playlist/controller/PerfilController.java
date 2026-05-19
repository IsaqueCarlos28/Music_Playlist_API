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
import senac.tsi.Music_Playlist.dtos.perfil.PerfilInputDTO;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;
import senac.tsi.Music_Playlist.service.PerfilService;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<
            PagedModel<EntityModel<PerfilResponseDTO>>
            > getAll(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getPage(pageable)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PerfilResponseDTO>> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.getById(id)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<PerfilResponseDTO>> create(
            @RequestBody @Valid PerfilInputDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PerfilResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid PerfilInputDTO dto
    ) {

        return ResponseEntity.ok(
                service.update(id, dto)
        );
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    // CUSTOM QUERY
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<
            PagedModel<EntityModel<PerfilResponseDTO>>
            > findByUsuario(
            @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByUsuario(
                        nome,
                        pageable
                )
        );
    }
}
