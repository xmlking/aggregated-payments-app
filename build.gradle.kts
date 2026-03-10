val kotestVersion = "6.1.5"
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
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Import the Spring Cloud BOM
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2025.1.1"))

    // Spring boot basic
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.cloud:spring-cloud-stream")
//    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")

    implementation("org.apache.kafka:kafka-streams")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")

    // Avro Kotlin
    implementation("com.github.avro-kotlin.avro4k:avro4k-core:2.10.0")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    // Kotest Spring integration
    testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.apache.kafka:kafka-streams-test-utils")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

noArg {
    invokeInitializers = true
//    annotation("micro.apps.model.NoArg")
//    annotation("com.redis.om.spring.annotations.Document")
    annotation("kotlinx.serialization.Serializable")
}

springBoot {
    mainClass.set("com.example.payments.ApplicationKt")
}
