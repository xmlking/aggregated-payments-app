package com.example.payments.model

import kotlinx.serialization.Serializable

@Serializable
data class VisaPayment(
    val transactionId: String,
    val amount: Double,
    val currency: String,
)

@Serializable
data class MastercardPayment(
    val transactionId: String,
    val amount: Double,
)

@Serializable
data class GiftcardPayment(
    val transactionId: String,
    val amount: Double,
)

@Serializable
data class AggregatedPayment(
    val transactionId: String,
    val visa: VisaPayment? = null,
    val mastercard: MastercardPayment? = null,
    val giftcard: GiftcardPayment? = null,
)
