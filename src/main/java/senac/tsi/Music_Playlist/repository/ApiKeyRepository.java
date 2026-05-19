package senac.tsi.Music_Playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.ApiKey;
import senac.tsi.Music_Playlist.domains.Usuario;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {

    Optional<ApiKey> findByKeyAndActiveTrue(String key);

    Optional<ApiKey> findByUsuarioAndActiveTrue(Usuario usuario);

    List<ApiKey> findAllByUsuarioAndActiveTrue(Usuario usuario);
}
