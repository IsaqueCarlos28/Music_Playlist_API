package senac.tsi.Music_Playlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.Musica;
import senac.tsi.Music_Playlist.domains.Playlist;

public interface MusicaRepository extends JpaRepository<Musica,Long> {
    // Search by music title
    Page<Musica> findByTituloContainingIgnoreCase(
            String titulo,
            Pageable pageable
    );

    // Search by artist name
    Page<Musica> findByArtistaNomeContainingIgnoreCase(
            String artista,
            Pageable pageable
    );

    // Search by artist genre
    Page<Musica> findByArtistaGeneroContainingIgnoreCase(
            String genero,
            Pageable pageable
    );
}
