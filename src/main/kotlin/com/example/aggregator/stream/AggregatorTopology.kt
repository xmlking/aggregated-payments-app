package com.example.aggregator.stream

import com.example.aggregator.model.AggregatedMessage
import com.example.aggregator.model.SourceMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID
import java.util.function.Function
import kotlin.time.Clock

private val logger = KotlinLogging.logger {}

/**
 * Core aggregation processor.
 *
 * Strategy: merge all 4 input KStreams via [KStream.merge], then map
 * each [SourceMessage] into an [AggregatedMessage] envelope and forward
 * it to the single output topic.
 *
 * The function is wired to Spring Cloud Stream bindings declared in
 * application.yml:
 *   - inputs  : topic1-in, topic2-in, topic3-in, topic4-in
 *   - output  : aggregated-out
 */
@Configuration
class AggregatorTopology {
//    private val log = LoggerFactory.getLogger(javaClass)
    @Bean
    fun uppercase(): Function<KStream<String, String>, KStream<String, String>> =
        Function {
            it.peek { k, v -> logger.info { "State: $k, $v" } }
            it.mapValues { v -> v.uppercase() }
        }

    @Bean
    fun aggregate(): Function<Array<KStream<String, SourceMessage>>, KStream<String, AggregatedMessage>> =
        Function { inputs ->
            require(inputs.size == 4) {
                "Expected exactly 4 input streams, got ${inputs.size}"
            }

            val (stream1, stream2, stream3, stream4) = inputs

            // Tag each stream with its logical source name before merging
            val tagged1 = stream1.tagSource("topic-1")
            val tagged2 = stream2.tagSource("topic-2")
            val tagged3 = stream3.tagSource("topic-3")
            val tagged4 = stream4.tagSource("topic-4")

            // Merge all 4 streams into one
            val merged: KStream<String, SourceMessage> =
                tagged1
                    .merge(tagged2)
                    .merge(tagged3)
                    .merge(tagged4)

            // Transform each SourceMessage into an AggregatedMessage
            merged
                .peek { key, msg ->
                    logger.info { "Aggregating message key=$key source=${msg.source}" }
                }.mapValues { sourceMsg ->
                    AggregatedMessage(
                        aggregationId = UUID.randomUUID().toString(),
                        sources = listOf(sourceMsg.source),
                        messages = listOf(sourceMsg),
                        aggregatedAt = Clock.System.now(),
                    )
                }.also { stream ->
                    stream.peek { key, agg ->
                        logger.debug {
                            "Forwarding aggregated message id=${agg.aggregationId} totalCount=${agg.totalCount} to output topic"
                        }
                    }
                }
        }

    // ── Extension helpers ────────────────────────────────────────────────────

    /**
     * Ensures the [SourceMessage.source] field reflects the logical topic name.
     * Useful when the producer did not set a source or set it incorrectly.
     */
    private fun KStream<String, SourceMessage>.tagSource(sourceName: String): KStream<String, SourceMessage> =
        mapValues { msg -> msg.copy(source = sourceName) }
}
