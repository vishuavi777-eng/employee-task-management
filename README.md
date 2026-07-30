# Employee Task Microservices

A production-style microservices learning project built incrementally 
using
Spring Boot and MySQL.

## Services

- Employee Service
- Task Service
- Notification Service
- Report Service

## Initial Communication

- Task Service validates employees through synchronous REST.
- Task Service sends notifications through synchronous REST.
- Reporting initially uses REST-based data retrieval.

## Planned Evolution

- API Gateway
- Eureka Service Discovery
- Resilience patterns
- Redis caching
- Kafka or RabbitMQ
- Distributed tracing
- Centralized configuration

## Service Ports

| Service | Port |
|---|---:|
| Employee Service | 8081 |
| Task Service | 8082 |
| Notification Service | 8083 |
| Report Service | 8084 |

## Architecture Rules

- Each service owns its database.
- Services never directly access another service's database.
- JPA entities are not shared between services.
- Service URLs remain configurable.
- Infrastructure components are introduced only after basic communication 
works.

# Monitoring, Logging and Distributed Tracing

This project uses the following observability tools:

| Tool          | Purpose                              |    Port | URL                      |
| ------------- | ------------------------------------ | ------: | ------------------------ |
| Prometheus    | Collects application metrics         |  `9090` | `http://localhost:9090`  |
| Zipkin        | Displays distributed traces          |  `9411` | `http://localhost:9411`  |
| Grafana       | Visualizes metrics and logs          |  `3000` | `http://localhost:3000`  |
| Loki          | Stores and queries application logs  |  `3100` | `http://localhost:3100`  |
| Grafana Alloy | Collects logs and sends them to Loki | `12345` | `http://localhost:12345` |

---

## 1. Start Prometheus

Prometheus is installed manually in the following directory:

```bash
cd ~/prometheus/prometheus-3.13.0.darwin-amd64
```

Start Prometheus:

```bash
./prometheus --config.file=prometheus.yml
```

Open Prometheus:

```text
http://localhost:9090
```

Check configured targets:

```text
http://localhost:9090/targets
```

Check Prometheus readiness:

```bash
curl http://localhost:9090/-/ready
```

Stop Prometheus by pressing:

```text
Control + C
```

---

## 2. Start Zipkin

Zipkin is available as an executable JAR file.

```bash
cd ~/zipkin
```

Start Zipkin:

```bash
java -jar zipkin.jar
```

Open Zipkin:

```text
http://localhost:9411
```

Stop Zipkin by pressing:

```text
Control + C
```

---

## 3. Start Grafana

Grafana is installed manually in the following directory:

```bash
cd ~/grafana/grafana-v12.1.0
```

Start Grafana:

```bash
./bin/grafana server
```

Open Grafana:

```text
http://localhost:3000
```

Default login, when not changed:

```text
Username: admin
Password: admin
```

Stop Grafana by pressing:

```text
Control + C
```

---

## 4. Start Loki

Loki is installed manually in the following directory:

```bash
cd ~/loki
```

Start Loki using its configuration file:

```bash
./loki --config.file=config.yml
```

Loki server URL:

```text
http://localhost:3100
```

Check whether Loki is ready:

```bash
curl http://localhost:3100/ready
```

Expected response:

```text
ready
```

Loki does not provide a normal dashboard. Logs stored in Loki are viewed through Grafana.

Stop Loki by pressing:

```text
Control + C
```

---

## 5. Start Grafana Alloy

Grafana Alloy is installed and managed using Homebrew Services.

Start Alloy:

```bash
brew services start grafana/grafana/alloy
```

Check Alloy service status:

```bash
brew services info grafana/grafana/alloy
```

Open the Alloy debugging UI:

```text
http://localhost:12345
```

Check Alloy metrics:

```text
http://localhost:12345/metrics
```

Restart Alloy:

```bash
brew services restart grafana/grafana/alloy
```

Stop Alloy:

```bash
brew services stop grafana/grafana/alloy
```

Alloy runs in the background, so its terminal does not need to remain open.

---

## Recommended Startup Order

Start the tools in the following order:

```text
1. Zipkin
2. Prometheus
3. Loki
4. Grafana Alloy
5. Grafana
6. Eureka Server
7. Employee Service
8. Task Service
9. API Gateway
```

Loki should start before Alloy because Alloy forwards application logs to Loki.

---

## Quick Start Commands

Open separate Terminal tabs and execute the following commands.

### Terminal 1 — Zipkin

```bash
cd ~/zipkin
java -jar zipkin.jar
```

### Terminal 2 — Prometheus

```bash
cd ~/prometheus/prometheus-3.13.0.darwin-amd64
./prometheus --config.file=prometheus.yml
```

### Terminal 3 — Loki

```bash
cd ~/loki
./loki --config.file=config.yml
```

### Terminal 4 — Grafana

```bash
cd ~/grafana/grafana-v12.1.0
./bin/grafana server
```

### Start Alloy in the Background

```bash
brew services start grafana/grafana/alloy
```

---

## Check Whether Ports Are Running

```bash
lsof -i :9090
lsof -i :9411
lsof -i :3000
lsof -i :3100
lsof -i :12345
```

Check all Homebrew services:

```bash
brew services list
```

---

## Grafana Data Sources

Configure these data sources inside Grafana.

### Prometheus

```text
Name: Prometheus
URL: http://localhost:9090
```

### Loki

```text
Name: Loki
URL: http://localhost:3100
```

After adding each data source, click **Save & Test**.

---

## Important Notes

* Prometheus, Zipkin, Grafana and Loki run manually, so their Terminal windows must remain open.
* Alloy runs as a Homebrew background service.
* Start Loki before Alloy.
* Start Zipkin before running Spring Boot services when testing distributed tracing.
* Start Prometheus before checking application metrics.
* Start the Spring Boot services before checking Prometheus targets.
* Application metrics must be available through the `/actuator/prometheus` endpoint.
* Application logs can be searched in Grafana using the Loki data source.


## Start All Observability Tools

Instead of starting each tool manually, you can use the startup script to launch all observability components in the correct order.

The script starts the following tools sequentially:

1. Zipkin
2. Prometheus
3. Loki
4. Grafana Alloy
5. Grafana

It also performs the following tasks:

* Checks whether a tool is already running before starting it.
* Waits until each service is ready before starting the next one.
* Stores the output logs of each tool in the `~/observability-logs` directory.
* Displays the URLs of all running services after startup.

### Start All Tools

```bash
~/start-observability.sh
```

or

```bash
bash ~/start-observability.sh
```

### Stop All Tools

```bash
~/stop-observability.sh
```

### Log Files

The startup script stores logs for each tool in:

```text
~/observability-logs/
```

Example log files:

```text
prometheus.log
zipkin.log
grafana.log
loki.log
```

This is the recommended way to start the local observability stack during development, as it ensures all services are started in the correct order with a single command.

Webhook test
