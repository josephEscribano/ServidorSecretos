package quevedo.servidorSecretos.EE.rest;


import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;
import quevedo.Common.errores.Apierror;
import quevedo.Common.modelos.UsuarioCertificado;
import quevedo.Common.modelos.UsuarioRegistroDTO;
import quevedo.servidorSecretos.service.ServiceUsuario;
import quevedo.servidorSecretos.utils.ConstantesRest;

@Path(ConstantesRest.PATH_USUARIOS)
@Produces({MediaType.APPLICATION_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Log4j2
public class RestSecretos {
    private final ServiceUsuario serviceUsuario;

    @Inject
    public RestSecretos(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    @POST
    public Response registrarUsuario(UsuarioRegistroDTO usuarioRegistroDTO){
        Response response;

        Either<Apierror, UsuarioCertificado> result = serviceUsuario.registrarUsuario(usuarioRegistroDTO);

        if (result.isRight()) {
            response = Response.status(Response.Status.OK)
                    .entity(result.get())
                    .build();
        } else {
            response = Response.status(Response.Status.NOT_FOUND)
                    .entity(result.getLeft())
                    .build();
        }

        return response;
    }

}
