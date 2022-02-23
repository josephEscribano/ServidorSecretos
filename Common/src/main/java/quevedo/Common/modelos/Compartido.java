package quevedo.Common.modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compartido {
    private String id_secreto;
    private String userName;
    private String keyAsimetrica;
}
