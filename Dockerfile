FROM openjdk:17
ARG ARTIFACT="JWT-Demo2"
ARG VERSION="0.0.1-SNAPSHOT"
ARG FILE="/build/libs/${ARTIFACT}-${VERSION}.jar"
COPY ${FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8081