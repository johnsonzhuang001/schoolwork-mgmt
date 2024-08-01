#! /bin/bash

if [ -z ${version+x} ]; then
  echo "version is needed"
  exit 1
fi

docker run -d --name coolcode-server \
 --network=crazycollectors-net \
 -v /home/xzhuangaa/apps/crazycollectors/app/certs:/app/certs\
 -p 9443:9443 \
 -e APP_ENV='default'\
 -e KEYSTORE_PATH='/app/certs/server.p12'\
 -e KEYSTORE_PASSWORD='{password}'\
 -e CRAZY_COLLECTORS_CERT='/app/certs/api.crazy-collectors.com.crt'\
 -e HEROKU_CERT='/app/certs/herokuapp.com.crt'\
 -e INSTRUCTION_URL='https://jolly-coast-00af64000.5.azurestaticapps.net/signin'\
 -e DISCORD_COOLCODEHACKER_TOKEN='{token}'\
 -e DISCORD_COOLCODEHACKER_CHANNEL_ID='{channel_id}'\
 -e COORDINATOR_AUTH_TOKEN='{coordinator_auth_token}'\
 -e MSSQL_URL='jdbc:sqlserver://crazycollectors-mssql:1433;encrypt=true;trustServerCertificate=true;database=coolcode'\
 -e MSSQL_USERNAME='sa'\
 -e MSSQL_PASSWORD='{password}'\
 xzhuangaa/coolcode:server-"${version}"