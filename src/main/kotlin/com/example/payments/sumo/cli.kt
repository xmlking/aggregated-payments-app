package com.example.payments.sumo

import com.github.avrokotlin.avro4k.*
import kotlinx.serialization.*

@Serializable
data class Project(val name: String, val language: String)

fun main() {
    // Generating schemas
    val schema = Avro.schema<Project>()
    println(schema.toString()) // {"type":"record","name":"Project","namespace":"myapp","fields":[{"name":"name","type":"string"},{"name":"language","type":"string"}]}

    // Serializing objects
    val data = Project("kotlinx.serialization", "Kotlin")
    val bytes = Avro.encodeToByteArray(data)

    // Deserializing objects
    val obj = Avro.decodeFromByteArray<Project>(bytes)
    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)
}