package senac.tsi.Music_Playlist.service;

import jakarta.transaction.Transactional;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public PagedModel<EntityModel<PlaylistResponseDTO>>
    getPage(Pageable pageable) {

        Page<PlaylistResponseDTO> page =
                repository.findAll(pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    @Transactional
    public EntityModel<PlaylistResponseDTO> getById(Long id) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Playlist", "id", id));

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public EntityModel<PlaylistResponseDTO> create(PlaylistInputDTO dto) {

        Usuario usuario = usuarioRepository
                .findById(dto.usuarioId())
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", dto.usuarioId()));

        Set<Musica> musicas = resolveMusicas(dto.musicasIds()).stream().collect(Collectors.toSet());

        Playlist playlist = mapper.toEntity(dto, usuario, musicas);

        usuario.addPlaylist(playlist);

        Playlist saved = repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(saved);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public EntityModel<PlaylistResponseDTO> update(Long id, PlaylistInputDTO dto) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Playlist", "id", id));

        Usuario usuario = usuarioRepository
                .findById(dto.usuarioId())
                .orElseThrow(() ->
                        new NotFoundException("Usuario", "id", dto.usuarioId()));

        Set<Musica> musicas = resolveMusicas(dto.musicasIds()).stream().collect(Collectors.toSet());

        playlist.setNome(dto.nome());
        playlist.setUsuario(usuario);
        playlist.setMusicas(musicas);

        repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    @Transactional
    public void delete(Long id) {

        Playlist playlist = repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Playlist", "id", id));

        repository.delete(playlist);
    }

    // CUSTOM QUERY
    @Transactional
    public PagedModel<EntityModel<PlaylistResponseDTO>>
    findByNome(String nome, Pageable pageable) {

        Page<PlaylistResponseDTO> page =
                repository
                        .findByNomeContainingIgnoreCase(nome, pageable)
                        .map(mapper::toResponseDTO);

        return pagedAssembler.toModel(page, assembler);
    }

    // ADD MUSIC
    @Transactional
    public EntityModel<PlaylistResponseDTO> addMusica(Long playlistId, Long musicaId) {

        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() ->
                        new NotFoundException("Playlist", "id", playlistId));

        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() ->
                        new NotFoundException("Musica", "id", musicaId));

        if (!playlist.getMusicas().contains(musica)) {
            playlist.getMusicas().add(musica);
        }

        repository.save(playlist);

        PlaylistResponseDTO responseDTO = mapper.toResponseDTO(playlist);
        return assembler.toModel(responseDTO);
    }

    // REMOVE MUSIC
    @Transactional
    public void removeMusica(Long playlistId, Long musicaId) {

        Playlist playlist = repository.findById(playlistId)
                .orElseThrow(() ->
                        new NotFoundException("Playlist", "id", playlistId));

        Musica musica = musicaRepository.findById(musicaId)
                .orElseThrow(() ->
                        new NotFoundException("Musica", "id", musicaId));

        playlist.getMusicas().remove(musica);

        repository.save(playlist);
    }

    /**
     * Validates that every requested music ID exists, throwing
     * NotFoundException for the first missing one.
     */
    @Transactional
    private List<Musica> resolveMusicas(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        List<Musica> musicas = musicaRepository.findAllById(ids);
        if (musicas.size() != ids.size()) {
            List<Long> foundIds = musicas.stream().map(Musica::getId).toList();
            Long missingId = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .findFirst()
                    .orElseThrow();
            throw new NotFoundException("Musica", "id", missingId);
        }
        return musicas;
    }
}
