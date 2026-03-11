package com.example.aggregator

import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.EnableTestBinder
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.messaging.support.GenericMessage

@SpringBootTest
@EnableTestBinder
@ApplyExtension(SpringExtension::class)
class ApplicationTest : FunSpec() {
    @Autowired
    lateinit var input: InputDestination

    @Autowired
    lateinit var output: OutputDestination

    init {
        test("application starts").config(enabled = false) {
            input.send(GenericMessage<ByteArray>("hello".toByteArray()))
            output.receive().getPayload() shouldBe "HELLO".toByteArray()
        }
    }
}
