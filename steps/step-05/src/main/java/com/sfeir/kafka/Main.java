package com.sfeir.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger("step-05");

  public static void main(String[] args) throws InterruptedException {
    new Main().step05();
  }

  private void step05() throws InterruptedException {
    // 1. produce Avro record
    produce();

    Thread.sleep(Duration.ofSeconds(5).toMillis());

    // 2. consume Avro record
    consume();
  }

  private void produce() {
    Properties properties = new Properties();

    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092");
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

    // ...
  }

  private void consume() {
    Properties properties = new Properties();

    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092");
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

    // ...
  }

}
