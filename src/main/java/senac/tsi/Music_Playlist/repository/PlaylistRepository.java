package senac.tsi.Music_Playlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    Page<Playlist> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
