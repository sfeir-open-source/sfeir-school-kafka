package com.sfeir.kafka.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfeir.kafka.CardPayment;
import com.sfeir.kafka.Customer;
import com.sfeir.kafka.Gender;
import com.sfeir.kafka.Order;
import com.sfeir.kafka.init.models.CardPaymentDTO;
import com.sfeir.kafka.init.models.CustomerDTO;
import com.sfeir.kafka.init.models.OrderDTO;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;

public class Main {

  private final ObjectMapper om = new ObjectMapper();

  public static void main(String... args) throws IOException, InterruptedException {
    new Main().step07();
  }

  private void step07() throws IOException, InterruptedException {
    produceCustomers();
    produceOrders();
    produceCardPayments();

    Thread.sleep(Duration.ofSeconds(10).toMillis());
  }

  private void produceCustomers() throws IOException {
    Producer<Integer, Customer> producer = new KafkaProducer<>(properties(IntegerSerializer.class));

    Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

    CustomerDTO[] customers = readJson("customers.json", CustomerDTO[].class);

    for (CustomerDTO customerDTO : customers) {
      Customer customer = Customer.newBuilder()
              .setId(customerDTO.getId())
              .setEmail(customerDTO.getEmail())
              .setGender(Gender.valueOf(customerDTO.getGender()))
              .build();

      ProducerRecord<Integer, Customer> record = new ProducerRecord<>(
              "customers",
              customer.getId(),
              customer
      );

      producer.send(record);
    }
  }

  private void produceOrders() throws IOException {
    Producer<String, Order> producer = new KafkaProducer<>(properties(StringSerializer.class));

    Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

    OrderDTO[] orders = readJson("orders.json", OrderDTO[].class);

    for (OrderDTO orderDTO : orders) {
      Order order = Order.newBuilder()
              .setId(orderDTO.getId())
              .setCustomerId(orderDTO.getCustomerId())
              .setOrderTimestamp(Clock.systemUTC().millis())
              .setTotalPrice(orderDTO.getTotalPrice())
              .build();

      ProducerRecord<String, Order> record = new ProducerRecord<>(
              "orders",
              order.getId().toString(),
              order
      );

      producer.send(record);
    }
  }

  private void produceCardPayments() throws IOException {
    Producer<String, CardPayment> producer = new KafkaProducer<>(properties(StringSerializer.class));

    Runtime.getRuntime().addShutdownHook(new Thread(producer::close));

    CardPaymentDTO[] payments = readJson("card_payments.json", CardPaymentDTO[].class);

    for (CardPaymentDTO paymentDTO : payments) {
      CardPayment cardPayment = CardPayment.newBuilder()
              .setId(paymentDTO.getId())
              .setPaidPrice(paymentDTO.getPaidPrice())
              .build();

      ProducerRecord<String, CardPayment> record = new ProducerRecord<>(
              "card_payments",
              cardPayment.getId().toString(),
              cardPayment
      );

      producer.send(record);
    }
  }

  private Properties properties(Class<?> keySerializer) {
    Properties properties = new Properties();

    properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
    properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, "before-step-07-" + new Random().nextInt());
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
    properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, Boolean.FALSE.toString());

    return properties;
  }

  private <T> T readJson(String name, Class<T> clazz) throws IOException {
    final String content = IOUtils.toString(getClass().getResourceAsStream("/data/" + name));
    return om.readValue(content, clazz);
  }

}
