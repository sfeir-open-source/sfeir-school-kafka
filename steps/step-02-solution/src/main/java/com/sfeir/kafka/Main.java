package com.sfeir.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger("step-02");

  private final String customerJson = "{" +
          "\"id\": 1, " +
          "\"email\": \"john.doe@gmail.com\", " +
          "\"gender\": \"MALE\"" +
          "}";

  public static void main(String... args) throws InterruptedException {
    new Main().step01();
  }

  private void step01() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);

    Properties properties = new Properties();

    // 1. set producer configuration
    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, "step-01");
    properties.put(ProducerConfig.ACKS_CONFIG, "all");
    properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
    properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE + "");
    properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");

    // 2. create producer
    Producer<Integer, String> producer = new KafkaProducer<>(properties);

    // 3. create record
    ProducerRecord<Integer, String> record = new ProducerRecord<>("customers", 1, customerJson);

    // 4. send record
    producer.send(record, (metadata, exception) -> {
      // 5. print message partition and offset
      if (exception == null) {
        logger.warn(
                "Produced message for partition {} and offset {}",
                metadata.partition(),
                metadata.offset()
        );
      } else {
        logger.error("Failed to produce message !", exception);
      }

      latch.countDown();
    });

    // 6. close producer on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

    latch.await(10, TimeUnit.SECONDS);

  }

}
