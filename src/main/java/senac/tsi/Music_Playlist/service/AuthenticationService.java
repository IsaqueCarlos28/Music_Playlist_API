package senac.tsi.Music_Playlist.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import senac.tsi.Music_Playlist.domains.ApiKeys;
import senac.tsi.Music_Playlist.infrastructure.ApiKeyAuthentication;
import senac.tsi.Music_Playlist.repository.ApiKeyRepository;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private static final String HEADER = "X-API-KEY";

    private final ApiKeyRepository repository;

    public AuthenticationService(ApiKeyRepository repository) {
        this.repository = repository;
    }

    public Authentication getAuthentication(HttpServletRequest request) {

        String key = request.getHeader(HEADER);

        if (key == null || key.isBlank()) {
            throw new BadCredentialsException("Missing API Key");
        }

        ApiKeys apiKeys = repository.findByKeyAndActiveTrue(key)
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid API Key"));

        if (apiKeys.getExpiresAt() != null &&
                apiKeys.getExpiresAt().isBefore(LocalDateTime.now())) {

            throw new BadCredentialsException("API Key expired");
        }

        return new ApiKeyAuthentication(
                key,
                AuthorityUtils.createAuthorityList(apiKeys.getRole().toString())
        );
    }
}
