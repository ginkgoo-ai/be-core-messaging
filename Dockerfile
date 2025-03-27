FROM maven:3.9-amazoncorretto-23 AS builder
ARG GITHUB_USER
ARG GITHUB_TOKEN

WORKDIR /app
COPY pom.xml ./
COPY settings.xml ./
COPY be-core-messaging-app ./be-core-messaging-app
COPY be-core-messaging-sdk ./be-core-messaging-sdk

RUN mvn clean install -X -U -s settings.xml && \
    mvn package -pl be-core-messaging-app -Dmaven.test.skip=true -s settings.xml

FROM openjdk:23-jdk-slim
WORKDIR /app

COPY --from=builder /app/be-core-messaging-app/target/*.jar app.jar
COPY integration/grafana-opentelemetry-java-v2.12.0.jar ./grafana-opentelemetry-java-v2.12.0.jar

CMD ["java", "-Xms128m", "-Xmx1024m", "-javaagent:grafana-opentelemetry-java-v2.12.0.jar", "-jar", "app.jar"]
