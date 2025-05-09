# Transaction Detail Docker Service

## Build Project

Navigate to the module `transaction-manager/transaction-detail` and package the service using Maven:

```shell
mvn clean package -DskipTests=true -P docker-arm64
```

## Build Docker Image

Navigate to the Docker module `transaction-detail/transaction-detail-docker` and execute the following command to build the Docker image:

```shell
docker build -f ./Dockerfile --platform linux/arm64 -t healchow/transaction-detail:1.0.0 .
```

## Start Container

Execute the following command to start the Docker container:

```shell
docker run -d --name transaction-detail -p 8080:8080 \
-e ACTIVE_PROFILE=dev \
-e JVM_HEAP_OPTS='-Xms2048m -Xmx2048m' \
healchow/transaction-detail:1.0.0
```

## Access the Service

Access `http://127.0.0.1:8080/v1/doc.html` in a web browser to browse the service documents.

As shown in the figure below:

<img src="../../docs/img/transaction-detail-doc.png" alt="Transaction-Detail-Doc" width="1000px" />
