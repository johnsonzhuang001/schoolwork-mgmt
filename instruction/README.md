# Hacker Instruction UI
This serves as the UI of the hacker website where the instructions will be given to the challengers.

## How to start the application locally
Run `npm run dev`

## Build Docker & Run Locally
- Run `docker build --platform linux/amd64 --tag xzhuangaa/coolcode:instruction-0.0.1 .`
- Run `docker push xzhuangaa/coolcode:instruction-<version>`
- Run `docker run -d --name coolcode-instruction --network=crazycollectors-net -e REACT_APP_API_URL='https://api.crazy-collectors.com/coolcode' -e NODE_TLS_REJECT_UNAUTHORIZED=0 -p 3000:3000 xzhuangaa/coolcode:instruction-<version>`

Please update the repository to your own Docker repository

## Release to Heroku
- Comment `EXPOSE 3000` in `Dockerfile`
- Change `API_BASE_URL` in `url.ts` to `https://api.crazy-collectors.com/coolcode`
- Run `heroku container:push web -a coolcodehack`
- Run `heroku container:release web -a coolcodehack`