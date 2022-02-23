package quevedo.servidorSecretos.service;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import quevedo.Common.errores.Apierror;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.Common.modelos.UsuarioRegistroDTO;
import quevedo.servidorSecretos.config.ConfigurationServer;
import quevedo.servidorSecretos.dao.DAOUsuarios;
import quevedo.servidorSecretos.utils.Constantes;
import quevedo.servidorSecretos.utils.EncriptacionAsimetrica;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ServiceUsuario {

    private final DAOUsuarios daoUsuarios;
    private final ConfigurationServer configuration;


    @Inject
    public ServiceUsuario(DAOUsuarios daoUsuarios, ConfigurationServer configurationServer) {
        this.daoUsuarios = daoUsuarios;
        this.configuration = configurationServer;
    }

    public Either<Apierror, UsuarioCertificado> registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO){
        EncriptacionAsimetrica asimetrica = new EncriptacionAsimetrica();
        Either<Apierror,UsuarioCertificado> result;
        Either<String, PublicKey> reconstruirPublica = asimetrica.reconstruirPublica(usuarioRegistroDTO.getClavePublica());

        if (reconstruirPublica.isRight()){
            Either<String, PrivateKey> getPrivadaServer = asimetrica.getPrivateKey(configuration.getPassKeyStore());
            if (getPrivadaServer.isRight()){
                Either<String,String> certificado = asimetrica.crearCerfiticado(
                        usuarioRegistroDTO.getUserName(),
                        reconstruirPublica.get(),
                        getPrivadaServer.get());
                if (certificado.isRight()){
                    Either<Apierror,UsuarioCertificado> resultDao = daoUsuarios.registrarUsuario(UsuarioCertificado.builder()
                            .userName(usuarioRegistroDTO.getUserName())
                            .cert(certificado.get())
                            .build());
                    if (resultDao.isRight()){
                        result = Either.right(resultDao.get());
                    }else{
                        result = Either.left(resultDao.getLeft());
                    }

                }else{
                    result = Either.left(new Apierror(getPrivadaServer.getLeft()));
                }
            }else{
                result = Either.left(new Apierror(getPrivadaServer.getLeft()));
            }
        }else{
            result = Either.left(new Apierror(reconstruirPublica.getLeft()));
        }

        return result;

    }
}
