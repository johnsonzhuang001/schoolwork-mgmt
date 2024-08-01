FROM openjdk:17-oracle

ENV APP_ENV "docker"
ENV MSSQL_URL "url-to-override"
ENV MSSQL_USERNAME "username-to-override"
ENV MSSQL_PASSWORD "password-to-override"
ENV KEYSTORE_PATH "path-to-override"
ENV KEYSTORE_PASSWORD "password-to-override"
ENV DISCORD_COOLCODEHACKER_TOKEN "token-to-override"
ENV DISCORD_COOLCODEHACKER_CHANNEL_ID "channel-id-to-override"

RUN mkdir app
COPY ./build/libs/*.jar app
WORKDIR app

EXPOSE 9443
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${APP_ENV}", "server-0.0.2.jar"]
