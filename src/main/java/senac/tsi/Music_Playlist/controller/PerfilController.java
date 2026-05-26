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
import senac.tsi.Music_Playlist.dtos.perfil.PerfilInputDTO;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;
import senac.tsi.Music_Playlist.service.PerfilService;

@RestController
@Tag(name = "Perfis", description = "Gerenciamento de perfis de usuários (relacionamento 1:1 com Usuário)")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @Operation(summary = "Listar perfis", description = "Retorna todos os perfis cadastrados de forma paginada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/perfis")
    public ResponseEntity<PagedModel<EntityModel<PerfilResponseDTO>>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(pageable));
    }

    @Operation(summary = "Buscar perfil do Usuario", description = "Retorna os dados de um perfil específico pelo identificador de seu usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/usuarios/{usuarioId}/perfil")
    public ResponseEntity<EntityModel<PerfilResponseDTO>> getById(
            @Parameter(description = "ID do usuario") @PathVariable Long usuarioId
    ) {
        return ResponseEntity.ok(service.getByUsuarioId(usuarioId));
    }

    @Operation(
        summary = "Cadastrar perfil",
        description = "Cria um novo perfil de usuário com foto e biografia. " +
                      "Inclua X-Idempotency-Key para evitar cadastros duplicados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/usuarios/{usuarioId}/perfil")
    public ResponseEntity<EntityModel<PerfilResponseDTO>> create(
            @PathVariable Long usuarioId,
            @RequestBody @Valid PerfilInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto,usuarioId));
    }

    @Operation(summary = "Atualizar perfil", description = "Atualiza foto e biografia de um perfil existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/usuarios/{usuarioId}/perfil")
    public ResponseEntity<EntityModel<PerfilResponseDTO>> update(
            @Parameter(description = "ID do usuario") @PathVariable Long usuarioId,
            @RequestBody @Valid PerfilInputDTO dto
    ) {
        return ResponseEntity.ok(service.update(usuarioId, dto));
    }

    @Operation(summary = "Remover perfil", description = "Remove o perfil de um usuario do sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Perfil removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("usuarios/{usuarioId}/perfil")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuario") @PathVariable Long usuarioId
    ) {
        service.delete(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar perfis por nome de usuário", description = "Consulta personalizada que filtra perfis pelo nome do usuário vinculado (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("perfis/usuario")
    public ResponseEntity<PagedModel<EntityModel<PerfilResponseDTO>>> findByUsuario(
            @Parameter(description = "Trecho do nome do usuário a pesquisar") @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByUsuario(nome, pageable));
    }
}
