package com.cmc.caudex.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class PlantUploadResult(
    val plantId: Int,
    val imageUrl: String,
)
