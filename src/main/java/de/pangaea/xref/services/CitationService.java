package de.pangaea.xref.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class CitationService {

    private static final Logger logger = LoggerFactory.getLogger(CitationService.class);
    private final HttpClient httpClient;

    public CitationService() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String getCitation(String panURL) {
        logger.debug("Fetching citation from: {}", panURL);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(panURL))
                    .timeout(Duration.ofMinutes(1))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() == 200) {
                String citationString = response.body();
                return citationString.replace('\t', ' ');
            } else {
                logger.warn("Failed to fetch citation from {}. Status code: {}", panURL, response.statusCode());
                return "Citation not available";
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error fetching citation from {}", panURL, e);
            Thread.currentThread().interrupt(); // Restore interrupted status
            return "Citation not available";
        }
    }
}