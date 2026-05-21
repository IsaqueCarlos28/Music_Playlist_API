package senac.tsi.Music_Playlist.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Playlists Musica",
                version = "0.0.1",
                description = """
                Crie e utilize playlists com suas musicas preferidas
                """,
                contact = @Contact(
                        name = "Isaque Carlos",
                        email = "isaquecarlos2016@gmail.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org" // URL to the MIT license
                )
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "apiKeyAuth";

        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )
                .components(
                        new Components()
                                .addParameters(
                                        "IdempotencyHeader",
                                        new HeaderParameter()
                                                .name("X-Idempotency-Key")
                                                .required(true)
                                                .description("Unique idempotency key")
                                )
                                .addSecuritySchemes(
                                        securitySchemeName,

                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .name("X-API-KEY")
                                )
                );
    }

    @Bean
    public OpenApiCustomizer idempotencyHeaderCustomizer() {

        return openApi -> {

            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().values().forEach(pathItem -> {

                Operation postOperation = pathItem.getPost();

                if (postOperation != null) {

                    postOperation.addParametersItem(
                            new Parameter()
                                    .in("header")
                                    .required(true)
                                    .name("X-Idempotency-Key")
                                    .description("Unique idempotency key")
                    );
                }
            });
        };
    }


}
