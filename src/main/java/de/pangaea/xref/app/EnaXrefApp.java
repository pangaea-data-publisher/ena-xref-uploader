package de.pangaea.xref.app;

import de.pangaea.xref.config.AppConfig;
import de.pangaea.xref.datasource.DatabaseManager;
import de.pangaea.xref.model.EnaXref;
import de.pangaea.xref.repository.EnaXrefRepository;
import de.pangaea.xref.services.CitationService;
import de.pangaea.xref.services.FtpService;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EnaXrefApp {

    private static final Logger logger = LoggerFactory.getLogger(EnaXrefApp.class);

    public static void main(String[] args) {
        logger.info("Starting ENA xref service...");

        AppConfig config = new AppConfig();
        DatabaseManager databaseManager = new DatabaseManager(config);

        Jdbi jdbi = databaseManager.getJdbi();
        if (!isDatabaseReady(jdbi, config)) {
            logger.error("DB access currently impossible - exiting.");
            System.exit(1);
        }

        // Create repository and services
        EnaXrefRepository repository = jdbi.onDemand(EnaXrefRepository.class);
        CitationService citationService = new CitationService();
        FtpService ftpService = new FtpService(config);

        // 1. Fetch data from database
        List<EnaXref> xrefsWithoutCitations = repository.findAllEnaXrefs();

        // 2. Fetch citations for each record
        List<EnaXref> completeXrefs = xrefsWithoutCitations.stream()
                .map(xref -> {
                    String citation = citationService.getCitation(xref.citationUrl());
                    return xref.withCitation(citation);
                })
                .collect(Collectors.toList());

        // 3. Upload results
        if (!completeXrefs.isEmpty()) {
            ftpService.upload(completeXrefs);
        } else {
            logger.info("No cross-references found.");
        }

        logger.info("ENA xref service finished.");
    }

    private static boolean isDatabaseReady(Jdbi jdbi, AppConfig config) {
        int maxRetries = config.getDbConnectionRetries();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                jdbi.useHandle(handle -> { /* Do nothing, just check connection */ });
                logger.info("Database connection successful.");
                return true;
            } catch (JdbiException e) {
                logger.error("Error establishing DB connection (attempt {} of {}): {}", attempt, maxRetries, e.getMessage());
                if (attempt < maxRetries) {
                    try {
                        long delay = config.getDbConnectionRetryDelay();
                        logger.info("Retrying in {} ms...", delay);
                        TimeUnit.MILLISECONDS.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("Retry delay was interrupted", ie);
                        return false;
                    }
                }
            }
        }
        return false;
    }
}