package org.example.network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GrafanaHttpClient {
    static String grafanaUrl = "http://localhost:3000";
    static String username = "admin";
    static String password = "admin";

    public static void sendDashboard(String dashboardJson) throws IOException, InterruptedException {
        String auth = username + ":" + password;
        String basicAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String payload = "{ \"dashboard\": " + dashboardJson + ", \"folderId\": 0, \"overwrite\": true }";

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(grafanaUrl + "/api/dashboards/db"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + basicAuth)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
    }
}
