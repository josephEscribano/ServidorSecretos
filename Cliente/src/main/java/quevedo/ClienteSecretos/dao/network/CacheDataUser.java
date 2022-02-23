package quevedo.ClienteSecretos.dao.network;

import lombok.Data;

import javax.inject.Singleton;

@Data
@Singleton
public class CacheDataUser {
    private String userName;
    private String pass;
    private String token;
}
