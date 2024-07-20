### Publish to Remote & Deploy to Azure Container App
1. Run `./gradlew build`
2. Run `docker build --platform linux/amd64 --tag xzhuangaa/coolcode:server-<version> .`
    - `--platform linux/amd64` is required for container to run on Azure amd64 infrastructure
3. Try running it locally to make sure the image works
   - `docker run -d --name coolcode-server --network=crazycollectors-net -p 9443:9443 xzhuangaa/coolcode:server-<version>`
3. Run `docker push xzhuangaa/coolcode:server-<version>`

### Deployment
Run `sudo version=<version> ./start_server.sh`