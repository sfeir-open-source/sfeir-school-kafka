package com.sfeir.kafka;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

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

  private void produce() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);

    Properties properties = new Properties();

    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, "step-05-producer");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
    properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, Boolean.TRUE.toString());
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE + "");
    properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");

    Producer<Integer, Customer> producer = new KafkaProducer<>(properties);

    Customer customer = Customer.newBuilder()
            .setId(1)
            .setEmail("john.doe@sfeir.com")
            .setGender(Gender.MALE)
            .build();

    ProducerRecord<Integer, Customer> record = new ProducerRecord<>("customers", 1, customer);

    producer.send(record, (metadata, exception) -> {
      if (exception != null) {
        logger.error("Failed to send record", exception);
      }

      latch.countDown();
    });

    latch.await();
  }

  private void consume() {
    Properties properties = new Properties();

    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, "step-05-consumer");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "step-05-group");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, Boolean.FALSE.toString());
    properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, Boolean.TRUE.toString());
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE.toString());
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
    properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
    properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, Boolean.FALSE.toString());

    Consumer<Integer, Customer> consumer = new KafkaConsumer<>(properties);

    consumer.subscribe(Collections.singletonList("customers"));

    Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

    try {
      while (true) {
        ConsumerRecords<Integer, Customer> records = consumer.poll(Duration.ofSeconds(10));

        records.forEach(record ->
                logger.warn("Key: {}, Value: {}", record.key(), record.value())
        );

        consumer.commitAsync();
      }
    } catch (final WakeupException e) {
      // ...
    } catch (final Exception e) {
      logger.error("Consumer failed", e);
    } finally {
      consumer.commitSync();
      consumer.close();
    }
  }

}
