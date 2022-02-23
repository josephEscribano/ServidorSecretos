package quevedo.servidorSecretos.EE.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import quevedo.servidorSecretos.utils.ConstantesRest;

import java.security.Key;

public class KeyProvider {
    @Produces
    @Singleton
    @Named(ConstantesRest.JWT)
    public Key key() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}
