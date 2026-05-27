# Getting Started: Java

# Getting Started: Java Backend

This guide explains how to set up and run the Spring Boot backend locally for development.

The backend provides:
- REST APIs for cryptocurrency data
- CoinGecko synchronization
- historical market data storage
- Liquibase-managed PostgreSQL schema migrations
- OpenAPI/Swagger documentation

At the end of this guide you should have:
- PostgreSQL running
- the backend compiled and tested
- Liquibase migrations applied
- the REST API accessible locally

## Development setup

### Minimum Required Software Versions:
* PostgreSQL 16+
* Java 21 JDK
* Maven 3.9+

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

The examples are found in the ./backend/restservices directory. Copy the files, removing the .example extension to make them usable (example command below).
```shell 
cp ./backend/restservices/.env.dev.example ./backend/restservices/.env.dev
```

## 3. Create the database
   - Do not modify the database manually. All database changes should be performed through Liquibase change sets.

create the database by executing the below statement from psql shell
```sql
CREATE DATABASE crypto;
```
or Alt + Shift + N in pgAdmin and use the popup dialog.

N.B. I've used the name 'crypto' for the database, you're free to use something else as long as the .env files also use the same name and user etc. If using an existing database, Liquibase migrations may fail if required table names already exist.

## 4. Load the environment variables
   - To simplify loading the environment variables I've included a PowerShell script to load them, this should be done in any new terminal window (only needs to be run once for each terminal). Linux/macOS users can either manually export the variables or create an equivalent shell script for their environment.

change into to java project directory if required
```shell 
$ cd ./backend/restservices
$ ./Load-env.ps1
```

If successful, the terminal output should resemble the following:
```
Loaded SPRING_PROFILES_ACTIVE 
Loaded DB_USERNAME 
Loaded DB_URL
Loaded DB_PASSWORD
Loaded COINGECKO_API
Loaded COINGECKO_URL
Loaded CORS_CONFIG_URL
Loaded CORS_CONFIG_PORT
```

## 5. Do a maven build
   - Now that the database exists we should be able to do a maven build, which will create the database tables and populate them with some seed data.
   - The first Maven build may take several minutes while dependencies are downloaded.
   - Ensure PostgreSQL is running before executing the Maven build because this is when Liquibase will create the tables in the database.
   - Liquibase will automatically create the database tables and populate reference/seed data required for development. Liquibase creates the first two coins, bitcoin and ethereum and insert some fictional market price data which can be deleted once actual data is downloaded from coin gecko.

(Assuming you're still in the ./backend/restservices directory from step 4)
```shell
$ mvn clean install
```
If everything went to plan then the back-end should have been built, unit tested, and the database tables should now be created and populated as well.

## 6. Running the jar file
   - Run the command below in your terminal and the rest services should start up
   - Make sure that the environment variables are loaded before running the jar file.
(Assuming you're still in the ./backend/restservices directory)
```shell
$ java -jar ./target/restservices-0.0.1.jar
```
For development, it is usually easier to run the application directly through Maven:

```shell
$ mvn spring-boot:run
```

## 7. Test that the rest services are up and running
   - In a web browser/postman/curl test you get a response from the below URL

```
http://localhost:8080/api/v1/info
```
You should get a response similar to the JSON below. N.B. The build time is in UTC+0
```json
{"application":"restservices","version":"0.0.1","buildTime":"2026-05-26T17:44:54.357Z"}
```
## 8. Testing with Swagger
   - Swagger is also available for more information on what can be done with the REST services.

http://localhost:8080/swagger-ui/index.html