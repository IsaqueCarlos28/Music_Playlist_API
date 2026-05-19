package senac.tsi.Music_Playlist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.PerfilAssembler;
import senac.tsi.Music_Playlist.domains.Perfil;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilInputDTO;
import senac.tsi.Music_Playlist.dtos.perfil.PerfilResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.PerfilMapper;
import senac.tsi.Music_Playlist.repository.PerfilRepository;

@Service
public class PerfilService {

    private final PerfilRepository repository;
    private final PerfilMapper mapper;
    private final PagedResourcesAssembler<PerfilResponseDTO> pagedAssembler;
    private final PerfilAssembler assembler;

    public PerfilService(
            PerfilRepository repository,
            PerfilMapper mapper,
            PagedResourcesAssembler<PerfilResponseDTO> pagedAssembler,
            PerfilAssembler assembler
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PerfilResponseDTO>> getPage(
            Pageable pageable
    ) {

        Page<PerfilResponseDTO> page = repository.findAll(pageable)
                .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    public EntityModel<PerfilResponseDTO> getById(Long id) {

        Perfil perfil = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil", "id", id));

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(perfil);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<PerfilResponseDTO> create(PerfilInputDTO dto) {

        Perfil perfil = mapper.toEntity(dto);

        Perfil saved = repository.save(perfil);

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(saved);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<PerfilResponseDTO> update(
            Long id,
            PerfilInputDTO dto
    ) {

        Perfil perfil = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil", "id", id));

        perfil.setFotoUrl(dto.fotoUrl());
        perfil.setBiografia(dto.biografia());

        repository.save(perfil);

        PerfilResponseDTO responseDTO= mapper.toResponseDTO(perfil);
        return assembler.toModel(responseDTO);
    }

    public void delete(Long id) {

        Perfil perfil = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Perfil", "id", id));

        repository.delete(perfil);
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
