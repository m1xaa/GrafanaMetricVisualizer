package org.example.services;

import com.grafana.foundation.dashboard.Dashboard;
import org.example.config.AppConfiguration;
import org.example.exceptions.GrafanaException;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class DashboardServiceTest {

    private DashboardService service;

    @BeforeEach
    void setup() {
        service = new DashboardService();
    }

    @Test
    void saveDashboard() throws Exception {
        Dashboard dashboard = new Dashboard();
        dashboard.uid = "uid";
        dashboard.title = "Title";
        dashboard.refresh = "5s";

        service.saveDashboard(dashboard);

        String filePath = Paths.get(System.getProperty("user.dir"),
                AppConfiguration.getOutputDirectory(), AppConfiguration.getOutputFileName()).toString();

        File file = new File(filePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        Files.deleteIfExists(file.toPath());
    }

    @Test
    void ensureDirectoryExists_WhenCreateFails_ThrowsGrafanaException() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.exists(any(Path.class))).thenReturn(false);
            filesMock.when(() -> Files.createDirectories(any(Path.class)))
                    .thenThrow(new IOException("Permission denied"));

            assertThrows(GrafanaException.class, () -> {
                service.saveDashboard(new Dashboard());
            });
        }
    }
}
