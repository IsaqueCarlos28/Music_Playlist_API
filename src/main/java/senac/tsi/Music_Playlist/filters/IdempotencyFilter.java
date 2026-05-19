package senac.tsi.Music_Playlist.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import senac.tsi.Music_Playlist.domains.IdempotencyKey;
import senac.tsi.Music_Playlist.service.IdempotencyService;

import java.io.IOException;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private final IdempotencyService service;

    public IdempotencyFilter(IdempotencyService service) {
        this.service = service;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getHeader("Idempotency-Key");

        if (key == null || key.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        IdempotencyKey existing =
                service.get(key, method, path);

        // 1. RETURN CACHED RESPONSE
        if (existing != null) {

            response.setStatus(existing.getStatusCode());
            response.setContentType("application/json");
            response.getWriter().write(existing.getResponseBody());
            return;
        }

        // 2. WRAP RESPONSE PROPERLY
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, wrappedResponse);

        // 3. READ RESPONSE BODY SAFELY
        String body = new String(
                wrappedResponse.getContentAsByteArray(),
                wrappedResponse.getCharacterEncoding()
        );

        // 4. SAVE IDEMPOTENCY RESULT
        service.save(
                key,
                method,
                path,
                body,
                wrappedResponse.getStatus()
        );

        // 5. IMPORTANT: COPY BACK RESPONSE
        wrappedResponse.copyBodyToResponse();
    }
}
