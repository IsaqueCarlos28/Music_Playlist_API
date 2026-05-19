package senac.tsi.Music_Playlist.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import senac.tsi.Music_Playlist.service.AuthenticationService;

import java.io.IOException;
import java.io.PrintWriter;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        Authentication authentication =
                authenticationService.getAuthentication(
                        (HttpServletRequest) request
                );

        // ENDPOINTS PUBLICOS
        if (
                path.startsWith("/auth")
                        || path.startsWith("/swagger-ui")
                        || path.startsWith("/v3/api-docs")
                        || path.startsWith("/h2-console")
        ) {

            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
