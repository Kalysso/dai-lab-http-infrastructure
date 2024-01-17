# Build stage
FROM openjdk:21-slim
WORKDIR /build
COPY . /build
RUN apt-get update && apt-get install -y maven && mvn package

# Run stage
FROM openjdk:21-slim
WORKDIR /app
COPY --from=0 /build/target/constellation-api-1.0-jar-with-dependencies.jar /app/app.jar
CMD ["java", "-jar", "app.jar", "/config/config.yaml"]