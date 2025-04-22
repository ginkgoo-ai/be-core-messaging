FROM maven:3.9-amazoncorretto-23 AS builder
ARG GITHUB_USER
ARG GITHUB_TOKEN

WORKDIR /app
COPY pom.xml ./
COPY settings.xml ./
COPY be-core-messaging-app ./be-core-messaging-app
COPY be-core-messaging-sdk ./be-core-messaging-sdk

RUN mvn clean install -U -s settings.xml && \
    mvn package -pl be-core-messaging-app -Dmaven.test.skip=true -s settings.xml

FROM openjdk:23-jdk-slim
ARG GRAFANA_OTEL_VERSION=v2.15.0
ENV GRAFANA_OTEL_JAR=grafana-opentelemetry-java-${GRAFANA_OTEL_VERSION}.jar

WORKDIR /app

RUN apt-get update && \
    apt-get install -y curl && \
    curl -s -L https://github.com/grafana/grafana-opentelemetry-java/releases/download/${GRAFANA_OTEL_VERSION}/grafana-opentelemetry-java.jar \
     -o ${GRAFANA_OTEL_JAR} && \
    apt-get purge -y --auto-remove curl && \
    rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/be-core-messaging-app/target/*.jar app.jar

CMD ["sh", "-c", "java -Xms128m -Xmx1024m -javaagent:${GRAFANA_OTEL_JAR} -jar app.jar"]
