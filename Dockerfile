# Etapa 1: Construcción con Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /accenture_backend_test
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Verificamos qué archivos se generaron
RUN ls -la /accenture_backend_test/target/

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copiamos cualquier archivo JAR
COPY --from=build /accenture_backend_test/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]