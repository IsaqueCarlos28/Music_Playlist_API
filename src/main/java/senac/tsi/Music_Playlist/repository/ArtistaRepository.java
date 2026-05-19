package senac.tsi.Music_Playlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.Artista;

public interface ArtistaRepository extends JpaRepository<Artista,Long> {
    Page<Artista> findByGeneroContainingIgnoreCase(String genero, Pageable pageable);
}
