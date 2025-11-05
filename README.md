# Grafana Metric Visualizer

This project is a solution to the JetBrains internship test task #1664 - [Monitoring as Code for YouTrack](https://internship.jetbrains.com/projects/1664).

When executed, the application automatically generates a Grafana dashboard in JSON format and saves it in the current working directory.  
Optionally, if configured, it also uploads the generated dashboard directly to a running Grafana instance via the HTTP API.

---

## What This Application Does

The application dynamically builds a **Grafana dashboard** using the **Grafana Foundation SDK for Java**.  
It connects to a local or remote **Prometheus** data source, retrieves the demo metric `cpu_usage`, and produces a structured dashboard definition.

Under the hood:
1. **`DashboardFactory`** builds the dashboard object using the Grafana SDK.  
2. **`DashboardService`** serializes the dashboard into JSON and saves it to disk.  
3. **`GrafanaHttpClient`** sends the JSON payload to the Grafana instance (if enabled).  
4. The process is fully configurable via environment variables (see below).

---

## Prerequisites

Before running the project, make sure you have the following installed:

- [**Java 21**](https://www.oracle.com/java/technologies/downloads/) - required runtime for the application  
- [**IntelliJ IDEA**](https://www.jetbrains.com/idea/download/) - recommended editor for Java development  
- [**Docker Desktop**](https://docs.docker.com/get-started/introduction/get-docker-desktop/) - for running Grafana, Prometheus, and the demo environment

---

## Introduction

This application is designed to monitor and visualize performance metrics from the [Grafana demo Prometheus project](https://github.com/grafana/demo-prometheus-and-grafana-alerts).

Before proceeding, please read the demo repository’s README file.  
Once the demo environment is running via Docker and test data is generated with **k6**, you can start this project.

The primary metric monitored by this application is **`cpu_usage`**, which represents simulated CPU utilization in the demo environment.

---

## Running the Application

### Option 1 - Run directly from IDE

1. **Clone this repository:**
   ```bash
   git clone https://github.com/m1xaa/GrafanaMetricVisualizer.git
   cd grafana-metric-visualizer
   ```
2. **Open the project** in **IntelliJ IDEA**.
3. **Run the `Main` class** (`org.example.Main`) directly from the IDE.

**Expected output:**
- A dashboard JSON file will be generated at:
  ```
  ./dashboards/grafana-dashboard.json
  ```
- If Grafana upload is enabled, the dashboard will be sent automatically to the configured Grafana API endpoint.

---

### Option 2 - Build a runnable JAR

You can build a **fat JAR** using Gradle’s Shadow plugin.

#### Windows
```bash
./gradlew.bat shadowJar
```

#### macOS / Linux
```bash
./gradlew shadowJar
```

After building, the JAR file will be created at:
```
build/libs/grafana-metric-visualizer-1.0-SNAPSHOT.jar
```

Run it with:
```bash
java -jar build/libs/grafana-metric-visualizer-1.0-SNAPSHOT.jar
```

---

## Expected Output

When executed successfully:
- A JSON dashboard file is created at  
  ```
  current_working_directory/dashboards/grafana-dashboard.json
  ```
- Example log output:
  ```
  INFO  Starting Grafana Dashboard generation...
  INFO  Created directory: dashboards
  INFO  Dashboard saved successfully to: dashboards/grafana-dashboard.json
  INFO  Dashboard generation completed successfully!
  ```

If Grafana upload is enabled, you will also see:
  ```
  INFO  Response status code: 200
  ```

---

## Configuration (Environment Variables)

You can configure the application using environment variables by creating a local configuration file named `application-dev.properties` in the directory where `application.properties` is located.  

| Variable | Description | Example |
|-----------|-------------|----------|
| `PROMETHEUS_UID` | UID of your Prometheus data source in Grafana | `DS_PROMETHEUS_UID` |
| `PROMETHEUS_TYPE` | Type of data source (default: `prometheus`) | `prometheus` |
| `DASHBOARD_REFRESH` | Dashboard refresh interval | `5s` |
| `DASHBOARD_SCHEMA_VERSION` | Grafana dashboard schema version | `41` |
| `OUTPUT_DIRECTORY` | Directory where the dashboard JSON will be stored | `dashboards` |
| `OUTPUT_FILE_NAME` | Output JSON filename | `grafana-dashboard.json` |
| `GRAFANA_API_URL` | Grafana API base URL | `http://localhost:3000` |
| `GRAFANA_USERNAME` | Grafana admin username | `admin` |
| `GRAFANA_PASSWORD` | Grafana admin password | `admin` |
| `GRAFANA_ENABLED` | Enable or disable automatic upload to Grafana | `true` / `false` |

Automatic upload to Grafana is not possible when running CI/CD script because Grafana is only hosted locally.

---

## Testing

Before running tests, create an `application-test.properties` file in **`src/test/resources`**.  
The project automatically uses this file when running tests (`app.profile=test`).

Run all tests:
```bash
./gradlew clean test
```

---

## CI/CD Integration

This project includes a simple **GitHub Actions CI/CD pipeline** that:
- Builds the project with Gradle,
- Runs unit and integration tests,
- Produces a fat JAR artifact ready for deployment.
- Runs the JAR artifact.

workflow file is located at: `.github/workflows/deploy.yml`


---

## Summary

- Clone → Build → Run.  
- Generates `grafana-dashboard.json` in `/dashboards/`.  
- Optionally uploads to Grafana (if `GRAFANA_ENABLED=true`).  
- Uses Prometheus metric `cpu_usage` from Grafana’s demo environment.  
- Fully configurable and CI/CD ready.

---
