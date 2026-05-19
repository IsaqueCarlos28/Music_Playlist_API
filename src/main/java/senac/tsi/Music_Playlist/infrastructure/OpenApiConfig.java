package senac.tsi.Music_Playlist.infrastructure;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
}
