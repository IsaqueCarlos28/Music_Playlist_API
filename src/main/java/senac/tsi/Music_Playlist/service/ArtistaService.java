package senac.tsi.Music_Playlist.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.ArtistaAssembler;
import senac.tsi.Music_Playlist.domains.Artista;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaInputDTO;
import senac.tsi.Music_Playlist.dtos.artista.ArtistaResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.ArtistaMapper;
import senac.tsi.Music_Playlist.repository.ArtistaRepository;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;
    private final ArtistaMapper mapper;
    private final PagedResourcesAssembler<ArtistaResponseDTO> pagedAssembler;
    private final ArtistaAssembler assembler;

    public ArtistaService(
            ArtistaRepository repository,
            ArtistaMapper mapper,
            PagedResourcesAssembler<ArtistaResponseDTO> pagedAssembler,
            ArtistaAssembler assembler
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
    }
    @Transactional
    public PagedModel<EntityModel<ArtistaResponseDTO>>
    getPage(Pageable pageable) {

        Page<ArtistaResponseDTO> page =
                repository.findAll(pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    @Transactional
    public EntityModel<ArtistaResponseDTO> getById(Long id) {

        Artista artista = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Artista",
                                "id",
                                id
                        )
                );
        ArtistaResponseDTO responseDTO= mapper.toResponseDTO(artista);
        return assembler.toModel(responseDTO);
    }
    @Transactional
    public EntityModel<ArtistaResponseDTO> create(
            ArtistaInputDTO dto
    ) {

        Artista artista = mapper.toEntity(dto);

        Artista saved = repository.save(artista);

        ArtistaResponseDTO responseDTO= mapper.toResponseDTO(saved);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public EntityModel<ArtistaResponseDTO> update(
            Long id,
            ArtistaInputDTO dto
    ) {

        Artista artista = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Artista",
                                "id",
                                id
                        )
                );

        artista.setNome(dto.nome());
        artista.setGenero(dto.genero());

        repository.save(artista);

        ArtistaResponseDTO responseDTO= mapper.toResponseDTO(artista);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public void delete(Long id) {

        Artista artista = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Artista",
                                "id",
                                id
                        )
                );

        repository.delete(artista);
    }

    // CUSTOM QUERY
    @Transactional
    public PagedModel<EntityModel<ArtistaResponseDTO>>
    findByGenero(
            String genero,
            Pageable pageable
    ) {

        Page<ArtistaResponseDTO> page =
                repository
                        .findByGeneroContainingIgnoreCase(
                                genero,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }
}
