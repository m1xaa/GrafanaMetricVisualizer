package org.example.factory.builders;

import com.grafana.foundation.dashboard.*;
import com.grafana.foundation.prometheus.Dataquery;

import java.util.ArrayList;
import java.util.List;

public class GrafanaPanelBuilder {
    private final Panel panel;
    private final List<Dataquery> queries;
    private DataSourceRef datasource;

    public GrafanaPanelBuilder() {
        this.panel = new Panel();
        this.queries = new ArrayList<>();
    }

    public GrafanaPanelBuilder id(Integer id) {
        panel.id = id;
        return this;
    }

    public GrafanaPanelBuilder type(String type) {
        panel.type = type;
        return this;
    }

    public GrafanaPanelBuilder title(String title) {
        panel.title = title;
        return this;
    }

    public GrafanaPanelBuilder datasource(String type, String uid) {
        this.datasource = new DataSourceRef();
        this.datasource.type = type;
        this.datasource.uid = uid;
        panel.datasource = this.datasource;
        return this;
    }

    public GrafanaPanelBuilder addPrometheusQuery(String expr, String refId) {
        Dataquery query = new Dataquery();
        query.expr = expr;
        query.refId = refId;
        query.datasource = this.datasource;
        queries.add(query);
        return this;
    }

    public GrafanaPanelBuilder gridPosition(int h, int w, int x, int y) {
        panel.gridPos = new GridPos(h, w, x, y, false);
        return this;
    }

    public GrafanaPanelBuilder unit(String unit) {
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