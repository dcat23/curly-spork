FROM eclipse-temurin:21.0.2_13-jre-alpine
LABEL authors="sandman"
ARG TRANSMISSION_VERSION="4.0.5-r0"
RUN apk add --no-cache  \
    transmission-cli==${TRANSMISSION_VERSION} \
    transmission-daemon==${TRANSMISSION_VERSION} \
    transmission-extra==${TRANSMISSION_VERSION} \
    transmission-remote==${TRANSMISSION_VERSION} && \
    mkdir -p /trans/config && \
    chmod -R 1777 /trans && \
    rm -rf  \
    /tmp/* \
    $HOME/.cache

EXPOSE 9091 51413/tcp 51413/udp


