package com.sfeir.kafka;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Repartitioned;
import org.apache.kafka.streams.kstream.StreamJoined;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Collections.singletonMap;

public class Main {

  public static void main(String... args) {
    new Main().step07();
  }

  private void step07() {
    // --------- Avro Serdes ---------

    Map<String, String> serdeConfig = singletonMap(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    final SpecificAvroSerde<Order> orderSerde = new SpecificAvroSerde<>();
    orderSerde.configure(serdeConfig, false);

    final SpecificAvroSerde<CardPayment> cardPaymentSerde = new SpecificAvroSerde<>();
    cardPaymentSerde.configure(serdeConfig, false);

    final SpecificAvroSerde<SuspiciousOrder> suspiciousOrderSerde = new SpecificAvroSerde<>();
    suspiciousOrderSerde.configure(serdeConfig, false);

    // --------- Stream Configuration ---------

    Properties properties = new Properties();

    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "step-07");
    properties.put(StreamsConfig.CLIENT_ID_CONFIG, "step-07-client");
    properties.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams-" + new Random().nextInt());
    properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5_000 + "");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // --------- Stream Topology ---------

    StreamsBuilder builder = new StreamsBuilder();

    KStream<String, Order> orderKStream = builder.stream(
      "orders",
      Consumed.with(Serdes.String(), orderSerde)
    );

    KStream<String, CardPayment> cardPaymentKStream = builder.stream(
      "card_payments",
      Consumed.with(Serdes.String(), cardPaymentSerde)
    );

    final Printed printed = Printed.toSysOut().withLabel("stdout");

    orderKStream
      .join(
        cardPaymentKStream,
        (left, right) -> SuspiciousOrder.newBuilder()
          .setOrderId(right.getId())
          .setCustomerId(left.getCustomerId())
          .setPaymentCount(0)
          .build(),
        JoinWindows.of(Duration.ofMinutes(5L)),
        StreamJoined.with(Serdes.String(), orderSerde, cardPaymentSerde)
      )
      .groupByKey(Grouped.with(Serdes.String(), suspiciousOrderSerde))
      .aggregate(
        SuspiciousOrder::new,
        (key, value, aggregate) -> SuspiciousOrder.newBuilder()
          .setCustomerId(value.getCustomerId())
          .setOrderId(value.getOrderId().toString())
          .setPaymentCount(aggregate.getPaymentCount() + 1)
          .build(),
        Materialized.with(Serdes.String(), suspiciousOrderSerde)
      )
      .filter((key, value) -> value.getPaymentCount() > 1)
      .toStream()
      .filter((key, value) -> value != null)
      .repartition(Repartitioned.with(Serdes.String(), suspiciousOrderSerde).withName("suspicious_orders"))
      .print(printed);

    KafkaStreams streams = new KafkaStreams(builder.build(), properties);
    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

}
