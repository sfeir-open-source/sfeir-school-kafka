package com.sfeir.kafka;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Map;
import java.util.Properties;

import static java.util.Collections.singletonMap;
import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

public class CountCustomersByGenderApp {

	private static final String BOOTSTRAP_SERVERS = "localhost:29092";
	private static final String SCHEMA_REGISTRY_URL = "http://localhost:8081";

	private SchemaRegistryClient client;

	public CountCustomersByGenderApp(SchemaRegistryClient client) {
		this.client = client;
	}

	public SpecificAvroSerde<Customer> customerSerde() {
		Map<String, String> serdeConfig = singletonMap(SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);

		SpecificAvroSerde<Customer> customerSerde = new SpecificAvroSerde<>(this.client);
		customerSerde.configure(serdeConfig, false);

		return customerSerde;
	}

	public Properties props() {
		Properties props = new Properties();

		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-03");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");
		props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1_000 + "");

		return props;
	}

	public Topology topology() {
		StreamsBuilder builder = new StreamsBuilder();

		builder
			.stream("customers", Consumed.with(Serdes.Integer(), customerSerde()))
			.selectKey((key, value) -> value.getGender().name())
			.through("customers_by_gender", Produced.with(Serdes.String(), customerSerde()))
			.groupByKey(Grouped.with(Serdes.String(), customerSerde()))
			.count(Materialized.as("customers_count_by_gender_store"))
			.toStream()
			.print(Printed.toSysOut());

		return builder.build();
	}

	public static void main(String... args) {
		CountCustomersByGenderApp app = new CountCustomersByGenderApp(
			new CachedSchemaRegistryClient(SCHEMA_REGISTRY_URL, 100)
		);

		KafkaStreams streams = new KafkaStreams(app.topology(), app.props());
		streams.start();

		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}

}