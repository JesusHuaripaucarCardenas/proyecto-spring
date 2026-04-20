# ================================
# ETAPA 1: BUILD con Maven + Alpine
# ================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ================================
# ETAPA 2: RUNTIME con JRE Alpine
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/ms-spring-webflux-nosql-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8082/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]