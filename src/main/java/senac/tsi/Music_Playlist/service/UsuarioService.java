package senac.tsi.Music_Playlist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import senac.tsi.Music_Playlist.assemblers.UsuarioAssembler;
import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioInputDTO;
import senac.tsi.Music_Playlist.dtos.usuario.UsuarioResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.UsuarioMapper;
import senac.tsi.Music_Playlist.repository.PerfilRepository;
import senac.tsi.Music_Playlist.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PerfilRepository perfilRepository;
    private final UsuarioMapper mapper;
    private final PagedResourcesAssembler<UsuarioResponseDTO> pagedAssembler;
    private final UsuarioAssembler assembler;

    public UsuarioService(
            UsuarioRepository repository,
            PerfilRepository perfilRepository,
            UsuarioMapper mapper,
            PagedResourcesAssembler<UsuarioResponseDTO> pagedAssembler,
            UsuarioAssembler assembler
    ) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
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

        Perfil perfil = perfilRepository.findById(dto.perfilId())
                .orElseThrow(() ->
                        new NotFoundException("Perfil", "id", dto.perfilId()));

        String senhaHash = dto.senha();
        Usuario usuario = mapper.toEntity(dto, Role.USER,senhaHash, perfil);

        Usuario saved = repository.save(usuario);

        UsuarioResponseDTO responseDTO = mapper.toResponseDTO(usuario);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<UsuarioResponseDTO> update(Long id, UsuarioInputDTO dto) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", id));

        Perfil perfil = perfilRepository.findById(dto.perfilId())
                .orElseThrow(() ->
                        new NotFoundException("Perfil", "id", dto.perfilId()));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPerfil(perfil);

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
