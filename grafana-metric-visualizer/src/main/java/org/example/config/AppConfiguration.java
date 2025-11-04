package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfiguration {

    private static final Properties props = new Properties();
    private static final String PROFILE = System.getProperty("app.profile", "dev");

    static {
        loadProperties("application.properties");
        loadProperties("application-" + PROFILE + ".properties");
    }

    private static void loadProperties(String filename) {
        try (InputStream input = AppConfiguration.class
                .getClassLoader()
                .getResourceAsStream(filename)) {
            if (input != null) {
                props.load(input);
                System.out.println("Loaded: " + filename);
            } else {
                System.out.println("Not found: " + filename);
            }
        } catch (IOException e) {
            System.err.println("Failed to load: " + filename);
        }
    }
    public static String getPrometheusUid() {
        return props.getProperty("prometheus.uid", "DS_PROMETHEUS_UID");
    }

    public static String getPrometheusType() {
        return props.getProperty("prometheus.type", "prometheus");
    }

    public static String getOutputPath() {
        return props.getProperty("output.path", "grafana-dashboard.json");
    }

    public static String getDefaultRefresh() {
        return props.getProperty("dashboard.refresh", "5s");
    }

    public static short getSchemaVersion() {
        return Short.parseShort(props.getProperty("dashboard.schema.version", "41"));
    }

    public static String getGrafanaUsername() {
        return props.getProperty("grafana.username", "admin");
    }

    public static String getGrafanaPassword() {
        return props.getProperty("grafana.password", "admin");
    }

    public static String getGrafanaApiUrl() {
        return props.getProperty("grafana.api.url", "admin");
    }
}
