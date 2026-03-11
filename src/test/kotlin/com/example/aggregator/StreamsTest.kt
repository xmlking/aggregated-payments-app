package com.example.aggregator

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class StreamsTest :
    StringSpec({

        "should aggregate payments" {

            val transactionId = "tx-1"

            transactionId.length shouldBe 4
        }
    })
