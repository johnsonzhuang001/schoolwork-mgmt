FROM openjdk:17-oracle

ENV APP_ENV "docker"
ENV MSSQL_URL "url-to-override"
ENV MSSQL_USERNAME "username-to-override"
ENV MSSQL_PASSWORD "password-to-override"

RUN mkdir app
COPY ./build/libs/*.jar app
WORKDIR app

EXPOSE 443
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${APP_ENV}", "server-0.0.1.jar"]
