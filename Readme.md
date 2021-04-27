# Amazon DynamoDB CRUD application with Spring Webflux

This repository contains sample Aws DynamoDB CRUD application with Spring Webflux and TestContainers integration.

## Features

* Reactive API using Spring Webflux.
* [AWS SDK for Java 2.x.](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
* Integration tests for DynamoDB using Testcontainers.
* Docker-compose file with [dynamoDB](https://hub.docker.com/r/amazon/dynamodb-local/), [dynamodb-admin](https://hub.docker.com/r/aaronshaf/dynamodb-admin) and [AWS cli](https://hub.docker.com/r/banst/awscli).

## Getting Started

### Prerequisite

* Java 11
* Docker

### Usage

* Build docker image.
  ```shell
  ./gradlew bootBuildImage
  ```

* Start docker-compose demo.
  ```shell
  docker-compose -f docker/docker-compose.yml -f docker/docker-compose.app.yml up -d
  ```

* List containers and check if all are `Up`.
    ```shell
    docker-compose -f docker/docker-compose.yml -f docker/docker-compose.app.yml ps 

    #       Name                   Command                State             Ports         
    # -----------------------------------------------------------------------------------
    # app              /cnb/process/web                 Up         0.0.0.0:8080->8080/tcp
    # awscli           aws dynamodb create-table  ...   Exit 255                         
    # dynamodb         java -jar DynamoDBLocal.ja ...   Up         0.0.0.0:8000->8000/tcp
    # dynamodb-admin   node bin/dynamodb-admin.js       Up         0.0.0.0:8001->8001/tcp
    ```

* Verify if 'users' table was successfully created. Use either `curl -X GET --location "http://localhost:8080/users"` or
  open your web browser and go to [dynamo dashboard page](http://localhost:8001/tables/users).


* Test other CRUD endpoints using curl e.g.:
  ```shell
  # Create new user:
  curl --header "Content-Type: application/json" \                                      
  --request POST \
  --data '{"email":"curl_user@test.com"}' \
  http://localhost:8080/users
  ```

* Stop docker-compose demo.
  ```shell
  docker-compose -f docker/docker-compose.yml -f docker/docker-compose.app.yml down -v
  ```

## References

* [Getting Started with Java and DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.html)
* [Testcontainers](https://www.testcontainers.org/)
* [Installing, updating, and uninstalling the AWS CLI version 2 on macOS](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2-mac.html)


## Additional Links

* [Working with Global Secondary Indexes: Java](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GSIJavaDocumentAPI.html)
* [Design a Database for a Mobile App](https://aws.amazon.com/getting-started/hands-on/design-a-database-for-a-mobile-app-with-dynamodb/)
* [How to model one-to-many relationships in DynamoDB](https://www.alexdebrie.com/posts/dynamodb-one-to-many/)

## License

Distributed under the MIT License. See `LICENSE` for more information.
