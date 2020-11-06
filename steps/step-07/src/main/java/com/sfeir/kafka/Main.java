package com.sfeir.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Map;
import java.util.Properties;

import static java.util.Collections.singletonMap;
import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

public class Main {

  public static void main(String... args) {
    new Main().step07();
  }

  private void step07() {
    // --------- Avro Serdes ---------

    Map<String, String> serdeConfig = singletonMap(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    // 1. Configure Serdes

    // --------- Stream Configuration ---------

    Properties properties = new Properties();

    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");

    // 2. Add stream configuration

    // --------- Stream Topology ---------

    StreamsBuilder builder = new StreamsBuilder();

    // 3. Create topology

    KafkaStreams streams = new KafkaStreams(builder.build(), properties);
    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

}
