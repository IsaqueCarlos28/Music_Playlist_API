package senac.tsi.Music_Playlist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.UsuarioAssembler;
import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;
import senac.tsi.Music_Playlist.exceptions.BusinessException;
import senac.tsi.Music_Playlist.exceptions.ConflictException;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.UsuarioMapper;
import senac.tsi.Music_Playlist.repository.PerfilRepository;
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

    public PagedModel<EntityModel<UsuarioResponseDTO>> getPage(Pageable pageable) {

        Page<UsuarioResponseDTO> page = repository.findAll(pageable)
                .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    public EntityModel<UsuarioResponseDTO> getById(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", id));

        UsuarioResponseDTO responseDTO = mapper.toResponseDTO(usuario);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<UsuarioResponseDTO> create(UsuarioInputDTO dto) {

        if (repository.existsByEmail(dto.email())) {
            throw new ConflictException(
                    "Email already in use",
                    "Usuario",
                    "email",
                    dto.email()
            );
        }

        String senhaHash = passwordEncoder.encode(dto.senha());
        Usuario usuario = mapper.toEntity(dto, Role.USER, senhaHash);

        repository.save(usuario);

        UsuarioResponseDTO responseDTO = mapper.toResponseDTO(usuario);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<UsuarioResponseDTO> update(Long id, UsuarioInputDTO dto) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", id));

        if (!usuario.getEmail().equals(dto.email())
                && repository.existsByEmail(dto.email())) {
            throw new BusinessException(
                    "Email already in use",
                    "Usuario",
                    "email",
                    dto.email()
            );
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        repository.save(usuario);

        UsuarioResponseDTO responseDTO = mapper.toResponseDTO(usuario);
        return assembler.toModel(responseDTO);
    }

    public void delete(Long id) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", id));

        repository.delete(usuario);
    }

    // CUSTOM QUERY
    public PagedModel<EntityModel<UsuarioResponseDTO>>
    findByNome(String nome, Pageable pageable) {

        Page<UsuarioResponseDTO> page =
                repository.findByNomeContainingIgnoreCase(nome, pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }
}
