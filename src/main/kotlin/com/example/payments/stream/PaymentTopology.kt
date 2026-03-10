package com.example.payments.stream

import com.example.payments.model.*
import org.apache.kafka.streams.kstream.KStream
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

@Configuration
class PaymentTopology {

    @Bean
    fun payments(): Function<Array<KStream<String, *>>, KStream<String, AggregatedPayment>> {

        return Function { streams ->

            val visa = streams[0] as KStream<String, VisaPayment>
            val mc = streams[1] as KStream<String, MastercardPayment>
            val gift = streams[2] as KStream<String, GiftcardPayment>

            val visaTable = visa.toTable()
            val mcTable = mc.toTable()
            val giftTable = gift.toTable()

            val visaMc = visaTable.join(mcTable) { v, m ->
                AggregatedPayment(v.transactionId, v, m, null)
            }

            val full = visaMc.join(giftTable) { agg, g ->
                agg.copy(giftcard = g)
            }

            full.toStream()
        }
    }
}

