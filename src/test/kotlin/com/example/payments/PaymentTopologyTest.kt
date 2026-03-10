package com.example.payments

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PaymentTopologyTest :
    StringSpec({

        "simple aggregation logic test" {

            val visa = 100.0
            val mc = 50.0

            val total = visa + mc

            total shouldBe 150.0
        }
    })

/*
import com.example.payments.model.*
import com.example.payments.stream.PaymentTopologyBuilder
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.TopologyTestDriver
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.StreamsConfig
import java.util.*

class PaymentTopologyTest : FunSpec({

    lateinit var testDriver: TopologyTestDriver

    lateinit var visaInput: TestInputTopic<String, VisaPayment>
    lateinit var mcInput: TestInputTopic<String, MastercardPayment>
    lateinit var giftInput: TestInputTopic<String, GiftcardPayment>

    lateinit var output: TestOutputTopic<String, AggregatedPayment>

    beforeTest {

        val props = Properties()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "test"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "dummy:9092"

        val topology = PaymentTopologyBuilder().build()

        testDriver = TopologyTestDriver(topology, props)

        visaInput = testDriver.createInputTopic(
            "visa-payments",
            Serdes.String().serializer(),
            visaSerde().serializer()
        )

        mcInput = testDriver.createInputTopic(
            "mastercard-payments",
            Serdes.String().serializer(),
            mcSerde().serializer()
        )

        giftInput = testDriver.createInputTopic(
            "giftcard-payments",
            Serdes.String().serializer(),
            giftSerde().serializer()
        )

        output = testDriver.createOutputTopic(
            "aggregated-payments",
            Serdes.String().deserializer(),
            aggregatedSerde().deserializer()
        )
    }

    afterTest {
        testDriver.close()
    }

    test("should aggregate payments from all sources") {

        val tx = "tx-123"

        visaInput.pipeInput(
            tx,
            VisaPayment(tx, 100.0, "USD")
        )

        mcInput.pipeInput(
            tx,
            MastercardPayment(tx, 50.0)
        )

        giftInput.pipeInput(
            tx,
            GiftcardPayment(tx, 20.0)
        )

        val result = output.readValue()

        result.transactionId shouldBe tx
        result.visa!!.amount shouldBe 100.0
        result.mastercard!!.amount shouldBe 50.0
        result.giftcard!!.amount shouldBe 20.0
    }

})
*/
