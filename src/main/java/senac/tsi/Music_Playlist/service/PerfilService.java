package senac.tsi.Music_Playlist.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.PerfilAssembler;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilInputDTO;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;
import senac.tsi.Music_Playlist.exceptions.ConflictException;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.PerfilMapper;
import senac.tsi.Music_Playlist.repository.PerfilRepository;
import senac.tsi.Music_Playlist.repository.UsuarioRepository;

@Service
public class PerfilService {

    private final PerfilRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilMapper mapper;
    private final PagedResourcesAssembler<PerfilResponseDTO> pagedAssembler;
    private final PerfilAssembler assembler;

    public PerfilService(
            PerfilRepository repository,
            UsuarioRepository usuarioRepository,
            PerfilMapper mapper,
            PagedResourcesAssembler<PerfilResponseDTO> pagedAssembler,
            PerfilAssembler assembler
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
    }

    @Transactional
    public PagedModel<EntityModel<PerfilResponseDTO>> getPage(
            Pageable pageable
    ) {
        Page<PerfilResponseDTO> page = repository.findAll(pageable)
                .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    @Transactional
    public EntityModel<PerfilResponseDTO> getByUsuarioId(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", usuarioId));

        if (usuario.getPerfil() == null){
            throw new NotFoundException("Perfil", "Usuario", usuarioId);
        }

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(usuario.getPerfil());
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public EntityModel<PerfilResponseDTO> create(PerfilInputDTO dto,Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", usuarioId));

        if (usuario.getPerfil() != null) {
            throw new ConflictException("Usuário já possui perfil","Perfil","Perfil",usuario.getPerfil().getId());
        }

        Perfil perfil = mapper.toEntity(dto);
        usuario.setPerfil(perfil);
        var saved = usuarioRepository.save(usuario);

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(saved.getPerfil());
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public EntityModel<PerfilResponseDTO> update(
            Long usuarioId,
            PerfilInputDTO dto
    ) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", usuarioId));
        Perfil perfil = usuario.getPerfil();

        if (perfil == null) {
            throw new NotFoundException("Perfil", "usuarioId", usuarioId);
        }

        perfil.setFotoUrl(dto.fotoUrl());
        perfil.setBiografia(dto.biografia());

        repository.save(perfil);

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(perfil);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public void delete(Long usuarioId) {


        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", usuarioId));

        if (usuario.getPerfil() == null) {
            throw new NotFoundException("Perfil", "usuarioId", usuarioId);
        }

        usuario.setPerfil(null);

        usuarioRepository.save(usuario);
    }

    // CUSTOM QUERY
    public PagedModel<EntityModel<PerfilResponseDTO>> findByUsuario(
            String nome,
            Pageable pageable
    ) {

        Page<PerfilResponseDTO> page =
                repository
                        .findByUsuarioNomeContainingIgnoreCase(
                                nome,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }
}
