package org.example.factory.builders;

import com.grafana.foundation.dashboard.Panel;
import com.grafana.foundation.prometheus.Dataquery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrafanaPanelBuilderTest {

    @Test
    void buildPanel() {
        Panel panel = new GrafanaPanelBuilder()
                .id(1)
                .type("timeseries")
                .title("CPU")
                .datasource("prometheus", "uid123")
                .addPrometheusQuery("cpu_usage", "A")
                .gridPosition(8, 24, 0, 0)
                .unit("percent")
                .build();

        assertEquals(1, panel.id);
        assertEquals("timeseries", panel.type);
        assertEquals("CPU", panel.title);
        assertNotNull(panel.datasource);
        assertEquals("prometheus", panel.datasource.type);
        assertEquals(1, panel.targets.size());
        Dataquery query = (Dataquery) panel.targets.getFirst();
        assertEquals("cpu_usage", query.expr);
        assertEquals("A", query.refId);
    }

    @Test
    void addPrometheusQuery_WithoutDatasource() {
        GrafanaPanelBuilder builder = new GrafanaPanelBuilder();
        builder.addPrometheusQuery("cpu_usage", "A");
        assertNull(builder.build().datasource);
    }
}
