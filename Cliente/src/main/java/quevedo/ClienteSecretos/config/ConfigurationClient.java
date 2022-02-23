package quevedo.ClienteSecretos.config;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

@Getter
@Singleton
public class ConfigurationClient {

    private final String pathbase;

    public ConfigurationClient() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Iterable<Object> it;
        it = yaml.loadAll(new FileInputStream(ConstantesConfig.CONFIG_YAML));

        Map<String, String> mp = (Map) it.iterator().next();
        this.pathbase = mp.get(ConstantesConfig.PATH_BASE);


    }
}
