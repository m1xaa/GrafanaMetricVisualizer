package org.example;

import com.grafana.foundation.dashboard.*;
import com.grafana.foundation.prometheus.Dataquery;
import org.example.builders.GrafanaDashboardBuilder;
import org.example.builders.GrafanaPanelBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Dashboard dashboard = new GrafanaDashboardBuilder()
                    .uid("java-cpu-dashboard")
                    .title("CPU Usage Dashboard")
                    .schemaVersion((short) 41)
                    .refresh("5s")
                    .timeRange("now-30m", "now")
                    .tags("java", "prometheus")
                    .addPanel(new GrafanaPanelBuilder()
                            .id(1)
                            .type("timeseries")
                            .title("CPU Usage Over Time")
                            .datasource("prometheus", "DS_PROMETHEUS_UID")
                            .addPrometheusQuery("cpu_usage", "A")
                            .gridPosition(8, 24, 0, 0)
                            .unit("percent")
                            .build())
                    .build();

            try (FileWriter file = new FileWriter("grafana-dashboard.json")) {
                file.write(dashboard.toJSON());
                System.out.println("Successfully wrote JSON to file.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
