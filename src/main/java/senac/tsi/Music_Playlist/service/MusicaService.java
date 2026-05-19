package senac.tsi.Music_Playlist.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.MusicaAssembler;
import senac.tsi.Music_Playlist.domains.Artista;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.dtos.musica.MusicaInputDTO;
import senac.tsi.Music_Playlist.dtos.musica.MusicaResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.MusicaMapper;
import senac.tsi.Music_Playlist.repository.ArtistaRepository;
import senac.tsi.Music_Playlist.repository.MusicaRepository;

@Service
public class MusicaService {

    private final MusicaRepository repository;
    private final ArtistaRepository artistaRepository;
    private final MusicaMapper mapper;
    private final PagedResourcesAssembler<MusicaResponseDTO> pagedAssembler;
    private final MusicaAssembler assembler;

    public MusicaService(
            MusicaRepository repository,
            ArtistaRepository artistaRepository,
            MusicaMapper mapper,
            PagedResourcesAssembler<MusicaResponseDTO> pagedAssembler,
            MusicaAssembler assembler
    ) {
        this.repository = repository;
        this.artistaRepository = artistaRepository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<MusicaResponseDTO>>
    getPage(Pageable pageable) {

        Page<MusicaResponseDTO> page =
                repository.findAll(pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    public EntityModel<MusicaResponseDTO> getById(Long id) {

        Musica musica = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Musica",
                                "id",
                                id
                        )
                );
        MusicaResponseDTO responseDTO = mapper.toResponseDTO(musica);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<MusicaResponseDTO> create(
            MusicaInputDTO dto
    ) {

        Artista artista = artistaRepository
                .findById(dto.artistaId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Artista",
                                "id",
                                dto.artistaId()
                        )
                );

        Musica musica = mapper.toEntity(dto, artista);

        Musica saved = repository.save(musica);

        MusicaResponseDTO responseDTO = mapper.toResponseDTO(saved);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<MusicaResponseDTO> update(
            Long id,
            MusicaInputDTO dto
    ) {

        Musica musica = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Musica",
                                "id",
                                id
                        )
                );

        Artista artista = artistaRepository
                .findById(dto.artistaId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Artista",
                                "id",
                                dto.artistaId()
                        )
                );

        musica.setTitulo(dto.titulo());
        musica.setDuracaoSegundos(dto.duracaoSegundos());
        musica.setArtista(artista);

        repository.save(musica);

        MusicaResponseDTO responseDTO = mapper.toResponseDTO(musica);
        return assembler.toModel(responseDTO);
    }

    public void delete(Long id) {

        Musica musica = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Musica",
                                "id",
                                id
                        )
                );

        repository.delete(musica);
    }

    // CUSTOM QUERY - TITLE
    public PagedModel<EntityModel<MusicaResponseDTO>>
    findByTitulo(
            String titulo,
            Pageable pageable
    ) {

        Page<MusicaResponseDTO> page =
                repository
                        .findByTituloContainingIgnoreCase(
                                titulo,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page,assembler);
    }

    // CUSTOM QUERY - ARTIST
    public PagedModel<EntityModel<MusicaResponseDTO>>
    findByArtista(
            String artista,
            Pageable pageable
    ) {

        Page<MusicaResponseDTO> page =
                repository
                        .findByArtistaNomeContainingIgnoreCase(
                                artista,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    // CUSTOM QUERY - GENRE
    public PagedModel<EntityModel<MusicaResponseDTO>>
    findByGenero(
            String genero,
            Pageable pageable
    ) {

        Page<MusicaResponseDTO> page =
                repository
                        .findByArtistaGeneroContainingIgnoreCase(
                                genero,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }
}