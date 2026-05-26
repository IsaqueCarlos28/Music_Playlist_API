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
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTO;
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTOv2;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;
import senac.tsi.Music_Playlist.service.MusicaService;

@RestController
@RequestMapping("/musicas")
@Tag(name = "Músicas", description = "Gerenciamento de músicas. O endpoint de cadastro é versionado via cabeçalho X-API-Version (1 ou 2).")
public class MusicaController {

    private final MusicaService service;

    public MusicaController(MusicaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar músicas", description = "Retorna todas as músicas cadastradas de forma paginada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MusicaResponseDTO>>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(pageable));
    }

    @Operation(summary = "Buscar música por ID", description = "Retorna os dados de uma música específica pelo seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Música encontrada"),
        @ApiResponse(responseCode = "404", description = "Música não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MusicaResponseDTO>> getById(
            @Parameter(description = "ID da música") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
        summary = "Cadastrar música — versão 1 (sem link)",
        description = "Cria uma nova música sem campo de link. Requer perfil ADMIN e cabeçalho " +
                      "**X-API-Version: 1**. Inclua X-Idempotency-Key para evitar duplicações."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Música criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Artista informado não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(headers = "X-API-Version=1")
    public ResponseEntity<EntityModel<MusicaResponseDTO>> create(
            @RequestBody @Valid MusicaInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(
        summary = "Cadastrar música — versão 2 (com link)",
        description = "Cria uma nova música incluindo o campo de link obrigatório. Requer perfil ADMIN e cabeçalho " +
                      "**X-API-Version: 2**. Inclua X-Idempotency-Key para evitar duplicações."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Música criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Artista informado não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(headers = "X-API-Version=2")
    public ResponseEntity<EntityModel<MusicaResponseDTO>> createv2(
            @RequestBody @Valid MusicaInputDTOv2 dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createV2(dto));
    }

    @Operation(
        summary = "Cadastrar música — versão padrão (fallback para v1)",
        description = "Fallback quando X-API-Version não é informado — comporta-se como a versão 1 (sem link). " +
                      "Recomenda-se sempre informar X-API-Version explicitamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Música criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<MusicaResponseDTO>> createDefault(
            @RequestBody @Valid MusicaInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Atualizar música", description = "Atualiza todos os dados de uma música existente (usa o formato v2 com link). Requer perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Música atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Música ou artista não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<MusicaResponseDTO>> update(
            @Parameter(description = "ID da música") @PathVariable Long id,
            @RequestBody @Valid MusicaInputDTOv2 dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Remover música", description = "Remove uma música do sistema. Requer perfil ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Música removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Música não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Acesso negado — requer perfil ADMIN",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da música") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar músicas por título", description = "Consulta personalizada que filtra músicas pelo título (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/titulo")
    public ResponseEntity<PagedModel<EntityModel<MusicaResponseDTO>>> findByTitulo(
            @Parameter(description = "Trecho do título a pesquisar") @RequestParam String titulo,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByTitulo(titulo, pageable));
    }

    @Operation(summary = "Buscar músicas por artista", description = "Consulta personalizada que filtra músicas pelo nome do artista (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/artista")
    public ResponseEntity<PagedModel<EntityModel<MusicaResponseDTO>>> findByArtista(
            @Parameter(description = "Nome do artista a pesquisar") @RequestParam String artista,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByArtista(artista, pageable));
    }

    @Operation(summary = "Buscar músicas por gênero", description = "Consulta personalizada que filtra músicas pelo gênero do artista (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/genero")
    public ResponseEntity<PagedModel<EntityModel<MusicaResponseDTO>>> findByGenero(
            @Parameter(description = "Gênero musical a pesquisar") @RequestParam String genero,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByGenero(genero, pageable));
    }
}
