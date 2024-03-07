FROM eclipse-temurin:21.0.2_13-jre-alpine
LABEL authors="sandman"

WORKDIR /app

RUN apk add --no-cache transmission-cli

RUN mkdir -p /app/data
VOLUME /app/data

ENTRYPOINT ["transmission-cli", "-w", "/app/data"]


