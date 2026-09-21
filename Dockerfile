FROM gradle:jdk17-corretto AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/notificacao.jar
EXPOSE 8082
CMD ["java", "-jar", "/app/notificacao.jar"]

