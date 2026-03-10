package com.example.payments

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension

class KotestConfig : AbstractProjectConfig() {
    override val extensions: List<Extension> = listOf(SpringExtension())
}