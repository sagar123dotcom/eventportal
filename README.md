# Community Event Portal

A Spring Boot 3.3 application built with Java 17, Thymeleaf, Spring Data JPA, and PostgreSQL in production (H2 for local development).

## Features
- Browse and search upcoming events by title, location, or category
- Event details with remaining capacity
- Public registration with validation, capacity checks, and duplicate-email prevention
- Admin login and event management (create, edit, delete, and view registrations)
- Sample events are inserted automatically when the database is empty

## Run locally

Requirements: Java 17+ and Maven.

```bash
mvn spring-boot:run
```

Open http://localhost:8080. The local default admin login is `admin` / `admin123`. Change it with `ADMIN_USERNAME` and `ADMIN_PASSWORD` environment variables before exposing a local instance publicly.

## Deploy to Render (recommended: Blueprint)

1. Push this project folder to a GitHub repository. Include `Dockerfile` and `render.yaml` at the repository root.
2. In Render, choose **New + → Blueprint** and connect that GitHub repository.
3. Review the blueprint. It creates the web service and a PostgreSQL database in Singapore. Both are configured on Render’s free plan to make testing inexpensive. **Important:** the free PostgreSQL database expires 30 days after creation; upgrade it to a paid database before then if you need to keep registrations and events. The free web service can also spin down when idle.
4. Deploy. Render generates a strong `ADMIN_PASSWORD` automatically; open the web service's **Environment** page to retrieve it. The admin username is `admin` by default.
5. Open the web service URL. The health check is `/actuator/health`; the public portal is `/` and admin login is `/admin/login`.

The Render profile uses PostgreSQL and secure, HTTP-only session cookies. Database credentials are injected by Render and are not stored in the repository.

## Deploy manually (alternative)

Create a PostgreSQL database on Render, then create a Docker web service from this repository. Set these environment variables on the web service:

- `SPRING_PROFILES_ACTIVE=render`
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` (use the database's **Internal** connection details)
- `DB_SSLMODE=require`
- `ADMIN_USERNAME` and a strong `ADMIN_PASSWORD`

Render provides `PORT` automatically. The Dockerfile builds and runs the app with Java 17.

## Important notes
- Render's filesystem is ephemeral. The app uses PostgreSQL so events and registrations persist across web-service deploys/restarts. However, Render's free PostgreSQL plan expires after 30 days and has no backups; upgrade it for ongoing use.
- The existing local H2 database file in `data/` is not automatically imported into PostgreSQL. If it contains records you need, export/migrate those records before switching production traffic.
- The sample data loader only adds demo events when the database has no events.
- Hibernate uses `ddl-auto=update` for this small project. For a larger production system, introduce versioned database migrations (for example, Flyway).

## Build a JAR locally

```bash
mvn clean package
java -jar target/community-event-portal-1.0.0.jar
```
"# eventportal" 
