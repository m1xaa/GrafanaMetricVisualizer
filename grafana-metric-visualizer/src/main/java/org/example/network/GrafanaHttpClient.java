package org.example.network;

import org.example.config.AppConfiguration;
import org.example.services.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GrafanaHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(GrafanaHttpClient.class);

    public static void sendDashboard(String dashboardJson) throws IOException, InterruptedException {
        String auth = AppConfiguration.getGrafanaUsername() + ":" + AppConfiguration.getGrafanaPassword();
        String basicAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String payload = "{ \"dashboard\": " + dashboardJson + ", \"folderId\": 0, \"overwrite\": true }";

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AppConfiguration.getGrafanaApiUrl() + "/api/dashboards/db"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + basicAuth)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Response status code: {}", response.statusCode());
        }
    }
}
