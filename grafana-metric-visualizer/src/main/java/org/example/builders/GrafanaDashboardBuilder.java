package org.example.builders;

import com.grafana.foundation.dashboard.Dashboard;
import com.grafana.foundation.dashboard.DashboardDashboardTime;
import com.grafana.foundation.dashboard.Panel;
import com.grafana.foundation.dashboard.PanelOrRowPanel;

import java.util.LinkedList;
import java.util.List;

public class GrafanaDashboardBuilder {
    private final Dashboard dashboard;
    private final List<PanelOrRowPanel> panels;

    public GrafanaDashboardBuilder() {
        this.dashboard = new Dashboard();
        this.panels = new LinkedList<>();
    }

    public GrafanaDashboardBuilder uid(String uid) {
        dashboard.uid = uid;
        return this;
    }

    public GrafanaDashboardBuilder title(String title) {
        dashboard.title = title;
        return this;
    }

    public GrafanaDashboardBuilder schemaVersion(short version) {
        dashboard.schemaVersion = version;
        return this;
    }

    public GrafanaDashboardBuilder refresh(String refresh) {
        dashboard.refresh = refresh;
        return this;
    }

    public GrafanaDashboardBuilder timeRange(String from, String to) {
        dashboard.time = new DashboardDashboardTime();
        dashboard.time.from = from;
        dashboard.time.to = to;
        return this;
    }

    public GrafanaDashboardBuilder tags(String... tags) {
        dashboard.tags = List.of(tags);
        return this;
    }

    public GrafanaDashboardBuilder addPanel(Panel panel) {
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