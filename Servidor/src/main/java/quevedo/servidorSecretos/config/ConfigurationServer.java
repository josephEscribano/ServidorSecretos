package quevedo.servidorSecretos.config;

import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Getter
@Log4j2
@Singleton
public class ConfigurationServer {

    private String ruta;
    private String password;
    private String user;
    private String driver;
    private String passKeyStore;


    public ConfigurationServer() {

        try {
            Yaml yaml = new Yaml();
            Iterable<Object> it;

            it = yaml.loadAll(this.getClass().getClassLoader().getResourceAsStream(ConstantesConfig.RUTA_CONFIGURACION));

            Map<String, String> m = (Map) it.iterator().next();

            this.ruta = m.get(ConstantesConfig.RUTA);
            this.password = m.get(ConstantesConfig.PASSWORD);
            this.user = m.get(ConstantesConfig.USER);
            this.driver = m.get(ConstantesConfig.DRIVER);
            this.passKeyStore = m.get(ConstantesConfig.PASS_KEY_STORE);


        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
