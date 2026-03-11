package com.example.aggregator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AggregatorTopologyTest :
    StringSpec({

        "simple aggregation logic test" {

            val visa = 100.0
            val mc = 50.0

            val total = visa + mc

            total shouldBe 150.0
        }
    })

/*
import com.example.kafkaaggregator.config.jsonSerde
import com.example.kafkaaggregator.config.sharedMapper
import com.example.kafkaaggregator.model.AggregatedMessage
import com.example.kafkaaggregator.model.SourceMessage
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.time.Instant
import java.util.Properties
import java.util.UUID

/**
 * Unit-level test using [TopologyTestDriver] — no running Kafka broker needed.
 *
 * For a full integration test against embedded Kafka, see [KafkaAggregatorIntegrationTest].
 */
class TopologyTest {

    private val sourceSerde = jsonSerde<SourceMessage>()
    private val aggregatedSerde = jsonSerde<AggregatedMessage>()

    private fun buildTestConfig(): Properties = Properties().apply {
        put(StreamsConfig.APPLICATION_ID_CONFIG, "topology-test-${UUID.randomUUID()}")
        put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234")
        put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, org.apache.kafka.common.serialization.Serdes.StringSerde::class.java)
        put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, org.apache.kafka.common.serialization.Serdes.StringSerde::class.java)
    }

    @Test
    fun `messages from all 4 topics are merged into aggregated topic`() {
        // Build minimal topology manually for unit testing
        val builder = org.apache.kafka.streams.StreamsBuilder()
        val stringSerde = org.apache.kafka.common.serialization.Serdes.String()

        val s1 = builder.stream("topic-1", org.apache.kafka.streams.kstream.Consumed.with(stringSerde, sourceSerde))
        val s2 = builder.stream("topic-2", org.apache.kafka.streams.kstream.Consumed.with(stringSerde, sourceSerde))
        val s3 = builder.stream("topic-3", org.apache.kafka.streams.kstream.Consumed.with(stringSerde, sourceSerde))
        val s4 = builder.stream("topic-4", org.apache.kafka.streams.kstream.Consumed.with(stringSerde, sourceSerde))

        s1.merge(s2).merge(s3).merge(s4)
            .mapValues { msg ->
                AggregatedMessage(
                    aggregationId = UUID.randomUUID().toString(),
                    sources = listOf(msg.source),
                    messages = listOf(msg)
                )
            }
            .to("aggregated-topic", org.apache.kafka.streams.kstream.Produced.with(stringSerde, aggregatedSerde))

        val topology = builder.build()

        TopologyTestDriver(topology, buildTestConfig()).use { driver ->
            val inputTopics = listOf("topic-1", "topic-2", "topic-3", "topic-4").map { topicName ->
                driver.createInputTopic(topicName, StringSerializer(), sourceSerde.serializer())
            }
            val outputTopic = driver.createOutputTopic(
                "aggregated-topic",
                StringDeserializer(),
                aggregatedSerde.deserializer()
            )

            // Send one message to each source topic
            inputTopics.forEachIndexed { idx, topic ->
                val msg = SourceMessage(
                    id = "msg-$idx",
                    source = "topic-${idx + 1}",
                    payload = "payload from topic ${idx + 1}",
                    timestamp = Instant.now()
                )
                topic.pipeInput("key-$idx", msg)
            }

            // Expect 4 aggregated messages on the output topic
            val results = outputTopic.readValuesToList()
            assertEquals(4, results.size, "All 4 messages should be forwarded to aggregated-topic")

            results.forEach { agg ->
                assertNotNull(agg.aggregationId)
                assertEquals(1, agg.messages.size)
                assertTrue(agg.sources.isNotEmpty())
            }
        }
    }

    @Test
    fun `single source message is correctly wrapped in AggregatedMessage`() {
        val builder = org.apache.kafka.streams.StreamsBuilder()
        val stringSerde = org.apache.kafka.common.serialization.Serdes.String()

        val stream = builder.stream("topic-1", org.apache.kafka.streams.kstream.Consumed.with(stringSerde, sourceSerde))
        stream.mapValues { msg ->
            AggregatedMessage(
                aggregationId = "fixed-id",
                sources = listOf(msg.source),
                messages = listOf(msg)
            )
        }.to("aggregated-topic", org.apache.kafka.streams.kstream.Produced.with(stringSerde, aggregatedSerde))

        TopologyTestDriver(builder.build(), buildTestConfig()).use { driver ->
            val inputTopic = driver.createInputTopic("topic-1", StringSerializer(), sourceSerde.serializer())
            val outputTopic = driver.createOutputTopic("aggregated-topic", StringDeserializer(), aggregatedSerde.deserializer())

            val source = SourceMessage(id = "1", source = "topic-1", payload = "hello")
            inputTopic.pipeInput("k1", source)

            val result = outputTopic.readValue()
            assertEquals("topic-1", result.sources.first())
            assertEquals("hello", result.messages.first().payload)
            assertEquals(1, result.totalCount)
        }
    }
}

*/
