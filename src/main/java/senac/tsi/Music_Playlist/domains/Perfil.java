package senac.tsi.Music_Playlist.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "perfis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A foto URL é obrigatória")
    @Column(nullable = false)
    private String fotoUrl;

    @NotBlank(message = "A biografia é obrigatória")
    @Size(max = 255, message = "A biografia deve ter no máximo 255 caracteres")
    @Column(nullable = false, length = 255)
    private String biografia;

    // One-to-One
    @OneToOne(mappedBy = "perfil")
    @JsonIgnore
    private Usuario usuario;
}
