package org.example.factory.builders;

import com.grafana.foundation.dashboard.Dashboard;
import com.grafana.foundation.dashboard.Panel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafanaDashboardBuilderTest {

    @Test
    void buildDashboard() {
        Panel panel = new Panel();
        Dashboard dashboard = new GrafanaDashboardBuilder()
                .uid("uid-1")
                .title("Title")
                .schemaVersion((short) 41)
                .refresh("5s")
                .timeRange("now-15m", "now")
                .tags("a", "b")
                .addPanel(panel)
                .build();

        assertEquals("uid-1", dashboard.uid);
        assertEquals("Title", dashboard.title);
        assertEquals("5s", dashboard.refresh);
        assertEquals("now", dashboard.time.to);
        assertEquals(1, dashboard.panels.size());
    }

    @Test
    void buildDashboard_NoPanels() {
        Dashboard dashboard = new GrafanaDashboardBuilder()
                .uid("x")
                .title("y")
                .build();

        assertNotNull(dashboard.panels);
        assertTrue(dashboard.panels.isEmpty());
    }
}
