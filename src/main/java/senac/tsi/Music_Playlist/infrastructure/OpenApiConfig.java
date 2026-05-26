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
                API REST para criação e gerenciamento de playlists musicais.
                
                **Autenticação:** todos os endpoints (exceto POST /usuarios e POST /auth/login) exigem o cabeçalho `X-API-KEY`.
                Obtenha sua chave via `POST /auth/login`.
                
                **Idempotência:** requisições POST devem incluir o cabeçalho `X-Idempotency-Key` (UUID único por operação)
                para evitar processamento duplicado em caso de reenvio.
                
                **Rate Limiting:** máximo de 10 requisições por minuto por chave/IP.
                Os cabeçalhos `X-RateLimit-Limit` e `X-RateLimit-Remaining` são retornados em cada resposta.
                
                **Versionamento:** o endpoint `POST /musicas` aceita o cabeçalho `X-API-Version: 1` (sem link) ou `X-API-Version: 2` (com link obrigatório).
                
                ---
                
                ## HTTP Status Codes
            
                | Código | Significado |
                |--------|-------------|
                | 200 OK | Requisição realizada com sucesso |
                | 201 Created | Recurso criado com sucesso |
                | 204 No Content | Operação realizada sem conteúdo de retorno |
                | 400 Bad Request | Dados inválidos enviados pelo cliente |
                | 401 Unauthorized | API Key ausente ou inválida |
                | 403 Forbidden | Usuário sem permissão para acessar o recurso |
                | 404 Not Found | Recurso não encontrado |
                | 405 Method Not Allowed | O método de requisição 'POST' não é suportado. |
                | 409 Conflict | Conflito de dados ou operação duplicada |
                | 415 Unsupported Media Type | O formato de documento de patch não é suportado |
                | 422 Unprocessable Entity | Erro de validação |
                | 429 Too Many Requests | Limite de requisições excedido |
                | 500 Internal Server Error | Erro interno do servidor |
            
                ---
            
                ## Estrutura de erro padrão
            
                ```json
                {
                  "timestamp": "2026-05-26T14:00:00",
                  "status": 400,
                  "error": "Bad Request",
                  "message": "Validation failed",
                  "path": "/usuarios"
                }
                ```
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
