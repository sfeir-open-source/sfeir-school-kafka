package com.sfeir.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    // add more consumer configuration

    // 2. create consumer
    Consumer<Integer, String> consumer = null;

    // 3. subscribe to topic
    consumer.subscribe(Collections.singletonList(""));

    // 4. register shutdown hook
    Runtime.getRuntime().addShutdownHook(null);

    try {
      while (true) {
        // 5. poll records
        ConsumerRecords<Integer, String> records = null;

        // 6. print records to console
        records.forEach(record -> {
          TopicPartition topicPartition = null;
          OffsetAndMetadata offset = null;

          offsets.put(topicPartition, offset);
        });

        // 7. async commit offsets
        consumer.commitAsync(null, null);
      }
    } catch (final WakeupException e) {
      // let it go ...
    } catch (final Exception e) {
      logger.error("Consumer failed", e);
    } finally {
      // 8. commit offsets before closing

    }

  }

}
