# Kafka Setup and Message Consumption Guide

This guide provides instructions for setting up Kafka and consuming messages from a specified topic.

## Step 1: Start Zookeeper

Navigate to the Kafka directory:

```bash
cd kafka_2.13-3.7.0
```

Start Zookeeper server:

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

## Step 2: Start the Kafka broker service, Open another terminal session and run: 


Use the Kafka console consumer to view messages from the specified topic (`TsofenTopic` in this case):

```bash
 bin/kafka-server-start.sh config/server.properties 
```

## Step 3: Monitor Messages (Optional)

For monitoring messages, you can run the Kafka server(Open another terminal ):

```bash
bin/kafka-console-consumer.sh --topic TsofenTopic --from-beginning --bootstrap-server localhost:9092  
```


### Note:
- Make sure to replace `TsofenTopic` with the actual name of your Kafka topic.
- Ensure Kafka and Zookeeper are properly configured before running these commands.

