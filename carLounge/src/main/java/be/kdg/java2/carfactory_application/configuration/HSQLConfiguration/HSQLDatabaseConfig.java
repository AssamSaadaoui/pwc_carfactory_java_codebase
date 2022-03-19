package be.kdg.java2.carfactory_application.configuration.HSQLConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class HSQLDatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(HSQLDatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName("org.hsqldb.jdbcDriver")
                .url("jdbc:hsqldb:file:dbData/carFactory")
                .username("sa")
                .password("")
                .build();
        log.debug("HSQL database configured");
        return dataSource;
    }
}
