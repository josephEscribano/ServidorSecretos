package quevedo.ClienteSecretos.service;

import com.nimbusds.jose.util.X509CertUtils;
import io.reactivex.rxjava3.core.Single;
import io.vavr.control.Either;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;
import quevedo.ClienteSecretos.dao.UsuariosDAO;
import quevedo.ClienteSecretos.gui.controllers.FXMLPrincipalController;
import quevedo.ClienteSecretos.utils.Constantes;
import quevedo.ClienteSecretos.utils.EncriptacionAsimetrica;
import quevedo.Common.modelos.Usuario;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.Common.modelos.UsuarioRegistroDTO;

import javax.inject.Inject;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class ServiceUsuario {

    private final UsuariosDAO usuariosDAO;



    @Inject
    public ServiceUsuario(UsuariosDAO usuariosDAO) {
        this.usuariosDAO = usuariosDAO;
    }

    public Either<String, String> registro(Usuario usuario){
        Either<String,String> result = null;
        EncriptacionAsimetrica asimetrica = new EncriptacionAsimetrica();
        Either<String, KeyPair> keys = asimetrica.generateKeys();


        if (keys.isRight()){
            String publica = Base64.getUrlEncoder().encodeToString(keys.get().getPublic().getEncoded());
            Either<String,UsuarioCertificado> resultadoRegistro = usuariosDAO.registro(
                    UsuarioRegistroDTO
                            .builder()
                            .clavePublica(publica)
                            .userName(usuario.getUserName())
                            .build()
            );

            if (resultadoRegistro.isRight()){
                X509Certificate cert = X509CertUtils.parse(Base64.getUrlDecoder().decode(resultadoRegistro.get().getCert()));
                boolean confirmacionKeyStore = asimetrica.crearKeyStore(usuario, keys.get().getPrivate(),cert);
                if (confirmacionKeyStore){
                    result = Either.right(Constantes.EL_USUARIO_HA_SIDO_REGISTRADO);
                }else{
                    result = Either.left(Constantes.ERROR_AL_CREAR_EL_KEYSTORE);
                }
            }else{
                result = Either.left(resultadoRegistro.getLeft());
            }


        }else{
            result = Either.left(keys.getLeft());
        }


        return result;


    }
}
