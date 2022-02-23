package quevedo.ClienteSecretos.dao.retrofit;

import io.reactivex.rxjava3.core.Single;
import quevedo.Common.modelos.Usuario;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.Common.modelos.UsuarioRegistroDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitUsuarios {


    @POST("usuarios")
    Call<UsuarioCertificado> registrarUsuario(@Body UsuarioRegistroDTO usuarioRegistroDTO);

}
