package org.example.factory;

import com.grafana.foundation.dashboard.Dashboard;
import org.example.config.AppConfiguration;
import org.example.factory.builders.GrafanaDashboardBuilder;
import org.example.factory.builders.GrafanaPanelBuilder;

public class DashboardFactory {

    public static Dashboard createCpuDashboard() {
        return new GrafanaDashboardBuilder()
                .uid("java-cpu-dashboard")
                .title("CPU Usage Dashboard")
                .schemaVersion(AppConfiguration.getSchemaVersion())
                .refresh(AppConfiguration.getDefaultRefresh())
                .timeRange("now-30m", "now")
                .tags("java", "prometheus", "monitoring")
                .addPanel(createCpuPanel().build())
                .build();
    }

    private static GrafanaPanelBuilder createCpuPanel() {
        return new GrafanaPanelBuilder()
                .id(1)
                .type("timeseries")
                .title("CPU Usage Over Time")
                .datasource(
                        AppConfiguration.getPrometheusType(),
                        AppConfiguration.getPrometheusUid()
                )
                .addPrometheusQuery("cpu_usage", "A")
                .gridPosition(8, 24, 0, 0)
                .unit("percent");
    }
}
