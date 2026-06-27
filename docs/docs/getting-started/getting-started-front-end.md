# Getting Started: Front-end

There are two TypeScript projects in this repository the front-end project, and the documentaion project.

The front-end project found in \<project_root\>/frontend/crypto_front is where the React front-end is implemented. This document mainly focuses on this project as this is where the bulk of front-end development will be done.

The font-end provides:
* Web interface for users to view the cryptocurrency data
* Ability to control various settings so that the user can customize how they view the cryptocurrency data.

At the end of this guide you should have:
* The front-end react project ready to start development

## Development setup

### Minimum Required Software Versions:
* Node.JS 20.0
* React 19.2.5
* Recharts 3.8.1
* shadcn 4.6.0
* orval 8.10.0
* vite 8.0.10
* typescript 6.0.2

Version numbers can be changed for later versions at your discretion, but I recommend using only well established LTS versions e.g. I chose JDK 21 over 25 because support for 21 is better established than 25. Long term support and compatibility are far more important than new and shiny.

## 1. Clone the project from GitHub
- download the zip archive or clone it, whichever you prefer.

https://github.com/waynepotts/crypto_project

## 2. Create the .env files
- As we're not saving database passwords and API keys etc. to version control you need to set up the way to talk to the database and external REST services through environment variables. N.B. .env file should be added to the .gitignore and never commited to version control.

The application supports separate environment configurations for development, testing, and production:

- `.env.dev`
- `.env.test`
- `.env.prod`

The examples are found in the ./frontend/crypto_front directory. Copy the files, removing the .example extension to make them usable (example command below).
```shell 
cp ./frontend/crypto_front/.env.dev.example ./frontend/crypto_front/.env.dev
```

## 3. Test that the rest services are up and running
- If you have the rest services running on your development machine then you should be able to use the standard config found in the .env.dev file. If not then you'll need to change the VITE_API_URL value to where the rest services are being hosted.
- In a web browser/postman/curl test you get a response from the below URL

:::tip[Run the rest services on your local machine]

Although you can point the rest services URL to something that isn't run locally. Running a local copy means that you other people making changes to the database etc. that you're not aware of. 

:::

```
http://localhost:8080/api/v1/info
```
You should get a response similar to the JSON below. N.B. The build time is in UTC+0
```json
{"application":"restservices","version":"0.0.1","buildTime":"2026-05-26T17:44:54.357Z"}
```

## 4. Run the project with npm