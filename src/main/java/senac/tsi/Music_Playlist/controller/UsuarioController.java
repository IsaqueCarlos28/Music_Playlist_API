package senac.tsi.Music_Playlist.controller;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;
import senac.tsi.Music_Playlist.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>>
    getAll(@ParameterObject Pageable pageable) {

        return ResponseEntity.ok(service.getPage(pageable));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> create(
            @RequestBody @Valid UsuarioInputDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioInputDTO dto
    ) {

        return ResponseEntity.ok(service.update(id, dto));
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
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>>
    findByNome(
            @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.findByNome(nome, pageable)
        );
    }
}
