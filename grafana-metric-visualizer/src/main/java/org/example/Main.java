package org.example;

import com.grafana.foundation.dashboard.*;
import com.grafana.foundation.prometheus.Dataquery;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Dashboard dashboard = new DashboardBuilder()
                    .uid("java-cpu-dashboard")
                    .title("CPU Usage Dashboard")
                    .schemaVersion((short) 41)
                    .refresh("5s")
                    .timeRange("now-30m", "now")
                    .tags("java", "prometheus")
                    .addPanel(new PanelBuilder()
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

class DashboardBuilder {
    private final Dashboard dashboard;
    private final List<PanelOrRowPanel> panels;

    public DashboardBuilder() {
        this.dashboard = new Dashboard();
        this.panels = new LinkedList<>();
    }

    public DashboardBuilder uid(String uid) {
        dashboard.uid = uid;
        return this;
    }

    public DashboardBuilder title(String title) {
        dashboard.title = title;
        return this;
    }

    public DashboardBuilder schemaVersion(short version) {
        dashboard.schemaVersion = version;
        return this;
    }

    public DashboardBuilder refresh(String refresh) {
        dashboard.refresh = refresh;
        return this;
    }

    public DashboardBuilder timeRange(String from, String to) {
        dashboard.time = new DashboardDashboardTime();
        dashboard.time.from = from;
        dashboard.time.to = to;
        return this;
    }

    public DashboardBuilder tags(String... tags) {
        dashboard.tags = List.of(tags);
        return this;
    }

    public DashboardBuilder addPanel(Panel panel) {
        panels.add(PanelOrRowPanel.createPanel(panel));
        return this;
    }

    public Dashboard build() {
        if (dashboard.panels == null) {
            dashboard.panels = new LinkedList<>();
        }
        dashboard.panels.addAll(panels);
        return dashboard;
    }
}

class PanelBuilder {
    private final Panel panel;
    private final List<Dataquery> queries;
    private DataSourceRef datasource;

    public PanelBuilder() {
        this.panel = new Panel();
        this.queries = new ArrayList<>();
    }

    public PanelBuilder id(Integer id) {
        panel.id = id;
        return this;
    }

    public PanelBuilder type(String type) {
        panel.type = type;
        return this;
    }

    public PanelBuilder title(String title) {
        panel.title = title;
        return this;
    }

    public PanelBuilder datasource(String type, String uid) {
        this.datasource = new DataSourceRef();
        this.datasource.type = type;
        this.datasource.uid = uid;
        panel.datasource = this.datasource;
        return this;
    }

    public PanelBuilder addPrometheusQuery(String expr, String refId) {
        Dataquery query = new Dataquery();
        query.expr = expr;
        query.refId = refId;
        query.datasource = this.datasource;
        queries.add(query);
        return this;
    }

    public PanelBuilder gridPosition(int h, int w, int x, int y) {
        panel.gridPos = new GridPos(h, w, x, y, false);
        return this;
    }

    public PanelBuilder unit(String unit) {
        if (panel.fieldConfig == null) {
            panel.fieldConfig = new FieldConfigSource();
        }
        if (panel.fieldConfig.defaults == null) {
            panel.fieldConfig.defaults = new FieldConfig();
        }
        panel.fieldConfig.defaults.unit = unit;
        return this;
    }

    public Panel build() {
        panel.targets = new ArrayList<>(queries);
        return panel;
    }
}