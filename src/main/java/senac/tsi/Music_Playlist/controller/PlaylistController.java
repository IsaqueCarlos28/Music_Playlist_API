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
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistInputDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;
import senac.tsi.Music_Playlist.service.PlaylistService;

@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Gerenciamento de playlists e suas músicas (relacionamento N:N entre Playlist e Música)")
public class PlaylistController {

    private final PlaylistService service;

    public PlaylistController(PlaylistService service) {
        this.service = service;
    }

    @Operation(summary = "Listar playlists", description = "Retorna todas as playlists cadastradas de forma paginada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PlaylistResponseDTO>>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(pageable));
    }

    @Operation(summary = "Buscar playlist por ID", description = "Retorna os dados de uma playlist específica, incluindo a lista de músicas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Playlist encontrada"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>> getById(
            @Parameter(description = "ID da playlist") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
        summary = "Criar playlist",
        description = "Cria uma nova playlist associada a um usuário. É possível já incluir músicas pelo campo `musicasIds`. " +
                      "Inclua X-Idempotency-Key para evitar duplicações."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Playlist criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou música não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<EntityModel<PlaylistResponseDTO>> create(
            @RequestBody @Valid PlaylistInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Atualizar playlist", description = "Atualiza o nome, usuário dono e lista completa de músicas de uma playlist.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Playlist atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Playlist, usuário ou música não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>> update(
            @Parameter(description = "ID da playlist") @PathVariable Long id,
            @RequestBody @Valid PlaylistInputDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Remover playlist", description = "Remove uma playlist do sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Playlist removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Playlist não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da playlist") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar playlists por nome", description = "Consulta personalizada que filtra playlists pelo nome (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<PlaylistResponseDTO>>> findByNome(
            @Parameter(description = "Trecho do nome a pesquisar") @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByNome(nome, pageable));
    }

    @Operation(
        summary = "Adicionar música à playlist",
        description = "Adiciona uma música existente a uma playlist. Se a música já estiver na playlist, a operação é ignorada (idempotente por natureza)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Música adicionada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Playlist ou música não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<EntityModel<PlaylistResponseDTO>> addMusica(
            @Parameter(description = "ID da playlist") @PathVariable Long playlistId,
            @Parameter(description = "ID da música a adicionar") @PathVariable Long musicaId
    ) {
        return ResponseEntity.ok(service.addMusica(playlistId, musicaId));
    }

    @Operation(
        summary = "Remover música da playlist",
        description = "Remove uma música de uma playlist. Não remove a música do sistema, apenas o vínculo com a playlist."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Música removida da playlist com sucesso"),
        @ApiResponse(responseCode = "404", description = "Playlist ou música não encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{playlistId}/musicas/{musicaId}")
    public ResponseEntity<?> removeMusica(
            @Parameter(description = "ID da playlist") @PathVariable Long playlistId,
            @Parameter(description = "ID da música a remover") @PathVariable Long musicaId
    ) {
        service.removeMusica(playlistId, musicaId);
        return ResponseEntity.noContent().build();
    }
}
