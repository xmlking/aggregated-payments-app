package com.example.aggregator.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Represents a raw message arriving from any of the 4 source topics.
 */
@Serializable
data class SourceMessage(
    val id: String,
    val source: String, // Which topic this came from (topic1..topic4)
    val payload: String,
    val metadata: Map<String, String> = emptyMap(),
    val timestamp: Instant = Clock.System.now(),
)

/**
 * Represents the aggregated message published to the output topic.
 * Wraps one or more source messages into a single envelope.
 */
@Serializable
data class AggregatedMessage(
    val aggregationId: String,
    val sources: List<String>, // All source topics contributing
    val messages: List<SourceMessage>,
    val totalCount: Int = messages.size,
    val aggregatedAt: Instant = Clock.System.now(),
)
