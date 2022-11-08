package it.sevenbits.quizzes.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuration for postgres migrations
 */
@Configuration
public class DatabaseMigrationsConfig {
    private final DataSource dataSource;

    /**
     * config constructor
     * @param dataSource - data source of db
     */
    public DatabaseMigrationsConfig(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * the method for db configuration
     * @return flyway instance
     */
    @Bean
    public Flyway creativeManagementFlyway() {
        Flyway flyway = Flyway
                .configure()
                .dataSource(this.dataSource)
                .table("flyway_schema")
                .baselineOnMigrate(true)
                .locations("db/migrations")
                .load();

        try {
            flyway.migrate();
        } catch (FlywayException e) {
            flyway.repair();
            flyway.migrate();
        }

        return flyway;
    }
}
