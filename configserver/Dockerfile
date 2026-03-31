FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Ensure only 1 jar is copied or use specific name
COPY target/configserver-*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
