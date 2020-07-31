package ru.korolevss.currencyapp.model

import java.math.BigDecimal

data class CurrencyItem(
    val code: String,
    val name: String,
    val flagResource: Int,
    val nominal: Double,
    val value: Double
)