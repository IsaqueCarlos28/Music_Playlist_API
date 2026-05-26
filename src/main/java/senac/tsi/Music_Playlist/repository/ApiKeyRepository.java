package senac.tsi.Music_Playlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import senac.tsi.Music_Playlist.domains.ApiKeys;
import senac.tsi.Music_Playlist.domains.Enum.Role;
import senac.tsi.Music_Playlist.domains.Usuario;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKeys, Long> {

    Optional<ApiKeys> findByKeyAndActiveTrue(String key);

    Optional<ApiKeys> findByUsuarioAndActiveTrueAndRole(Usuario usuario, Role role);

    List<ApiKeys> findAllByUsuarioAndActiveTrue(Usuario usuario);
}
