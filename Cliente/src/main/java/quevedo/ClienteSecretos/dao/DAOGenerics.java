package quevedo.ClienteSecretos.dao;

import com.google.gson.Gson;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import quevedo.ClienteSecretos.dao.utils.ConstantesDAO;
import quevedo.Common.errores.Apierror;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

@Log4j2
abstract class DAOGenerics {
    @Inject
    private Gson gson;


    public <T> Single<Either<String, T>> safeSingleApicall(Single<T> call) {
        return call.map(t -> Either.right(t).mapLeft(Object::toString))
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> {
                    Either<String, T> error = Either.left(ConstantesDAO.ERROR_AL_CONECTARSE);
                    if (throwable instanceof HttpException) {

                        if (Objects.equals(((HttpException) throwable).response().errorBody().contentType(), MediaType.get(ConstantesDAO.APPLICATION_JSON)) || Objects.equals(((HttpException) throwable).response().errorBody().contentType(), MediaType.get(ConstantesDAO.APPLICATION_JSON_CHARSET_ISO_8859_1))) {
                            Apierror apiError = gson.fromJson(((HttpException) throwable).response().errorBody().string(), Apierror.class);
                            error = Either.left(apiError.getMensaje());
                        } else {
                            error = Either.left(new Apierror(ConstantesDAO.ERROR_AL_CONECTARSE).getMensaje());
                        }
                    }
                    return error;
                });

    }

    public <T> Either<String, T> safeApiCall(Call<T> call) {
        Either<String, T> resultado;

        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                resultado = Either.right(response.body());
            } else {
                if (Objects.equals(response.errorBody().contentType(), MediaType.get(ConstantesDAO.MEDIA_TYPE_JSON))) {
                    Apierror apiError = gson.fromJson(response.errorBody().string(), Apierror.class);
                    resultado = Either.left(apiError.getMensaje());
                } else {
                    resultado = Either.left(new Apierror(ConstantesDAO.ERROR_AL_CONECTARSE).getMensaje());
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            resultado = Either.left(new Apierror(ConstantesDAO.ERROR_SERVIDOR).getMensaje());
        }

        return resultado;
    }
}
