# MBank

This application implements a Bank HTTP API following the requirements containing 3 HTTP methods:

- `GET /movements/{customerId}`: Returns the customer movements.
- `POST /deposits/{customerId}`: Deposits an amount to customer account.
- `POST /withdraws/{customerId}`: Withdraws an amount from customer account.

## Tools Used

- Java 17
- Gradle
- Spring Boot
- H2 Database
- Junit5
- Mockito

## Instructions

### How to Compile and Run

As a gradle application using Spring, you can run a build command like below:

```shell
./gradlew clean build
```

This command will generate a jar file at the folder `build\libs`, that can be run with the following command:

```shell
java -jar build/libs/mbank-0.0.1-SNAPSHOT.jar
```

You can also run the application through the Gradle run command:

```shell
./gradle bootRun
```

### Running Tests

Some Junit tests were implemented to assert the application behaviour, these tests can be executed with:

```shell
./gradle test
```

### Calling the API

The simplest way to call the API is to use `curl` to perform HTTP Requests, bellow some examples for each operation:


#### Depositing an amount

```shell
curl -XPOST -H "Content-Type: application/json" localhost:8080/deposits/1 -d '{"amount": -50}'
```

Response
```json
{"id":1,"customerId":1,"amount":50,"balance":50,"dateTime":"2023-02-21T18:00:00.1148551"}
```

#### Withdrawing an amount

```shell
curl -XPOST -H "Content-Type: application/json" localhost:8080/withdraws/1 -d '{"amount": -10}'
```

Response
```json
{"id":2,"customerId":1,"amount":-10,"balance":40,"dateTime":"2023-02-21T18:01:32.3745726"}
```

#### Finding Movements

```shell
curl localhost:8080/movements/1
```

Response
```json
[
  {"id":1,"customerId":1,"amount":50,"balance":50,"dateTime":"2023-02-21T18:00:00.114855"},
  {"id":2,"customerId":1,"amount":-10,"balance":40,"dateTime":"2023-02-21T18:01:32.374573"}
]
```