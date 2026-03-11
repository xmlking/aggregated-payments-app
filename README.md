# Spring-Kafka-Functions

**Spring Cloud Streams** example

## Run

Start kafka

```shell
# start all - redpanda and console
docker compose up

rpk version
rpk cluster info
rpk topic delete visa-payments mastercard-payments giftcard-payments aggregated-payments

# create topic
rpk topic create -c cleanup.policy=compact -r 1 -p 1 visa-payments 
rpk topic create -c cleanup.policy=compact -r 1 -p 1 mastercard-payments
rpk topic create -c cleanup.policy=compact -r 1 -p 1 giftcard-payments
rpk topic create -c cleanup.policy=compact -r 1 -p 1 aggregated-payments

# produce
rpk topic produce visa-payments -k tansaction-id-1
{"transactionId": " tansaction-id-1", "amount": 121, "currency": "USD"}
{"transactionId": " tansaction-id-2", "amount": 122, "currency": "USD"}
# or
echo '{"transactionId": " tansaction-id-1", "amount": 131}' | rpk topic produce mastercard-payments -k tansaction-id-1
echo '{"transactionId": " tansaction-id-1", "amount": 141}' | rpk topic produce giftcard-payments -k tansaction-id-1
# consume
rpk topic consume visa-payments
rpk topic consume mastercard-payments
rpk topic consume giftcard-payments
rpk topic consume aggregated-payments
```

Start µService

```shell
gradle bootRun
# log at debug level
gradle bootRun --debug
```

## Build

```shell
gradle spotlessApply
gradle build
```

## Test

```shell
# list all schemas 
curl -s \
  "http://localhost:8081/subjects" \
  | jq .
# get schemas for `all-in-topic-value`
curl -s \
  "http://localhost:8081/subjects/aggregated-payments-value/versions/1" \
  | jq '.schema | fromjson' 
# (or) you can see ` "sensitive": "true"` property.
curl -s \
  "http://localhost:8081/subjects/aggregated-payments-value/versions/latest/schema" \
  | jq .
```

## Lint

```shell
ktlint --format
# or
ktlint -F
```
## Operations

### Metrics

```shell
curlie :3000/actuator

curlie :3000/actuator/health

curlie :3000/actuator/metrics
curlie :3000/actuator/metrics/kafka.admin.client.request.total

curlie :3000/actuator/bindings
curlie :3000/actuator/bindings/payments-out-0
curlie :3000/actuator/bindings/payments-in-1
curlie :3000/actuator/bindings/payments-in-0

curlie :3000/actuator/kafkastreamstopology
curlie :3000/actuator/kafkastreamstopology/<application-id of the processor>
curlie :3000/actuator/kafkastreamstopology/state-applicationId
curlie :3000/actuator/kafkastreamstopology/city-applicationId
curlie :3000/actuator/kafkastreamstopology/print-applicationId
````

### Binding control

```shell
curl -d '{"state":"STOPPED"}' -H "Content-Type: application/json" -X POST localhost:3000/actuator/bindings/payments-in-0
curlie :3000/actuator/bindings/payments-in-0
curl -d '{"state":"STARTED"}' -H "Content-Type: application/json" -X POST localhost:3000/actuator/bindings/payments-in-0
curl -d '{"state":"PAUSED"}'  -H "Content-Type: application/json" -X POST localhost:3000/actuator/bindings/payments-in-0
curl -d '{"state":"RESUMED"}' -H "Content-Type: application/json" -X POST localhost:3000/actuator/bindings/payments-in-0
```

### Binders
we need add `kafka` binder for `Supplier` functions to work
We can only use `Consumer` and `Function` functions with `KStream` binder.

```gradle
implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
 ```

## Maintenance

Update Gradle wrapper 
```shell
./gradlew wrapper --gradle-version 9.4.0  --distribution-type all
```

show outdated dependencies
```shell
gradle dependencyUpdates -Drevision=release
```

## Reference
- [Fictional Spring Cloud Streams](https://github.com/spring-cloud/spring-cloud-stream/blob/main/docs/src/main/asciidoc/spring-cloud-stream.adoc#functions-with-multiple-input-and-output-arguments)
- [Introducing Java Functions for Spring Cloud Stream Applications - Part 0](https://spring.io/blog/2020/07/13/introducing-java-functions-for-spring-cloud-stream-applications-part-0)
- [spring-cloud-stream-binder-kafka Docs](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream-binder-kafka/)
- [No need for Schema Registry in your Spring-Kafka tests](https://medium.com/@igorvlahek1/no-need-for-schema-registry-in-your-spring-kafka-tests-a5b81468a0e1)
### Example projects
- https://github.com/spring-cloud/spring-cloud-stream-samples/
- https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/kafka-streams-samples
- InteractiveQueryService https://github.com/piomin/sample-spring-cloud-stream-kafka/blob/master/stock-service/src/main/java/pl/piomin/samples/kafka/stock/controller/TransactionController.java
- https://github.com/spring-cloud/spring-cloud-stream-samples/blob/main/kafka-streams-samples/kafka-streams-inventory-count/src/main/java/kafka/streams/inventory/count/KafkaStreamsInventoryCountApplication.java
- https://github.com/ru-rocker/kafka-stream-employee-example
- [Distributed Transactions in Microservices with SAGA, Kafka Streams and Spring Boot ](https://github.com/piomin/sample-spring-kafka-microservices)