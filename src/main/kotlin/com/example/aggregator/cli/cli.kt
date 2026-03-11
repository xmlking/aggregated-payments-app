@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.example.aggregator.cli

import com.github.avrokotlin.avro4k.*
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.*

@Serializable
data class Project(
    val name: String,
    val language: String,
)

private val logger = KotlinLogging.logger {}

fun main() {
    // Generating schemas
    val schema = Avro.schema<Project>()
    // {"type":"record","name":"Project","namespace":"myapp","fields":[{"name":"name","type":"string"},{"name":"language","type":"string"}]}
    println(schema.toString())

    logger.info { "Printing schema..." }
    logger.atWarn {
        message = schema.toString()
        payload =
            buildMap(capacity = 3) {
                put("foo", 1)
                put("bar", "x")
                put("obj", Pair(2, 3))
            }
    }

    // Serializing objects
    val data = Project("kotlinx.serialization", "Kotlin")
    val bytes = Avro.encodeToByteArray(data)

    // Deserializing objects
    val obj = Avro.decodeFromByteArray<Project>(bytes)
    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)
}
