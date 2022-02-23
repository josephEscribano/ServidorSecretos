package quevedo.Common.modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Secreto {
    private String id;
    private String usuario;
    private String secreto;

    @Override
    public String toString() {
        return "secreto= " + secreto;
    }
}
