package senac.tsi.Music_Playlist.filters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitFilter extends GenericFilterBean {
    // API KEY -> BUCKET
    private final Map<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    private Bucket createBucket() {

        Bandwidth limit = Bandwidth.classic(
                10, // capacity
                Refill.intervally(
                        10,
                        Duration.ofMinutes(1)
                )
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        String apiKey =
                httpRequest.getHeader("X-API-KEY");

        // OPTIONAL:
        // skip public endpoints
        String path = httpRequest.getRequestURI();

        if (
                path.startsWith("/auth")
                        || path.startsWith("/swagger-ui")
                        || path.startsWith("/v3/api-docs")
                        || path.startsWith("/h2-console")
        ) {

            chain.doFilter(request, response);
            return;
        }

        // if no key yet, use IP fallback
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = httpRequest.getRemoteAddr();
        }

        Bucket bucket = buckets.computeIfAbsent(
                apiKey,
                k -> createBucket()
        );

        if (bucket.tryConsume(1)) {

            chain.doFilter(request, response);

        } else {

            httpResponse.setStatus(429);

            httpResponse.setContentType(
                    MediaType.APPLICATION_JSON_VALUE
            );

            httpResponse.getWriter().write("""
                    {
                      "status": 429,
                      "error": "Too Many Requests",
                      "message": "Rate limit exceeded"
                    }
                    """);
        }
    }
}
