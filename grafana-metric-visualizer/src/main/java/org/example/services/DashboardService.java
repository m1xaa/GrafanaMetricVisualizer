package org.example.services;

import com.grafana.foundation.dashboard.Dashboard;
import org.example.exceptions.GrafanaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    public void saveDashboard(Dashboard dashboard, String outputPath) {
        ensureDirectoryExists(outputPath);
        writeDashboardToFile(dashboard, outputPath);
    }


    private void ensureDirectoryExists(String filePath) {
        try {
            Path path = Paths.get(filePath).getParent();
            if (path != null && !Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: {}", path);
            }
        } catch (IOException e) {
            throw new GrafanaException("Failed to create output directory");
        }
    }

    private void writeDashboardToFile(Dashboard dashboard, String outputPath) {
        try (FileWriter file = new FileWriter(outputPath)) {
            String json = dashboard.toJSON();
            file.write(json);
            logger.info("Dashboard saved successfully to: {}", outputPath);
        } catch (IOException e) {
            throw new GrafanaException("Failed to write dashboard to file: " + outputPath);
        }
    }
}