# ---------- Build stage ----------
FROM eclipse-temurin:21 as build
WORKDIR /workspace

# Copy only build descriptors first to cache deps
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

RUN chmod +x mvnw

# Pre-fetch dependencies (fast rebuilds)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Now copy source (this invalidates cache when code changes)
COPY src ./src

# Build the jar
RUN ./mvnw -q -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the Spring Boot runnable jar (exclude original-*.jar)
COPY --from=build /workspace/target/*.jar /app/
RUN rm -f /app/original-*.jar && mv /app/*.jar /app/app.jar
#
#HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
#    CMD curl -f http://localhost:8082/actuator/health || exit 1

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
