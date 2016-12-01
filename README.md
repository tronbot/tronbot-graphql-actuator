# Tronbot Graphql Actuator

Microservice applicaton for generating the graphql schema


This application is configured for Service Discovery and Configuration with Consul. 
On launch, it will refuse to start if it is not able to connect to Consul at (http://localhost:8500). 
For more information.

## Development

To start your application in the dev profile, simply run:

    ./gradlew



## Building for production

To optimize the graphqlActuator application for production, run:

    ./gradlew -Pprod clean bootRepackage

To ensure everything worked, run:

    java -jar build/libs/*.war



## Testing

To launch your application's tests, run:

    ./gradlew test
### Other tests

Performance tests are run by Gatling and written in Scala. They're located in `src/test/gatling` and can be run with:

    ./gradlew gatlingRun

For more information, refer to the Running tests page

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the `src/main/docker` folder to launch required third party services.
For example, to start a no database in a docker container, run:

    docker-compose -f src/main/docker/no.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/no.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./gradlew bootRepackage -Pprod buildDocker

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to Using Docker and Docker-Compose, this page also contains information on the docker-compose sub-generator (`yo jhipster:docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Consul
consul agent -dev

## Kafka
Win + X, A
docker-machine create --driver hyperv kafka
docker-machine start kafka
docker-machine env kafka
docker-compose -f src/main/docker/kafka.yml up -d

