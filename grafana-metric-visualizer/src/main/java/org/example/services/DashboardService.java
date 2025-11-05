package org.example.services;

import com.grafana.foundation.dashboard.Dashboard;
import org.example.config.AppConfiguration;
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

    public void saveDashboard(Dashboard dashboard) {
        String jarDir = System.getProperty("user.dir");
        String directory = Paths.get(jarDir, AppConfiguration.getOutputDirectory()).toString();
        String outputPath = Paths.get(directory, AppConfiguration.getOutputFileName()).toString();

        ensureDirectoryExists(directory);
        writeDashboardToFile(dashboard, outputPath);
    }

    private void ensureDirectoryExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: {}", path);
            }
        } catch (IOException e) {
            throw new GrafanaException("Failed to create output directory: " + directoryPath);
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
