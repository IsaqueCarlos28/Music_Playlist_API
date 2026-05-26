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
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.dtos.ErrorResponse;
import senac.tsi.Music_Playlist.dtos.usuario.RoleChangeDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;
import senac.tsi.Music_Playlist.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários da plataforma")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados de forma paginada. Requer autenticação.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(pageable));
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico pelo seu identificador.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> getById(
            @Parameter(description = "ID do usuário") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
        summary = "Cadastrar usuário",
        description = "Cria um novo usuário com perfil USER. Este é o único endpoint público — não exige X-API-KEY. " +
                      "Inclua X-Idempotency-Key para evitar cadastros duplicados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> create(
            @RequestBody @Valid UsuarioInputDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza nome, e-mail e senha de um usuário existente. A senha é re-criptografada automaticamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "E-mail já está em uso por outro usuário",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> update(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @RequestBody @Valid UsuarioInputDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Alterar a Role de um usuario",
            description = "Atualiza Role de um usuário existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados ou operação duplicada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping(value = "/{id}/role",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> alterarRole(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @RequestBody @Valid RoleChangeDTO dto
    ) {
        return ResponseEntity.ok(service.changeRole(id, dto));
    }

    @Operation(summary = "Remover usuário", description = "Remove um usuário e todas as suas playlists associadas.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar o recurso",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "415", description = "O formato de documento de patch não é suportado",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuário") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar usuários por nome", description = "Consulta personalizada que filtra usuários pelo nome (busca parcial, sem diferenciar maiúsculas).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Chave de API ausente ou inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>> findByNome(
            @Parameter(description = "Trecho do nome a pesquisar") @RequestParam String nome,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(service.findByNome(nome, pageable));
    }
}
