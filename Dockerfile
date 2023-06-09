# Build stage

FROM gradle:7.4.2-alpine AS BUILD
WORKDIR /app/
COPY backend .
RUN gradle assemble

# Package stage

FROM openjdk:17-jdk-alpine
WORKDIR /app/
COPY --from=BUILD /app/ .
EXPOSE 8080

ENTRYPOINT exec java -jar /app/build/libs/app.jar