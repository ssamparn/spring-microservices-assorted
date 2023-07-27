
# Kafka SetUp

## Build the kafka image from docker file.

Navigate to /workspace/kafka-setup/docker-image directory and issue below docker commands to build the kafka-image.

```bash
$ docker build --tag=<repository-name>/<imagae-name> .
$ docker build --tag=ssamantr/reactive-kafka .
```

#### Push the kafka image to docker hub
```bash
$ docker push <repository-name>/<imagae-name>
$ docker push ssamantr/reactive-kafka
```

#### Run the kafka image
```bash
$ docker run <repository-name>/<imagae-name>
$ docker run ssamantr/reactive-kafka
```

#### As an alternative you can also run docker image from docker compose

#### Navigate to /workspace/kafka-setup/docker-compose directory

```bash
$ docker-compose up
```
```bash
$ docker ps
```

#### To get access to running kafka container
```bash
$ docker exec -it <container-name> bash
$ docker exec -it reactive-kafka bash
```

## Topic

### Create a kafka topic called hello-world

#### Note: we assume that directory which contains 'kafka-topics.sh' is included in the PATH
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic <topic-name> --create
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic hello-world --create
```

#### List all topics
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --list
```

#### Describe a topic
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic hello-world --describe
```

#### Delete a topic
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --delete
```

#### Create topic with partitons
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --create --partitions 2
```

#### Alter the number of partitions in a topic
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --alter --partitions 4
```

#### Create topic with replicaiton factor
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic order-events --create --partitions 2 --replication-factor 3
```
## Producer

#### To produce messages
```bash
$ kafka-console-producer.sh --bootstrap-server localhost:9092 --topic hello-world
```

#### linger.ms
```bash
$ kafka-console-producer.sh --bootstrap-server localhost:9092 --topic hello-world --timeout 100
```

## Consumer

#### To consume messages
```bash
$ kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello-world
```

#### To consume from beginning
```bash
$ kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello-world --from-beginning
```

## Print Offset

#### To print offset, time etc
```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--property print.offset=true \
--property print.timestamp=true
```

## Consumer Group

#### Create console producer
```bash
$ kafka-console-producer.sh \
--bootstrap-server localhost:9092 \
--topic order-events \
--property key.separator=: \
--property parse.key=true
```

#### Create console consumer with a consumer group
```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--property print.offset=true \
--property print.key=true \
--group <group-name>
```

#### List all the consumer groups
```bash
$ kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

#### Describe a consumer group
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--group <group-name> \
--describe
```
## Reset Offset

### Note: Stop the consumers before you enter this command

#### dry-run
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--group cg \
--topic hello-world \
--reset-offsets \
--shift-by -3 \
--dry-run
```

#### reset offset by shifting the offset
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--group cg \
--topic hello-world \
--reset-offsets \
--shift-by -3 \
--execute
```

#### reset by duration

```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--group cg \
--reset-offsets \
--by-duration PT5M \
--execute
```

#### -- to the beginning
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--group cg \
--reset-offsets \
--to-earliest \
--execute
```

#### -- to the end
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--group cg \
--reset-offsets \
--to-latest \
--execute
```

#### -- to date-time
```bash
$ kafka-consumer-groups.sh \
--bootstrap-server localhost:9092 \
--topic hello-world \
--group cg \
--reset-offsets \
--to-datetime 2023-01-01T01:00:00.000 \
--execute
```

## Kafka Transaction

```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic transfer-requests --create
```
```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic transaction-events --create
```

```bash
$ kafka-console-producer.sh \
--bootstrap-server localhost:9092 \
--topic transfer-requests \
--property key.separator=: \
--property parse.key=true
```

```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic transaction-events \
--property print.key=true \
--isolation-level=read_committed \
--from-beginning
```

```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic transaction-events \
--property print.key=true \
--from-beginning
```

### Commands to demo kafka transaction of section 15

```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic transfer-requests --create
```

```bash
$ kafka-topics.sh --bootstrap-server localhost:9092 --topic transaction-events --create
```

```bash
$ kafka-console-producer.sh \
--bootstrap-server localhost:9092 \
--topic transfer-requests \
--property key.separator=: \
--property parse.key=true
```

#### Console consumer with isolation level

```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic transaction-events \
--property print.key=true \
--isolation-level=read_committed \
--from-beginning
```

#### Console consumer without isolation level

```bash
$ kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic transaction-events \
--property print.key=true \
--from-beginning
```