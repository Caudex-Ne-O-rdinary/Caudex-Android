package com.cmc.caudex.domain.model

data class Plant(
    val plantId: Int,
    val ratioX: Double,
    val ratioY: Double,
    val plantUrl: String,
    val scale: Int = 80,
)
