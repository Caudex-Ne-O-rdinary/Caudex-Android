package com.cmc.caudex.domain.model

data class PlantDetail(
    val plantId: Int,
    val name: String,
    val imageUrl: String,
    val managementTip: String,
    val diaries: List<Diary>,
)
