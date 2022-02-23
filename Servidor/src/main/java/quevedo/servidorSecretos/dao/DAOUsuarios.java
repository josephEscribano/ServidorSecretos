package quevedo.servidorSecretos.dao;

import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import quevedo.Common.errores.Apierror;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.servidorSecretos.dao.conexionDB.DBConnectionPool;
import quevedo.servidorSecretos.dao.utils.ConstantesDao;
import quevedo.servidorSecretos.dao.utils.Querys;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Log4j2
public class DAOUsuarios {

    private final DBConnectionPool dbConnectionPool;

    @Inject
    public DAOUsuarios(DBConnectionPool dbConnectionPool) {
        this.dbConnectionPool = dbConnectionPool;
    }


    public Either<Apierror, UsuarioCertificado> registrarUsuario(UsuarioCertificado usuarioCertificado){

        Either<Apierror,UsuarioCertificado> result;
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dbConnectionPool.getHikariDataSource());


            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con
                        .prepareStatement(Querys.INSERT_USUARIO,
                                Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, usuarioCertificado.getUserName());
                preparedStatement.setString(2, usuarioCertificado.getCert());

                return preparedStatement;
            }, keyHolder);

            usuarioCertificado.setIdUsuario(keyHolder.getKey().toString());

            result = Either.right(usuarioCertificado);



        }catch (CannotGetJdbcConnectionException e){
            log.error(e.getMessage(), e);
            result = Either.left(new Apierror(ConstantesDao.ERROR_CONEXION));
        }

        return result;
    }
}
