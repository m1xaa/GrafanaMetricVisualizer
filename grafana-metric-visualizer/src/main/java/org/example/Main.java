package org.example;

import com.grafana.foundation.dashboard.Dashboard;
import org.example.config.AppConfiguration;
import org.example.exceptions.GrafanaException;
import org.example.factory.DashboardFactory;
import org.example.network.GrafanaHttpClient;
import org.example.services.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting Grafana Dashboard generation...");

            Dashboard dashboard = DashboardFactory.createCpuDashboard();

            DashboardService dashboardService = new DashboardService();
            dashboardService.saveDashboard(dashboard, AppConfiguration.getOutputPath());

            if (AppConfiguration.isGrafanaEnabled()) {
                logger.info("grafana enabled: {}", AppConfiguration.isGrafanaEnabled());
                logger.info("grafana url: {}", AppConfiguration.getGrafanaApiUrl());
                GrafanaHttpClient.sendDashboard(dashboard.toJSON());
            } else {
                logger.info("Skipping Grafana upload (GRAFANA_ENABLED=false)");
            }

            logger.info("Dashboard generation completed successfully!");

        } catch (GrafanaException e) {
            logger.error("Grafana error: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred", e);
        }
    }
}