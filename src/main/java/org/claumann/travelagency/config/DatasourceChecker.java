package org.claumann.travelagency.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatasourceChecker {

    private final DataSource dataSource;

    @PostConstruct
    public void check() {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Datasource URL: {}", connection.getMetaData().getURL());
            log.info("Database Product: {}", connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            log.error("Erro ao validar datasource", e);
        }
    }
}
