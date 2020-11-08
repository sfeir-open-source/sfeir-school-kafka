package com.sfeir.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger("step-03");

  private final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

  public static void main(String... args) {
    new Main().step03();
  }

  private void step03() {
    Properties properties = new Properties();

    // 1. set consumer configuration
    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, "step-03");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "step-03-group");
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE.toString());
    properties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "1");
    properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());
    properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, Boolean.FALSE.toString());

    // 2. create consumer
    Consumer<Integer, String> consumer = new KafkaConsumer<>(properties);

    try {
      // 3. subscribe to topic
      consumer.subscribe(Collections.singletonList("customers"), new CustomerRebalanceListener(consumer));

      // 4. register shutdown hook
      Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

      while (true) {
        // 5. poll records
        ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofSeconds(10));

        // 6. print records to console
        records.forEach(record -> {
          logger.warn("Received record with key: {} and value: {}", record.key(), record.value());

          TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
          OffsetAndMetadata offset = new OffsetAndMetadata(record.offset() + 1);

          offsets.put(topicPartition, offset);
        });

        // 7. async commit offsets
        consumer.commitAsync(offsets, null);
      }
    } catch (final WakeupException e) {
      // let it go ...
    } catch (final Exception e) {
      logger.error("Consumer failed", e);
    } finally {
      // 8. commit offsets before closing
      try {
        consumer.commitSync(offsets);
      } finally {
        consumer.close();
      }
    }
  }

  private class CustomerRebalanceListener implements ConsumerRebalanceListener {

    private final Consumer<Integer, String> consumer;

    public CustomerRebalanceListener(Consumer<Integer, String> consumer) {
      this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
      consumer.commitSync(offsets);
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
      // noop
    }

  }

}
