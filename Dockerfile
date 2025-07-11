# Usa una imagen base liviana con JDK 21
FROM eclipse-temurin:21-jdk-alpine

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR generado por Maven (asegúrate de que el nombre sea correcto)
COPY target/accentureBackendTest-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Configura variables de entorno opcionales (como el perfil)
ENV SPRING_PROFILES_ACTIVE=default

# Comando de inicio de la aplicación
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
