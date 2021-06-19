package com.sfeir.kafka;

import io.confluent.kafka.schemaregistry.avro.AvroSchema;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class KafkaStreamsTest {

  private final MockSchemaRegistryClient schemaRegistryClient = new MockSchemaRegistryClient();
  private final CountCustomersByGenderApp app = new CountCustomersByGenderApp(schemaRegistryClient);

  private TopologyTestDriver testDriver;
  private TestInputTopic<Integer, Customer> inputTopic;
  private TestOutputTopic<String, Customer> outputTopic;

  @BeforeEach
  void beforeEach() throws IOException, RestClientException {
    schemaRegistryClient.register("customers-value", new AvroSchema(Customer.SCHEMA$));

    testDriver = new TopologyTestDriver(app.topology(), app.props());
    inputTopic = testDriver
      .createInputTopic("customers", new IntegerSerializer(), app.customerSerde().serializer());
    outputTopic = testDriver
      .createOutputTopic("customers_by_gender", new StringDeserializer(), app.customerSerde().deserializer());
  }

  @Test
  void shouldOutputCustomersByGender() {
    inputTopic.pipeKeyValueList(customersRecords());
    List<KeyValue<String, Customer>> results = outputTopic.readKeyValuesToList();
    KeyValueStore<String, Long> kvStore = testDriver.getKeyValueStore("customers_count_by_gender_store");

    Assertions.assertEquals(3, results.size());
    Assertions.assertEquals(2, (long) kvStore.get(Gender.MALE.name()));
    Assertions.assertEquals(1, (long) kvStore.get(Gender.FEMALE.name()));
  }

  @AfterEach
  void afterAll() {
    testDriver.close();
  }

  private List<KeyValue<Integer, Customer>> customersRecords() {
    return customers()
      .stream()
      .map(customer -> KeyValue.pair(customer.getId(), customer))
      .collect(Collectors.toList());
  }

  private List<Customer> customers() {
    return Arrays.asList(
      Customer.newBuilder()
        .setId(1)
        .setEmail("john.doe@sfeir.com")
        .setGender(Gender.MALE)
        .build(),
      Customer.newBuilder()
        .setId(2)
        .setEmail("bob.doe@sfeir.com")
        .setGender(Gender.MALE)
        .build(),
      Customer.newBuilder()
        .setId(3)
        .setEmail("jane.doe@sfeir.com")
        .setGender(Gender.FEMALE)
        .build()
    );
  }

}
