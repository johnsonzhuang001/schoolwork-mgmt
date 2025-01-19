# Backend
This server application serves as the backend for both the hacker website and the CoolCode Education website.

## How to Start the Server Locally

### Run Local MSSQL Server
`docker run --name coolcode-mssql --network=<your network name> -v <your local path>:/var/opt/mssql -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=<your-password>" -p 1433:1433 -d mcr.microsoft.com/mssql/server:2019-latest`
<br/>
Default username is `sa`

### Set up locally
1. Add `application-local.yaml` to `main/resources`
2. Set environment variable `SPRING_PROFILES_ACTIVE=local`
3. Add to the local profile file
```yaml
spring:
   application:
      name: server
   datasource:
      url: jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;database=<your db>
      username: sa
      password: <your pwd>
   ssl:
      bundle:
         pem:
            crazy-collectors:
               truststore:
                  certificate: "classpath:<path to the server crt>"
            heroku:
               truststore:
                  certificate: "classpath:<path to the heroku crt>"
server:
   ssl:
      enabled: true
      key-store-type: PKCS12
      key-store: "classpath:<path to the server keystore cert>"
      key-store-password: '<keystore cert password>'
discord:
   coolcodehacker:
      token1: '<discord token>'
      channel: '<channel id number>'

app:
   coordinator-auth-token: '<auth token for the coordinator>'
   instruction-url: '<URL of the hacker instruction website>'
```
4. Run `ServerApplication`

## Publish to Remote
1. Run `./gradlew build`
2. Run `docker build --platform linux/amd64 --tag xzhuangaa/coolcode:server-<version> .`
    - `--platform linux/amd64` is required for container to run on Azure amd64 infrastructure
3. Try running it locally to make sure the image works
   - `docker run -d --name coolcode-server --network=<your network name> -p 9443:9443 xzhuangaa/coolcode:server-<version>`
3. Run `docker push xzhuangaa/coolcode:server-<version>`

### Deployment
Run `sudo version=<version> ./start_server.sh`