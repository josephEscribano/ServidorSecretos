package quevedo.ClienteSecretos.dao;


import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import quevedo.ClienteSecretos.dao.retrofit.RetrofitUsuarios;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.Common.modelos.UsuarioRegistroDTO;

import javax.inject.Inject;

public class UsuariosDAO extends DAOGenerics{

    private final RetrofitUsuarios retrofitUsuarios;

    @Inject
    public UsuariosDAO(RetrofitUsuarios retrofitUsuarios) {
        this.retrofitUsuarios = retrofitUsuarios;
    }

    public Either<String, UsuarioCertificado> registro(UsuarioRegistroDTO usuarioRegistroDTO){
        return safeApiCall(retrofitUsuarios.registrarUsuario(usuarioRegistroDTO));
    }
}
