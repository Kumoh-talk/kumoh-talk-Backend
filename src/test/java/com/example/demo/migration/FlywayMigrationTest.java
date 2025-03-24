package com.example.demo.migration;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class FlywayMigrationTest {

    @Test
    void testMigration() {
        MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                .withUsername("test")
                .withPassword("test")
                .withDatabaseName("testdb");
        mysqlContainer.start();

        Flyway flyway = Flyway.configure()
                .dataSource(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(), mysqlContainer.getPassword())
                .locations("classpath:db/migration")
                .load();
        
        System.out.println("Number of migrated files: " + flyway.info().all().length);
        assertDoesNotThrow(flyway::migrate, "Migration should not throw an exception");
    }

    @Test
    void testV20250212145216() throws Exception {
        FileReader reader = new FileReader("src/main/resources/db/migration/V20250314231509__init.sql");

        Statement statement = CCJSqlParserUtil.parse(reader);
        Assertions.assertNotNull(statement);
    }
}
