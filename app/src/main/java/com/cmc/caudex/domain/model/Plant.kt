package com.cmc.caudex.domain.model

const val DEFAULT_PLANT_SCALE = 80

data class Plant(
    val plantId: Int,
    val ratioX: Double,
    val ratioY: Double,
    val plantUrl: String,
    val scale: Int = DEFAULT_PLANT_SCALE,
)
