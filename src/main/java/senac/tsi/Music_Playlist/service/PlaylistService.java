package senac.tsi.Music_Playlist.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.assemblers.PlaylistAssembler;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.domains.Playlist;
import senac.tsi.Music_Playlist.domains.Usuario;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistInputDTO;
import senac.tsi.Music_Playlist.dtos.playlist.PlaylistResponseDTO;
import senac.tsi.Music_Playlist.exceptions.NotFoundException;
import senac.tsi.Music_Playlist.mapper.PlaylistMapper;
import senac.tsi.Music_Playlist.repository.MusicaRepository;
import senac.tsi.Music_Playlist.repository.PlaylistRepository;
import senac.tsi.Music_Playlist.repository.UsuarioRepository;

import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final MusicaRepository musicaRepository;
    private final PlaylistMapper mapper;
    private final PagedResourcesAssembler<PlaylistResponseDTO> pagedAssembler;
    private final PlaylistAssembler assembler;

    public PlaylistService(
            PlaylistRepository repository,
            UsuarioRepository usuarioRepository,
            MusicaRepository musicaRepository,
            PlaylistMapper mapper,
            PagedResourcesAssembler<PlaylistResponseDTO> pagedAssembler,
            PlaylistAssembler assembler
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.musicaRepository = musicaRepository;
        this.mapper = mapper;
        this.pagedAssembler = pagedAssembler;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PlaylistResponseDTO>>
    getPage(Pageable pageable) {

        Page<PlaylistResponseDTO> page =
                repository.findAll(pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    public EntityModel<PlaylistResponseDTO> getById(Long id) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Playlist",
                                "id",
                                id
                        )
                );

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<PlaylistResponseDTO> create(
            PlaylistInputDTO dto
    ) {

        Usuario usuario = usuarioRepository
                .findById(dto.usuarioId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuario",
                                "id",
                                dto.usuarioId()
                        )
                );

        List<Musica> musicas =
                musicaRepository.findAllById(
                        dto.musicasIds()
                );

        Playlist playlist =
                mapper.toEntity(
                        dto,
                        usuario,
                        musicas
                );

        Playlist saved = repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(saved);
        return assembler.toModel(responseDTO);
    }

    public EntityModel<PlaylistResponseDTO> update(
            Long id,
            PlaylistInputDTO dto
    ) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Playlist",
                                "id",
                                id
                        )
                );

        Usuario usuario = usuarioRepository
                .findById(dto.usuarioId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Usuario",
                                "id",
                                dto.usuarioId()
                        )
                );

        List<Musica> musicas =
                musicaRepository.findAllById(
                        dto.musicasIds()
                );

        playlist.setNome(dto.nome());
        playlist.setUsuario(usuario);
        playlist.setMusicas(musicas);

        repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    public void delete(Long id) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Playlist",
                                "id",
                                id
                        )
                );

        repository.delete(playlist);
    }

    // CUSTOM QUERY
    public PagedModel<EntityModel<PlaylistResponseDTO>>
    findByNome(
            String nome,
            Pageable pageable
    ) {

        Page<PlaylistResponseDTO> page =
                repository
                        .findByNomeContainingIgnoreCase(
                                nome,
                                pageable
                        )
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    // ADD MUSIC
    public EntityModel<PlaylistResponseDTO> addMusica(
            Long playlistId,
            Long musicaId
    ) {

        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Playlist",
                                "id",
                                playlistId
                        )
                );

        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Musica",
                                "id",
                                musicaId
                        )
                );

        if (!playlist.getMusicas().contains(musica)) {
            playlist.getMusicas().add(musica);
        }

        repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    // REMOVE MUSIC
    public void removeMusica(
            Long playlistId,
            Long musicaId
    ) {

        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Playlist",
                                "id",
                                playlistId
                        )
                );

        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Musica",
                                "id",
                                musicaId
                        )
                );

        playlist.getMusicas().remove(musica);

        repository.save(playlist);
    }
}
