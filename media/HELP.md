# Media

## Building the base image

```shell
docker buildx build --platform=linux/amd64,linux/arm64 \
  -t macchiato23/21-jre-alpine:ytdlp --push \
  -f Dockerfile .
```