package com.sfeir.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger("step-02");

  private final String customerJson = "{\"" +
          "\": 1, " +
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
    properties.put(ProducerConfig., "");

    // 2. create producer
    Producer<Integer, String> producer = null;

    // 3. create record
    ProducerRecord<Integer, String> record = null;

    // 4. send record
    producer.send(record, (metadata, exception) -> {
      // 5. print message partition and offset

      latch.countDown();
    });

    // 6. close producer on shutdown
    Runtime.getRuntime().;

    latch.await(10, TimeUnit.SECONDS);

  }

}
