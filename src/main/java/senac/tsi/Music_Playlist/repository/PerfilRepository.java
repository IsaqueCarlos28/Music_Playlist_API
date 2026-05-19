package senac.tsi.Music_Playlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil,Long> {
    Page<Perfil> findByUsuarioNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );
}
