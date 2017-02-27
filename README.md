
Simple message queue using kafka producer/consumer


Producer is using node-kafka library, simply throws a simple message onto the queue.

Consumer is a simple java class that pulls the message off the topic.


Pre-requisites:

Install NodeJs
Install kafka

https://kafka.apache.org/quickstart

*start zookeeper
*start kafka
*create a "simpleTopic" by running the following command:

cd into KAFKA_INSTALLATION_DIR/bin

kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic simpleTopic

to ensure that "simpleTopic" got created,

run: kafka-topics.sh --list --zookeeper localhost:2181  (simpleTopic is return from the list).


Once the topic is create, we are ready dump a simple message on it from node-kafka producer:

run the following commands:

cd node-kafka-producer

npm install

node simple-producer.js send simple message to kafka!


now, to see the message get picked up from the consumer side, we need to run the java consumer to consume the message

run the java main class: com.example.consumers.KafkaConsumer

should see a message like this:


consuming payload as key/value ... {payload=send simple message to kafka!}


I will expand this further to consume message from the beginning rather than consuming message from the
latest offset at a future time ... Maybe using spring-kafka project. Spring-Kafka does provide a nice wrapper
library to communicate with Kafka.







