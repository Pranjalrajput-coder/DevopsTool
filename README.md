# DevopsTool

**DevopsTool** is an AI-powered log analysis service built with Spring Boot and Spring AI. It exposes a simple REST endpoint that takes a raw application/error log and returns an AI-generated diagnosis — the **Issue**, an **Explanation**, and a suggested **Fix** — using a local LLM (via Ollama) enriched with context from previously analyzed logs (Retrieval-Augmented Generation).

Every log you submit is embedded, stored in a Postgres vector database, and used as context for future analyses — so the more logs the tool sees, the better its suggestions get.

## Features

- 🔍 **AI log analysis** — `POST` a log and get back a structured Issue / Explanation / Fix breakdown from an LLM.
- 🧠 **RAG-based context** — Past logs are embedded and stored in **pgvector**; similar historical logs are retrieved and fed back into the prompt so the model can reason with prior context.
- 🗄️ **Persistent log history** — Every analyzed log, its embedding, and the AI's suggestion are saved to Postgres via Spring Data JPA.
- 🧩 **Pluggable local models** — Runs against a local [Ollama](https://ollama.com/) instance (default chat model: `qwen3:0.6b`, embedding model: `mxbai-embed-large`), so no external API key is required.
- 🐳 **Container-ready** — Ships with a `Dockerfile` for building a lightweight runtime image.
- 📨 **Kafka scaffolding (optional)** — Kafka producer/consumer/topic wiring is included in the codebase (currently commented out) for evolving this into an async, event-driven pipeline.

## Tech Stack

| Layer            | Technology                                      |
|-------------------|--------------------------------------------------|
| Language / Runtime | Java 17                                          |
| Framework          | Spring Boot 4.0.3                                |
| AI Orchestration   | Spring AI 2.0.0 (Ollama chat + embedding models) |
| Vector Store       | pgvector (PostgreSQL extension)                  |
| Persistence        | Spring Data JPA / Hibernate                      |
| Database           | PostgreSQL                                       |
| Messaging (optional) | Apache Kafka / Spring Kafka                   |
| Build Tool         | Maven                                            |
| Containerization   | Docker (`eclipse-temurin:17-jdk-jammy`)          |

## How It Works

1. A client sends a raw log line/message to `POST /api/logs/analyze`.
2. The log is embedded using the configured Ollama embedding model.
3. The vector store is searched for similar past logs (top 3, similarity threshold `0.2`) to use as context.
4. A prompt is built instructing the model to act as a senior DevOps engineer and return the **Issue**, **Explanation**, and **Fix** for the log, using the retrieved context.
5. The chat model generates a response, which is returned to the caller.
6. The original log, its embedding, and the AI's suggestion are persisted to Postgres for future retrieval.

## Prerequisites

Before running the project, make sure you have:

- **Java 17+**
- **Maven** (or use the included Maven wrapper, if present)
- **PostgreSQL** with the [`pgvector`](https://github.com/pgvector/pgvector) extension enabled
- **[Ollama](https://ollama.com/)** running locally (or reachable) with the required models pulled:
  ```bash
  ollama pull qwen3:0.6b
  ollama pull mxbai-embed-large
  ```
- *(Optional)* **Apache Kafka**, if you plan to enable the messaging-based flow

## Configuration

The app is configured via `src/main/resources/application.yaml` and reads the following environment variables at startup:

| Variable            | Description                              | Default             |
|---------------------|-------------------------------------------|----------------------|
| `AI_DOCKER_HOST`     | Host where Ollama is running              | `localhost`          |
| `DB_HOST`            | Postgres/pgvector host                    | `localhost`          |
| `DB_PORT`            | Postgres/pgvector port                    | `5433`               |
| `POSTGRES_DB`        | Postgres database name                    | *(required)*         |
| `POSTGRES_USER`      | Postgres username                         | *(required)*         |
| `POSTGRES_PASSWORD`  | Postgres password                         | *(required)*         |
| `KAFKA_HOST`         | Kafka bootstrap server                    | `localhost:9092`     |

For local overrides, create an `application-local.yml` / `application-local.properties` (already ignored by git) rather than editing `application.yaml` directly.

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Pranjalrajput-coder/DevopsTool.git
   cd DevopsTool
   ```

2. **Start dependencies** (Postgres with pgvector, and Ollama). Example using Docker:
   ```bash
   docker run -d --name pgvector -e POSTGRES_DB=devopstool \
     -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
     -p 5433:5432 pgvector/pgvector:pg16

   ollama serve
   ```

3. **Set required environment variables**
   ```bash
   export POSTGRES_DB=devopstool
   export POSTGRES_USER=postgres
   export POSTGRES_PASSWORD=postgres
   ```

4. **Build and run**
   ```bash
   ./mvnw spring-boot:run
   ```
   The app starts on `http://localhost:8080` by default.

### Running with Docker

```bash
./mvnw clean package -DskipTests
docker build -t devopstool .
docker run -p 8080:8080 \
  -e DB_HOST=<postgres-host> \
  -e POSTGRES_DB=devopstool \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e AI_DOCKER_HOST=<ollama-host> \
  devopstool
```

## API Usage

### Analyze a log

```
POST /api/logs/analyze
Content-Type: text/plain
```

**Request body:** the raw log line/message to analyze.

```bash
curl -X POST http://localhost:8080/api/logs/analyze \
  -H "Content-Type: text/plain" \
  -d "java.lang.NullPointerException at com.example.service.OrderService.process(OrderService.java:42)"
```

**Response:** an AI-generated breakdown containing the Issue, Explanation, and Fix for the submitted log.

## Project Structure

```
DevopsTool/
├── src/
│   ├── main/
│   │   ├── java/com/example/logs/ai/DevopsTool/
│   │   │   ├── config/            # AI + Kafka configuration
│   │   │   ├── controller/        # REST controllers (LogController)
│   │   │   ├── entity/            # JPA entities (LogEntity)
│   │   │   ├── repository/        # Spring Data repositories
│   │   │   ├── service/           # Business logic (LogService, LogProducer, LogConsumer)
│   │   │   └── LogsCheckMainApplication.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── application-kafbat.yml
│   └── test/                      # Unit tests
├── Dockerfile
└── pom.xml
```

## Roadmap

- [ ] Re-enable the Kafka producer/consumer pipeline for asynchronous log ingestion
- [ ] Add authentication/authorization to the API
- [ ] Support batch log analysis
- [ ] Expose analysis history via a query endpoint

## Contributing

Contributions, issues, and feature requests are welcome. Feel free to open a PR or file an issue.

## License

No license has been specified for this project yet.
