import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

val springCloudVersion = "2025.1.1"
val kotestVersion = "6.1.6"
val kotlinLoggingVersion = "8.0.01"
val logbackVersion = "1.5.32"
val avro4kVersion = "2.10.0"

plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.spring") version "2.3.10"
    kotlin("plugin.noarg") version "2.3.10"
    kotlin("plugin.serialization") version "2.3.10"

//    Avro4k's gradle plugin, enabling kotlin code generation from avro schemas
//    id("io.github.avro-kotlin") version "2.10.0"
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.kotest") version "6.1.5"
    id("com.github.ben-manes.versions") version "0.53.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    // Import the Spring Cloud BOM
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion"))

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    // implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Spring boot basic
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // implementation("org.springframework.boot:spring-boot-starter-validation")

    // Spring cloud streams
    implementation("org.springframework.cloud:spring-cloud-stream")
//    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:$avro4kVersion")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    // Kotest Spring integration
    testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.apache.kafka:kafka-streams-test-utils")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-binder")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
        exceptionFormat = FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

noArg {
    invokeInitializers = true
//    annotation("micro.apps.model.NoArg")
//    annotation("com.redis.om.spring.annotations.Document")
    annotation("kotlinx.serialization.Serializable")
}

springBoot {
    mainClass.set("com.example.aggregator.ApplicationKt")
    buildInfo()
}
