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
    fun aggregate(): Function<KStream<String, SourceMessage>, KStream<String, AggregatedMessage>> =
        Function { input ->
            // All 4 source topics are merged by the binder via comma-separated destinations in application.yml.
            // The source field is expected to be set by the producer.
            input
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
}
