package br.com.fiap.minibanco.infra.config;

import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;


@Configuration
public class OracleDataSourceConfig
{

    @Value("${DB_URL}")
    private String dbUrl;

    @Value("${DB_USERNAME}")
    private String username;

    @Value("${DB_PASSWORD}")
    private String password;

    @Bean
    public DataSource dataSource() throws SQLException {
        OracleDataSource oracleDataSource = new OracleDataSource();

        // URL no formato que funciona
        oracleDataSource.setURL(dbUrl);
        oracleDataSource.setUser(username);
        oracleDataSource.setPassword(password);

        // Propriedades adicionais para TLS
        Properties props = new Properties();
        props.setProperty("oracle.net.ssl_server_dn_match", "true");
        props.setProperty("oracle.net.ssl_version", "1.2");
        oracleDataSource.setConnectionProperties(props);

        return oracleDataSource;
    }
}
