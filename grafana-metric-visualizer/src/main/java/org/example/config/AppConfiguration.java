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

    private static String resolveEnv(String value, String defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        if (value.startsWith("${") && value.endsWith("}")) {
            String envName = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envName);
            return envValue != null ? envValue : defaultValue;
        }

        return value;
    }

    public static String getPrometheusUid() {
        return resolveEnv(props.getProperty("prometheus.uid"), "DS_PROMETHEUS_UID");
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
        return resolveEnv(props.getProperty("grafana.username"), "admin");
    }

    public static String getGrafanaPassword() {
        return resolveEnv(props.getProperty("grafana.password"), "admin");
    }

    public static String getGrafanaApiUrl() {
        return resolveEnv(props.getProperty("grafana.api.url"), "http://NIFAOt:3000");
    }

    public static boolean isGrafanaEnabled() {
        return Boolean.parseBoolean(resolveEnv(props.getProperty("grafana.enabled"), "true"));
    }
}
