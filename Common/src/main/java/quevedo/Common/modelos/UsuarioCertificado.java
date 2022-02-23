package quevedo.Common.modelos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioCertificado {
    private String idUsuario;
    private String userName;
    private String cert;
}
