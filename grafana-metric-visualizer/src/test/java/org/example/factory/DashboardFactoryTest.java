package org.example.factory;

import com.grafana.foundation.dashboard.Dashboard;
import com.grafana.relocated.jackson.core.JsonProcessingException;
import org.example.config.AppConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DashboardFactoryTest {

    @Test
    void createCpuDashboard() {
        Dashboard dashboard = DashboardFactory.createCpuDashboard();

        assertNotNull(dashboard);
        assertEquals("java-cpu-dashboard", dashboard.uid);
        assertEquals("CPU Usage Dashboard", dashboard.title);
        assertTrue(dashboard.tags.contains("java"));
        assertNotNull(dashboard.panels);
        assertFalse(dashboard.panels.isEmpty());
    }

    @Test
    void createCpuDashboard_ContainsPrometheusPanel() throws JsonProcessingException {
        Dashboard dashboard = DashboardFactory.createCpuDashboard();
        var panelWrapper = dashboard.panels.getFirst();
        String panelJson = panelWrapper.toJSON();
        System.out.println(panelJson);

        assertTrue(panelJson.contains("\"type\" : \"timeseries\""), "Panel type not found in JSON");
        assertTrue(panelJson.contains("\"title\" : \"CPU Usage Over Time\""), "Panel title not found in JSON");
        assertTrue(panelJson.contains(AppConfiguration.getPrometheusUid()), "Prometheus UID missing in JSON");
    }

}
