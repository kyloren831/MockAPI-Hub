# ===== BUILD =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Primero dependencias (mejor cache)
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Luego el código y compila
COPY src ./src
RUN mvn -q -DskipTests package

# ===== RUNTIME =====
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/apisandbox-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
