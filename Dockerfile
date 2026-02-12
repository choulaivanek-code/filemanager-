# Image Java 17 légère
FROM eclipse-temurin:17-jdk-alpine

# Dossier de travail
WORKDIR /app

# Copier le jar
COPY target/*.jar app.jar

# Exposer le port Spring
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java","-jar","app.jar"]
