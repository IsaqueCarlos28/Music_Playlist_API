package senac.tsi.Music_Playlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.dtos.ErrorResponse;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaInputDTO;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaResponseDTO;
import senac.tsi.Music_Playlist.service.ArtistaService;

@RestController
@RequestMapping("/artistas")
@Tag(name = "Artistas", description = "Gerenciamento de artistas musicais")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar artistas", description = "Retorna todos os artistas cadastrados de forma paginada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ArtistaResponseDTO>>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(pageable));
    }

    @Operation(summary = "Buscar artista por ID", description = "Retorna os dados de um artista específico pelo seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artista encontrado"),
        @ApiResponse(responseCode = "404", description = "Artista não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ArtistaResponseDTO>> getById(
            @Parameter(description = "ID do artista") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Cadastrar artista", description = "Cria um novo artista. Requer perfil ADMIN. " +
               "Inclua o cabeçalho X-Idempotency-Key para evitar duplicações em caso de reenvio.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Artista criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<ArtistaResponseDTO>> create(
            @RequestBody @Valid ArtistaInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Atualizar artista", description = "Atualiza os dados de um artista existente. Requer perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artista atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Artista não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ArtistaResponseDTO>> update(
            @Parameter(description = "ID do artista") @PathVariable Long id,
            @RequestBody @Valid ArtistaInputDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Remover artista", description = "Remove um artista e todas as suas músicas associadas. Requer perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Artista removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Artista não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do artista") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar artistas por gênero", description = "Consulta personalizada que filtra artistas pelo gênero musical informado (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<ArtistaResponseDTO>>> findByGenero(
            @Parameter(description = "Gênero musical a pesquisar") @RequestParam String genero,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByGenero(genero, pageable));
    }
}
