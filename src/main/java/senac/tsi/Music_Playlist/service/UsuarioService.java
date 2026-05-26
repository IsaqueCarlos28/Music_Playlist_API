package senac.tsi.Music_Playlist.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.UsuarioAssembler;
import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.usuario.RoleChangeDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;
import senac.tsi.Music_Playlist.exceptions.BusinessException;
import senac.tsi.Music_Playlist.exceptions.ConflictException;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.UsuarioMapper;
import senac.tsi.Music_Playlist.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PagedResourcesAssembler<UsuarioResponseDTO> pagedAssembler;
    private final UsuarioAssembler assembler;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository repository,
            UsuarioMapper mapper,
            PagedResourcesAssembler<UsuarioResponseDTO> pagedAssembler,
            UsuarioAssembler assembler,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PagedModel<EntityModel<UsuarioResponseDTO>> getPage(Pageable pageable) {
        Page<UsuarioResponseDTO> page = repository.findAll(pageable)
                .map(mapper::toResponseDTO);
        return pagedAssembler.toModel(page, assembler);
    }

    @Transactional
    public EntityModel<UsuarioResponseDTO> getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", "id", id));
        return assembler.toModel(mapper.toResponseDTO(usuario));
    }

    public EntityModel<UsuarioResponseDTO> create(UsuarioInputDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new ConflictException("Email já está em uso", "Usuario", "email", dto.email());
        }

        String senhaHash = passwordEncoder.encode(dto.senha());
        Usuario usuario = mapper.toEntity(dto, Role.USER, senhaHash);
        repository.save(usuario);

        return assembler.toModel(mapper.toResponseDTO(usuario));
    }

    @Transactional
    public EntityModel<UsuarioResponseDTO> update(Long id, UsuarioInputDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", "id", id));

        if (!usuario.getEmail().equals(dto.email())
                && repository.existsByEmail(dto.email())) {
            throw new ConflictException(
                    "Email já está em uso", "Usuario", "email", dto.email()
            );
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        // Re-hash and persist the new password
        usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        repository.save(usuario);

        return assembler.toModel(mapper.toResponseDTO(usuario));
    }

    @Transactional
    public EntityModel<UsuarioResponseDTO> changeRole(Long id, RoleChangeDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", "id", id));

        if (usuario.getRole().equals(dto.role())){
            throw new ConflictException("Usuario ja possui essa ROLE","Usuario","ROLE",usuario.getRole().toString());
        }
        usuario.setRole(dto.role());

        repository.save(usuario);

        return assembler.toModel(mapper.toResponseDTO(usuario));
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario", "id", id));
        repository.delete(usuario);
    }

    @Transactional
    public PagedModel<EntityModel<UsuarioResponseDTO>> findByNome(String nome, Pageable pageable) {
        Page<UsuarioResponseDTO> page =
                repository.findByNomeContainingIgnoreCase(nome, pageable)
                        .map(mapper::toResponseDTO);
        return pagedAssembler.toModel(page, assembler);
    }
}
