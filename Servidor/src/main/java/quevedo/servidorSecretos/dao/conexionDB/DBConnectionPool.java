package quevedo.servidorSecretos.dao.conexionDB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import quevedo.servidorSecretos.config.ConfigurationServer;
import quevedo.servidorSecretos.dao.utils.ConstantesDao;

import javax.sql.DataSource;

@Singleton
public class DBConnectionPool {
    private final ConfigurationServer configurationServer;
    private final DataSource hikariDataSource;

    @Inject
    public DBConnectionPool(ConfigurationServer configurationServer) {
        this.configurationServer = configurationServer;
        hikariDataSource = getDataSourceHikari();
    }

    private DataSource getDataSourceHikari() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(this.configurationServer.getRuta());
        config.setUsername(this.configurationServer.getUser());
        config.setPassword(this.configurationServer.getPassword());
        config.setDriverClassName(this.configurationServer.getDriver());
        config.setMaximumPoolSize(1);

        config.addDataSourceProperty(ConstantesDao.CACHE_PREP_STMTS, ConstantesDao.VALUE_CACHE);
        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SIZE, ConstantesDao.VALUE_PREP_SIZE);
        config.addDataSourceProperty(ConstantesDao.PREP_STMT_CACHE_SQL_LIMIT, ConstantesDao.VALUE_PREP_LIMIT);

        return new HikariDataSource(config);
    }

    public DataSource getHikariDataSource() {
        return hikariDataSource;
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }
}
